package com.baidu.web.action;

import com.baidu.domain.Order;
import com.baidu.service.base.OrderService;
import com.baidu.web.action.base.BaseAction;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ZhangPei on 2017/8/10.
 */
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")

public class OrderAction extends BaseAction<Order> {

    @Autowired
    private OrderService os;

    //订单回显

    @Action(value = "findOrderByOrderNum", results = {@Result(name = "success", type = "json")})
    public String findOrderByOrderNum() {
        Order order = os.findOrderByOrderNum(model.getOrderNum());
        Map<String, Object> map = new HashMap<>();
        if (order == null) {
            map.put("status", 0);
        } else {
            map.put("status", 1);
            map.put("data", order);
        }
        ServletActionContext.getContext().getValueStack().push(map);
        return SUCCESS;
    }
}
