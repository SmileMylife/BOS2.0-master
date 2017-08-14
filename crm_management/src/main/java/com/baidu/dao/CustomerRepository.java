package com.baidu.dao;

import com.baidu.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by ZhangPei on 2017/7/30.
 */
public interface CustomerRepository extends JpaSpecificationExecutor<Customer>, JpaRepository<Customer, Integer> {

    List<Customer> findByFixedAreaIdIsNull();

    List<Customer> findByFixedAreaId(String fixedAreaId);

    @Query(value = "update Customer set fixedAreaId = ?1 where id = ?2")
    @Modifying
    void updateFixedAreaId(String fixedAreaId, Integer customerId);

    Customer findByTelephone(String telephone);

    Customer findByTelephoneAndPassword(String telephone, String password);

    Customer findByEmailAndPassword(String email, String password);

    @Query(value = "select fixedAreaId from Customer where address = ?1")
    String findFixedAreaByAddress(String address);

}
