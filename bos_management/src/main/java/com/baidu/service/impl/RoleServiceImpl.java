package com.baidu.service.impl;

import com.baidu.dao.base.RoleRepository;
import com.baidu.domain.Role;
import com.baidu.domain.User;
import com.baidu.service.base.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by ZhangPei on 2017/8/14.
 */
@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository rr;

    //查询角色
    @Override
    public List<Role> findByUsers(User user) {
        List<Role> list = rr.findByUsers(user);
        return list;
    }
}
