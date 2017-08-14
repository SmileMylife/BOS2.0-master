package com.baidu.service.impl;

import com.baidu.dao.base.CourierRepository;
import com.baidu.dao.base.FixedAreaRepository;
import com.baidu.dao.base.TakeTimeRepository;
import com.baidu.domain.Courier;
import com.baidu.domain.FixedArea;
import com.baidu.domain.TakeTime;
import com.baidu.service.base.FixedAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by ZhangPei on 2017/7/28.
 */
@Service
@Transactional
public class FixedAreaServiceImpl implements FixedAreaService {

    @Autowired
    private FixedAreaRepository far;

    //添加定区
    public void addFixedArea(FixedArea model) {
        far.save(model);
    }

    //分页查询
    public Page<FixedArea> fixedAreaPageQuery(Specification specification, Pageable pageable) {
        Page<FixedArea> page = far.findAll(specification, pageable);
        return page;
    }

    //关联快递员
    @Autowired
    private CourierRepository cr;
    @Autowired
    private TakeTimeRepository ttr;

    @Override
    public void associationCourier(Integer courierId, Integer takeTimeId, String fixedAreaId) {
        System.err.println(courierId);
        Courier courier = cr.findOne(courierId);
        FixedArea fixArea = far.findOne(fixedAreaId);
        fixArea.getCouriers().add(courier);
        TakeTime takeTime = ttr.findOne(takeTimeId);
        courier.setTakeTime(takeTime);
    }

}
