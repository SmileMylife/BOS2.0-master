package com.baidu.service.impl;

import com.baidu.dao.base.WayBillRepository;
import com.baidu.domain.WayBill;
import com.baidu.service.base.WayBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by ZhangPei on 2017/8/10.
 */
@Service
@Transactional
public class WayBillServiceImpl implements WayBillService {
    @Autowired
    private WayBillRepository wbr;

    //保存运单
    @Override
    public void saveWayBill(WayBill model) {
        wbr.save(model);
    }

    //分页查询
    @Override
    public Page<WayBill> wayBillPageQuery(Pageable pageable) {
        Page<WayBill> page = wbr.findAll(pageable);
        return page;
    }

    //查询运单
    @Override
    public WayBill findWayBillByWayBillNum(String wayBillNum) {
        WayBill wayBill = wbr.findByWayBillNum(wayBillNum);
        return wayBill;
    }
}
