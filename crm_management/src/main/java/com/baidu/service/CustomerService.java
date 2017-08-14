package com.baidu.service;

import com.baidu.domain.Customer;

import javax.ws.rs.*;
import java.util.List;

/**
 * Created by ZhangPei on 2017/7/30.
 */
public interface CustomerService {

    //查询未关联客户
    @Path("/findNoAssociationCustomers")
    @GET
    @Produces({"application/xml", "application/json"})
    public List<Customer> findNoAssociationCustomers();

    //查询关联到指定定区的客户
    @Path("/findAssociationCustomers/{fixedAreaId}")
    @GET
    @Produces({"application/xml", "application/json"})
    @Consumes({"application/xml", "application/json"})
    public List<Customer> findAssociationCustomers(@PathParam("fixedAreaId") String fixedAreaId);

    //关联定区到客户
    @Path("/associationFixedAreaToCustomer")
    @PUT
    //@QueryParam接收的是？后边的键值对，而@PathParam后边接收的是/后边的值。
    public void associationFixedAreaToCustomer(@QueryParam("noAssociationCustomers") String noAssociationCustomers, @QueryParam("associationCustomers") String associationCustomers, @QueryParam("fixedAreaId") String fixedAreaId);

    //保存客户到数据库
    @Path("/saveCustomer")
    @POST
    @Consumes({"application/xml", "application/json"})
    public void saveCustomer(Customer customer);

    //查询客户
    @Path("/findCustomer/{telephone}")
    @GET
    @Produces({"application/json", "application/xml"})
    public Customer findCustomer(@PathParam("telephone") String telephone);

    //邮箱登录验证
    @Path("/loginByEmail")
    @GET
    @Produces({"application/json", "application/xml"})
    public Customer loginByEmail(@QueryParam("email") String email, @QueryParam("password") String password);

    //手机号登录验证
    @Path("/loginByTelephone")
    @GET
    @Produces({"application/json", "application/xml"})
    public Customer loginByTelephone(@QueryParam("telephone") String telephone, @QueryParam("password") String password);

    //根据地址查询定区
    @Path("/findFixedAreaByAddress")
    @GET
    @Produces({"application/xml", "application/json"})
    public String findFixedAreaByAddress(@QueryParam("address") String address);
}
