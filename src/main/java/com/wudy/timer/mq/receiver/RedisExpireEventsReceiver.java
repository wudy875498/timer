package com.wudy.timer.mq.receiver;

import com.wudy.timer.doc.TimerPayload;
import com.wudy.timer.enums.MessageQueue;
import com.wudy.timer.helper.RedisHelper;
import com.wudy.timer.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;

@Slf4j
@Service
@RabbitListener(queues = MessageQueue.TIMER_QUEUE_NAME)
public class RedisExpireEventsReceiver {
    @RabbitHandler
    public void receive(TimerPayload payload) {
        log.info("接收到消息");
    }
}
