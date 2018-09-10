package com.wudy.timer.service;

import com.wudy.timer.doc.TimerPayload;
import com.wudy.timer.vo.ResponseObj;

public interface TimerService {

    /**
     * 提交一个任务
     * @param timerPayload
     * @return
     */
    ResponseObj submitTask(TimerPayload timerPayload);
}
