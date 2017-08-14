package com.baidu.action;

import com.baidu.domain.Area;
import com.baidu.domain.Customer;
import com.baidu.domain.Order;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.ws.rs.core.MediaType;

/**
 * Created by ZhangPei on 2017/8/9.
 */
@ParentPackage("json-default")
@Namespace("/")
@Scope("prototype")
@Controller
public class OrderAction extends ActionSupport implements ModelDriven<Order> {
    //订单模型
    private Order order = new Order();

    @Override
    public Order getModel() {
        return order;
    }

    //收件人区域和发件人区域
    private String sendAreaInfo;
    private String recAreaInfo;

    public void setSendAreaInfo(String sendAreaInfo) {
        this.sendAreaInfo = sendAreaInfo;
    }

    public void setRecAreaInfo(String recAreaInfo) {
        this.recAreaInfo = recAreaInfo;
    }

    //处理订单数据

    @Action(value = "placeOrder",results = {@Result(name = "input",type = "redirect",location = "order.html")})
    public String placeOrder() {
        System.out.println(order);
        //提取省市区，存入订单对象中
        if (sendAreaInfo == null || recAreaInfo == null) {
            return INPUT;
        }
        //设置寄件人区域
        String[] sendAreaArr = sendAreaInfo.split("/");
        Area sendArea = new Area();
        sendArea.setProvince(sendAreaArr[0]);
        sendArea.setCity(sendAreaArr[1]);
        sendArea.setDistrict(sendAreaArr[2]);
        //设置收件人区域
        String[] recieveAreaArr = recAreaInfo.split("/");
        Area recieveArea = new Area();
        recieveArea.setProvince(recieveAreaArr[0]);
        recieveArea.setCity(recieveAreaArr[1]);
        recieveArea.setDistrict(recieveAreaArr[2]);
        order.setRecArea(recieveArea);
        order.setSendArea(sendArea);
        Customer customer = (Customer)ServletActionContext.getRequest().getSession().getAttribute("customer");
        order.setCustomer_id(customer.getId());
        //调用webservice向bos_management传递order对象
        String url = "http://localhost:8080/bos_management/services/orderService/saveOrder";
        WebClient.create(url).type(MediaType.APPLICATION_JSON).post(order);
        return NONE;
    }
}
