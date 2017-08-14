package com.baidu.dao.base;

import com.baidu.domain.Courier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by ZhangPei on 2017/7/26.
 */
public interface CourierRepository extends JpaRepository<Courier,Integer>,JpaSpecificationExecutor<Courier> {
    //实现删除快递员方法
    @Query(value = "update Courier set deltag = '1' where id = ?1")
    @Modifying
    public void deleteCourier(Integer id);

}
