package com.wudy.timer.listener;

import com.wudy.timer.doc.TimerPayload;
import com.wudy.timer.enums.RedisExpireNotifyType;
import com.wudy.timer.helper.RedisHelper;
import com.wudy.timer.mq.sender.RedisExpireKeySender;
import com.wudy.timer.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TopicMessageListener implements MessageListener {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private RedisExpireKeySender redisExpireKeySender;
    @Autowired
    private RedisHelper redisHelper;

    @Override
    public void onMessage(Message message, byte[] bytes) {
        byte[] body = message.getBody();
        byte[] channel = message.getChannel();
        String topic = new String(channel);
        String expiredKey = new String(body);
        log.info("收到redis过期订阅通知:"+topic+",其key为》》"+expiredKey);
        TimerPayload timerPayload = mongoTemplate.findById(expiredKey, TimerPayload.class);
        int runTimes = timerPayload.getRunTimes() + 1;
        timerPayload.setRunTimes(runTimes);
        if (RedisExpireNotifyType.MQ.name().equals(timerPayload.getNotifyType())) {
            redisExpireKeySender.send(timerPayload);
        }
        if (RedisExpireNotifyType.URL.name().equals(timerPayload.getNotifyType())) {
            //TODO 网络请求
            CloseableHttpClient client = HttpClients.createDefault();
            String url = "http://www.baidu.com";
            HttpPost httpPost = new HttpPost(url);
        }
        mongoTemplate.updateFirst(Query.query(Criteria.where("taskId").is(expiredKey)), Update.update("runTimes", runTimes),TimerPayload.class);
        if (-1 == timerPayload.getTimes() || timerPayload.getTimes()-runTimes>=1) {
            long seconds = timerPayload.getSeconds()==null? DateUtils.seconds(timerPayload.getCron()):timerPayload.getSeconds();
            redisHelper.set(timerPayload.getTaskId(),timerPayload.getTaskName(),seconds);
        }
    }
}
