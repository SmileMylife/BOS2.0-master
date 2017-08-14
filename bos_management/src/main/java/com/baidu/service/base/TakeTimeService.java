package com.baidu.service.base;

import com.baidu.domain.TakeTime;

import java.util.List;

/**
 * Created by ZhangPei on 2017/8/1.
 */
public interface TakeTimeService {

    //查询派送时间
    List<TakeTime> findTakeTime();
}
