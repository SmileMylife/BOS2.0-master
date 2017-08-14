package com.baidu.service.impl;

import com.baidu.dao.base.WorkBillRepository;
import com.baidu.domain.Order;
import com.baidu.domain.WorkBill;
import com.baidu.service.base.WorkBillService;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by ZhangPei on 2017/8/9.
 */
@Service
@Transactional
public class WorkBillServiceImpl implements WorkBillService {
    @Autowired
    private WorkBillRepository wbr;
    //生成工单并发送短信
    @Override
    public void produceWorkBillAndSendMessage(final Order order) {
        WorkBill workBill = new WorkBill();
        workBill.setType("新");
        workBill.setPickstate("未取件");
        workBill.setBuildtime(new Date());
        workBill.setRemark(order.getRemark());
        //生成短信验证码
        String meassage = RandomStringUtils.randomNumeric(4);
        workBill.setSmsNumber(meassage);
        workBill.setOrder(order);
        workBill.setCourier(order.getCourier());
        wbr.save(workBill);
        //调用发短信接口进行发送短信

        workBill.setPickstate("已通知");
    }
}
