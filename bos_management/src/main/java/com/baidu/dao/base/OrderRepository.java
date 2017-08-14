package com.baidu.dao.base;

import com.baidu.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by ZhangPei on 2017/8/9.
 */
public interface OrderRepository extends JpaRepository<Order, Integer> {

    //查找订单通过订单号
    Order findByOrderNum(String orderNum);
}
