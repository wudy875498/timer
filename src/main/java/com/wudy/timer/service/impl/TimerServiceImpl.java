package com.wudy.timer.service.impl;

import com.wudy.timer.doc.TimerPayload;
import com.wudy.timer.helper.RedisHelper;
import com.wudy.timer.mq.sender.RedisExpireKeySender;
import com.wudy.timer.service.TimerService;
import com.wudy.timer.utils.DateUtils;
import com.wudy.timer.vo.ResponseObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class TimerServiceImpl implements TimerService {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private RedisExpireKeySender redisExpireKeySender;
    @Autowired
    private RedisHelper redisHelper;
    /**
     * 提交一个任务
     *
     * @param timerPayload
     * @return
     */
    @Override
    public ResponseObj submitTask(TimerPayload timerPayload) {
        mongoTemplate.insert(timerPayload);
        //立即执行
        if (0 == timerPayload.getTimes()) {
            redisExpireKeySender.send(timerPayload);
        } else { //执行一次以上
            long seconds = timerPayload.getSeconds()==null?DateUtils.seconds(timerPayload.getCron()):timerPayload.getSeconds();
            redisHelper.set(timerPayload.getTaskId(),timerPayload.getTaskName(),seconds);
        }
        return ResponseObj.success();
    }
}
