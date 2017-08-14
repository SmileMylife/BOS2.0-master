package com.baidu.service.base;

import com.baidu.domain.Role;
import com.baidu.domain.User;

import java.util.List;

/**
 * Created by ZhangPei on 2017/8/14.
 */
public interface RoleService {
    List<Role> findByUsers(User user);
}
