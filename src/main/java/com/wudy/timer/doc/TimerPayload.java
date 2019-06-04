package com.wudy.timer.doc;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
@Document(collection = "timer_payload")
public class TimerPayload implements Serializable {
    /**
     * 任务id，同时也是redis的key
     */
    @Id
    private String taskId;
    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 执行次数，-1:无限制,0-立即执行
     */
    private Integer times;

    /**
     * 已经执行次数
     */
    private Integer runTimes=0;
    /**
     * 执行时间，cron表达式
     */
    private String cron;
    /**
     * 执行时间间隔，单位：秒，表示多长时间执行一次
     */
    private Long seconds;
    /**
     * 任务调起方式：mq-消息队列，url-http请求
     * 推荐使用mq
     */
    private String notifyType;
    /**
     * 回调地址，全路径url
     */
    private String notifyUrl;
    /**
     * rabbitmq的交换机名称
     */
    private String exchange;
    /**
     * rabbitmq的路由
     */
    private String routingKey;

    private boolean jsonBody;

    /**
     * 额外参数，业务参数
     */
    private Map<String,String> params;
}
