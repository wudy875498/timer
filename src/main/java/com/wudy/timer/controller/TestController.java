package com.wudy.timer.controller;

import com.wudy.timer.doc.TimerPayload;
import com.wudy.timer.service.TimerService;
import com.wudy.timer.vo.ResponseObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(value = "timer")
public class TestController {
    @Autowired
    private TimerService timerService;

    @RequestMapping(value = "submit")
    public ResponseObj submitTask(@RequestBody TimerPayload payload) {
        payload.setTaskId(UUID.randomUUID().toString().replaceAll("-",""));
        return timerService.submitTask(payload);
    }
}
