package com.baidu.service.impl;

import com.baidu.dao.base.PromotionRepository;
import com.baidu.domain.PageBean;
import com.baidu.domain.Promotion;
import com.baidu.service.base.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by ZhangPei on 2017/8/5.
 */
@Service
@Transactional
public class PromotionServiceImpl implements PromotionService {
    @Autowired
    private PromotionRepository pr;

    //保存宣传内容
    @Override
    public void savePromotion(Promotion model) {
        pr.save(model);
    }

    //查询宣传活动
    @Override
    public Page<Promotion> findPromotion(Pageable pageable) {
        Page<Promotion> page = pr.findAll(pageable);
        return page;
    }

    //前台系统查询宣传活动分页
    @Override
    public PageBean<Promotion> pageQueryOfFront(Integer page, Integer rows) {
        Pageable pageable = new PageRequest(page - 1, rows);
        Page<Promotion> pageResult = pr.findAll(pageable);
        PageBean<Promotion> pageBean = new PageBean<>();
        pageBean.setRows(pageResult.getContent());
        pageBean.setTotal(((Long) pageResult.getTotalElements()).intValue());
        return pageBean;
    }

    //查找详情页面
    @Override
    public Promotion findPromotion(Integer id) {
        Promotion result = pr.findOne(id);
        return result;
    }

    //更新活动状态
    @Override
    public void updatePromotion(Date date) {
        pr.updatePromotion(date);
    }
}
