package com.baidu.service.impl;

import com.baidu.dao.CustomerRepository;
import com.baidu.domain.Customer;
import com.baidu.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by ZhangPei on 2017/7/30.
 */
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository cr;

    //查询所有未关联客户
    public List<Customer> findNoAssociationCustomers() {
        List<Customer> list = cr.findByFixedAreaIdIsNull();
        return list;
    }

    //查询已经关联的客户
    public List<Customer> findAssociationCustomers(String fixedAreaId) {
        System.err.println(fixedAreaId + "service");
        List<Customer> list = cr.findByFixedAreaId(fixedAreaId);
        return list;
    }

    //关联定区到客户
    public void associationFixedAreaToCustomer(String noAssociationCustomers, String associationCustomers, String fixedAreaId) {
        if (noAssociationCustomers != null) {
            String[] noAssociationCustomersArr = noAssociationCustomers.split("-");
            for (String id : noAssociationCustomersArr) {
                Integer customerId = Integer.parseInt(id);
                cr.updateFixedAreaId(null, customerId);
            }
        }
        if (associationCustomers != null) {
            String[] associationCustomersArr = associationCustomers.split("-");
            for (String id : associationCustomersArr) {
                Integer customerId = Integer.parseInt(id);
                cr.updateFixedAreaId(fixedAreaId, customerId);
            }
        }
    }

    //保存客户
    @Override
    public void saveCustomer(Customer customer) {
        cr.save(customer);
    }

    //查询客户
    @Override
    public Customer findCustomer(String telephone) {
        Customer customer = cr.findByTelephone(telephone);
        return customer;
    }

    //邮箱登录验证
    @Override
    public Customer loginByEmail(String email, String password) {
        Customer customer = cr.findByEmailAndPassword(email, password);
        return customer;
    }

    //手机号登录验证
    @Override
    public Customer loginByTelephone(String telephone, String password) {
        Customer customer = cr.findByTelephoneAndPassword(telephone, password);
        return customer;
    }
    //根据地址查询定区
    @Override
    public String findFixedAreaByAddress(String address) {
        String fixedAreaId = cr.findFixedAreaByAddress(address);
        return fixedAreaId;
    }

    //根据地址查找定区

}
