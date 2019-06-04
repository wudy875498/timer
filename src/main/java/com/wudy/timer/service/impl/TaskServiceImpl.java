package com.wudy.timer.service.impl;

import com.wudy.timer.doc.TimerPayload;
import com.wudy.timer.enums.RedisExpireNotifyType;
import com.wudy.timer.helper.RedisHelper;
import com.wudy.timer.mq.sender.RedisExpireKeySender;
import com.wudy.timer.service.TaskService;
import com.wudy.timer.utils.DateUtils;
import com.wudy.timer.utils.HttpClientUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

/**
 * ClassName TaskServiceImpl
 * Description 任务执行类
 *
 * @Author wudy
 * @Date 2019/6/4 16:57
 * @Version 1.0
 **/
@Service
@Slf4j
public class TaskServiceImpl implements TaskService {
    @Autowired
    private RedisExpireKeySender redisExpireKeySender;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private RedisHelper redisHelper;

    @Override
    public void doTask(String taskId) {
        TimerPayload payload = mongoTemplate.findById(taskId, TimerPayload.class);
        Integer runTimes = payload.getRunTimes() + 1;
        if (RedisExpireNotifyType.MQ.name().equals(payload.getNotifyType())) {
            redisExpireKeySender.send(payload);
        }
        if (RedisExpireNotifyType.URL.name().equals(payload.getNotifyType())) {
            CloseableHttpResponse response;
            if (payload.isJsonBody()) {
                response = HttpClientUtils.postJson(payload.getNotifyUrl(),payload.getParams());
            } else {
                response = HttpClientUtils.postMap(payload.getNotifyUrl(),payload.getParams());
            }
            if (response != null) {
                log.info(response.toString());
            }
        }
        mongoTemplate.updateFirst(Query.query(Criteria.where("taskId").is(payload.getTaskId())), Update.update("runTimes", runTimes), TimerPayload.class);
        if (-1 == payload.getTimes() || payload.getTimes()-runTimes>=1) {
            long seconds = payload.getSeconds()==null? DateUtils.seconds(payload.getCron()):payload.getSeconds();
            log.info("请求间隔："+seconds);
            redisHelper.set(payload.getTaskId(),payload.getTaskName(),seconds);
        }
    }
}
