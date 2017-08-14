package com.baidu.service.base;

import com.baidu.domain.Courier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * Created by ZhangPei on 2017/7/26.
 */
public interface CourierService {
    //保存快递员
    void save(Courier courier);

    //快递员分页
    Page<Courier> courierPageQuery(Specification<Courier> specification, Pageable pageable);

    //删除快递员
    void deleteCourier(Integer i);

    //查询所有快递员
    List<Courier> findCouriers();
}
