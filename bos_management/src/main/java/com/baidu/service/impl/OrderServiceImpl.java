package com.baidu.service.impl;

import com.baidu.dao.base.AreaRepository;
import com.baidu.dao.base.FixedAreaRepository;
import com.baidu.dao.base.OrderRepository;
import com.baidu.domain.*;
import com.baidu.service.base.OrderService;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.core.MediaType;
import java.util.Set;
import java.util.UUID;

/**
 * Created by ZhangPei on 2017/8/9.
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private AreaRepository ar;
    @Autowired
    private OrderRepository or;
    @Autowired
    private FixedAreaRepository far;

    //保存订单操作
    @Override
    public void saveOrder(Order order) {
        //因为此前传入的区域中没有id值，所以一会直接操作数据库会报错，在这里我们把area在查出来，成为持久对象。
        //查持久化寄件人区域
        Area sendArea = order.getSendArea();
        String sendProvince = sendArea.getProvince();
        String sendCity = sendArea.getCity();
        String sendDistrict = sendArea.getDistrict();
        Area sendAreaResult = ar.findByProvinceAndCityAndDistrict(sendProvince, sendCity, sendDistrict);
        //持久化收件人区域
        Area recArea = order.getRecArea();
        String recieveProvince = recArea.getProvince();
        String recieveCity = recArea.getCity();
        String recieveDistrict = recArea.getDistrict();
        Area recieveAreaResult = ar.findByProvinceAndCityAndDistrict(recieveProvince, recieveCity, recieveDistrict);
        //将区域信息保存到订单
        order.setSendArea(sendAreaResult);
        order.setRecArea(recieveAreaResult);
        //根据填写的地址在crm系统中查找定区
        String url = "http://localhost:9008/crm_management/services/customerService/findFixedAreaByAddress?address=" + order.getSendAddress();
        String fixedAreaId = WebClient.create(url).accept(MediaType.APPLICATION_JSON).get(String.class);
        if (fixedAreaId != null) {
            FixedArea fixedArea = far.findOne(fixedAreaId);
            Set<Courier> couriers = fixedArea.getCouriers();
            Courier courier = couriers.iterator().next();
            if (courier == null) {
                //转入人工派单
            } else {
                order.setCourier(courier);
                order.setOrderNum(UUID.randomUUID().toString().replace("-", ""));
            }
            //保存订单到数据库
            or.save(order);
            //生成工单发送短信

            return;
        }
        //基于分区关键字自动分单
        Set<SubArea> subareas = sendAreaResult.getSubareas();
        for (SubArea subarea : subareas) {
            if (order.getSendAddress().contains(subarea.getKeyWords())) {
                FixedArea fixedArea = subarea.getFixedArea();
                Set<Courier> couriers = fixedArea.getCouriers();
                Courier courier = couriers.iterator().next();
                order.setCourier(courier);
                order.setOrderNum(UUID.randomUUID().toString().replace("-", ""));
                or.save(order);
                //生成工单发送短信
                return;
            }
        }
        //基于辅助关键字进行分单
        for (SubArea subarea : subareas) {
            if (order.getSendAddress().contains(subarea.getAssistKeyWords())) {
                Set<Courier> couriers = subarea.getFixedArea().getCouriers();
                Courier courier = couriers.iterator().next();
                order.setCourier(courier);
                order.setOrderNum(UUID.randomUUID().toString().replace("-", ""));
                or.save(order);
                //生成工单发送短信
                return;
            }
        }
        //人工分单
        order.setOrderType("2");
        or.save(order);
    }

    //查询订单通过订单号
    @Override
    public Order findOrderByOrderNum(String orderNum) {
        Order order = or.findByOrderNum(orderNum);
        return order;
    }
}
