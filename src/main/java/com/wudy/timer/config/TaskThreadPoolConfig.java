package com.wudy.timer.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component(value = "taskThreadPoolConfig")
@ConfigurationProperties(prefix = "task.pool")
public class TaskThreadPoolConfig {
    private Integer corePoolSize;

    private Integer maxPoolSize;

    private Integer keepAliveSeconds;

    private Integer queueCapacity;

    private String threadNamePrefix;

}
