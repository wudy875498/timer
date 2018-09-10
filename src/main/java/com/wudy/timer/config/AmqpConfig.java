package com.wudy.timer.config;

import com.wudy.timer.converter.HessianMessageConverter;
import com.wudy.timer.enums.MessageQueue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpConfig {
    @Autowired
    private ConnectionFactory connectionFactory;

    /***********************配置定时器-通知业务消息队列Start*********************************/
    @Bean
    Queue timerNotificationQueue() {
        return new Queue(MessageQueue.TIMER_QUEUE.getQueue());
    }

    @Bean
    DirectExchange timerNotificationExchange() {
        return new DirectExchange(MessageQueue.TIMER_QUEUE.getExchange());
    }

    @Bean
    Binding timerNotificationBinding(Queue timerNotificationQueue, DirectExchange timerNotificationExchange) {
        return BindingBuilder.bind(timerNotificationQueue).to(timerNotificationExchange).with(MessageQueue.TIMER_QUEUE.getRoutingKey());
    }
    /*********************配置定时器-通知业务消息队列End*********************************/

    /**
     * 自定义RabbitTemplate，主要是改造MessageConverter。
     * 默认用的是SimpleMessageConverter，这对于复杂对象（对象中会嵌套其他对象）的情况下，会出现序列化问题。
     * 改用Hessian，实际上是把对象序列化成二进制，较为通用。
     * 不建议使用JDK默认提供的序列化工具。
     *
     * @return RabbitTemplate
     */
    @Bean
    RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        rabbitTemplate.setMessageConverter(new HessianMessageConverter());
        return rabbitTemplate;
    }

    /**
     * issue: no method found for class [B
     *
     * @return SimpleRabbitListenerContainerFactory
     */
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new HessianMessageConverter());
        return factory;
    }
}
