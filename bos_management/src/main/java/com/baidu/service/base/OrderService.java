package com.baidu.service.base;

import com.baidu.domain.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * Created by ZhangPei on 2017/8/9.
 */
public interface OrderService {

    //保存订单
    @Path("/saveOrder")
    @POST
    @Consumes({"application/xml", "application/json"})
    public void saveOrder(Order order);

    //按订单号查询订单
    public Order findOrderByOrderNum(String orderNum);
}
