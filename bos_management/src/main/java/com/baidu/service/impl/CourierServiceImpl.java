package com.baidu.service.impl;

import com.baidu.dao.base.CourierRepository;
import com.baidu.domain.Courier;
import com.baidu.service.base.CourierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by ZhangPei on 2017/7/26.
 */
@Service
@Transactional
public class CourierServiceImpl implements CourierService {

    @Autowired
    private CourierRepository cr;

    //新增快递员
    public void save(Courier courier) {
        cr.save(courier);
    }

    //按条件进行分页查询
    public Page<Courier> courierPageQuery(Specification<Courier> specification, Pageable pageable) {

        return cr.findAll(specification, pageable);
    }

    //删除快递员
    public void deleteCourier(Integer id) {
        cr.deleteCourier(id);
    }

    //查询所有快递员
    @Override
    public List<Courier> findCouriers() {
        List<Courier> list = cr.findAll();
        return list;
    }
}
