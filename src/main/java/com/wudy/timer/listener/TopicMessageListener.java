package com.wudy.timer.listener;

import com.wudy.timer.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TopicMessageListener implements MessageListener {
    @Autowired
    private TaskService taskService;

    @Override
    public void onMessage(Message message, byte[] bytes) {
        byte[] body = message.getBody();
        byte[] channel = message.getChannel();
        String topic = new String(channel);
        String taskId = new String(body);
        log.info("收到redis过期订阅通知:"+topic+",其key为》》"+taskId);
        taskService.doTask(taskId);
    }
}
