package com.wudy.timer;

import com.wudy.timer.helper.RedisHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TimerApplicationTests {
    @Autowired
    private RedisHelper redisHelper;

    @Test
    public void contextLoads() {
    }

    @Test
    public void test() {
        redisHelper.set("wudy","q233",5);
    }
}