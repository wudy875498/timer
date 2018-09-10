package com.wudy.timer.mq.sender;

import com.wudy.timer.doc.TimerPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RedisExpireKeySender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(TimerPayload payload) {
        rabbitTemplate.convertAndSend(payload.getExchange(),payload.getRoutingKey(),payload);
        log.info("这是第"+payload.getRunTimes()+"次执行任务");
        log.info("消息已发送");
    }
}
