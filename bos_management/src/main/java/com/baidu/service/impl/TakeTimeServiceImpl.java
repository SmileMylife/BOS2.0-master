package com.baidu.service.impl;

import com.baidu.dao.base.TakeTimeRepository;
import com.baidu.domain.TakeTime;
import com.baidu.service.base.TakeTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by ZhangPei on 2017/8/1.
 */
@Service
@Transactional
public class TakeTimeServiceImpl implements TakeTimeService {

    @Autowired
    private TakeTimeRepository ttr;

    //查询派送时间
    @Override
    public List<TakeTime> findTakeTime() {
        List<TakeTime> list = ttr.findAll();
        return list;
    }
}
