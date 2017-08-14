package com.baidu.service.impl;

import com.baidu.dao.base.StandardRepository;
import com.baidu.domain.Standard;
import com.baidu.service.base.StandardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by ZhangPei on 2017/7/26.
 */
@Service
@Transactional

public class StandardServiceImpl implements StandardService {

    @Autowired
    private StandardRepository sr;

    //新增取派标准
    public void save(Standard standard) {
        sr.save(standard);
    }

    //分页查询所有取派标准
    public Page<Standard> standardPageQuery(Pageable pageable) {
        Page<Standard> page = sr.findAll(pageable);
        return page;
    }

    //查询所有取派标准，用于加载下拉列表
    public List<Standard> findAllStandard() {
        List<Standard> list = sr.findAll();
        return list;
    }
}
