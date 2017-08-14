package com.baidu.service.base;

import com.baidu.domain.Courier;
import com.baidu.domain.FixedArea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * Created by ZhangPei on 2017/7/28.
 */
public interface FixedAreaService {
    //添加定区
    void addFixedArea(FixedArea model);

    //定区分页
    Page<FixedArea> fixedAreaPageQuery(Specification specification, Pageable pageable);

    //关联快递员
    void associationCourier(Integer courierId, Integer takeTimeId, String fixedAreaId);
}
