package com.baidu.test;

import com.baidu.domain.Customer;
import com.baidu.service.CustomerService;
import com.baidu.service.impl.CustomerServiceImpl;
import org.apache.cxf.jaxrs.client.WebClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by ZhangPei on 2017/7/30.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class TestService {
    @Qualifier("customerServiceImpl")
    @Autowired
    private CustomerService cs;

    @Test
    public void test1() {
        List<Customer> list = cs.findNoAssociationCustomers();
        System.err.println(list.toString());
    }

    @Test
    public void test2() {
        List<Customer> list = cs.findAssociationCustomers("100");
        System.err.println(list.toString());
    }

    @Test
    public void test3() {
        Customer login = cs.loginByTelephone("13112345678", "123456");
        System.err.println(login);
    }

    //测试根据地址查询定区
    @Test
    public void test4() {
        String fixedAreaId = cs.findFixedAreaByAddress("陕西省安康市");
        System.err.println(fixedAreaId);
    }
}
