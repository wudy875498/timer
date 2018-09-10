package com.wudy.timer.enums;

import lombok.Getter;

@Getter
public enum MessageQueue {
    /**
     * 定时器消息队列，direct
     */
    TIMER_QUEUE("X-exchange-timer","X-queue-timer-notification","direct.timer.notification","notification")
    ;

    private String exchange;
    private String queue;
    private String routingKey;
    private String biz;

    MessageQueue(String exchange, String queue, String routingKey, String biz) {
        this.exchange = exchange;
        this.queue = queue;
        this.routingKey = routingKey;
        this.biz = biz;
    }

    public static final String TIMER_QUEUE_NAME = "X-queue-timer-notification";
}
