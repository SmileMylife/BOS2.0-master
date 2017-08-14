package com.baidu.test;

import com.baidu.domain.Customer;
import com.baidu.domain.PageBean;
import org.apache.cxf.jaxrs.client.WebClient;
import org.junit.Test;

import javax.ws.rs.core.MediaType;


/**
 * Created by ZhangPei on 2017/8/6.
 */
public class PromotionTest {
    @Test
    public void test1() {
        String url = "http://localhost:8080/bos_management/services/promotionService/pageQueryOfFront?page=" + 1 + "&rows=" + 2;
        PageBean pageBean = WebClient.create(url).accept(MediaType.APPLICATION_JSON).get(PageBean.class);
        System.out.println(pageBean);
    }
    //测试登录功能
    @Test
    public void test3() {
        String url = "http://localhost:9008/crm_management/services/customerService/login?telephone=" + "13112345678" + "&password=" + "123456";
        Customer customer = WebClient.create(url).accept(MediaType.APPLICATION_JSON).get(Customer.class);
        System.err.println(customer);
    }
}
