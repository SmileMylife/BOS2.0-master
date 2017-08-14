package com.baidu.service.impl;

import com.baidu.dao.base.AreaRepository;
import com.baidu.domain.Area;
import com.baidu.service.base.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * Created by ZhangPei on 2017/7/27.
 */
@Service
@Transactional
public class AreaServiceImpl implements AreaService {
    @Autowired
    private AreaRepository ar;

    //添加区域设置
    public void saveArea(ArrayList<Area> list) {
        ar.save(list);
    }

    //根据条件进行查询
    public Page<Area> areaPageQuery(Specification<Area> specification, Pageable pageable) {
        return ar.findAll(specification, pageable);
    }
}
