package com.baidu.service.base;

import com.baidu.domain.User;

/**
 * Created by ZhangPei on 2017/8/13.
 */
public interface UserService {

    User findByUsername(String username);
}
