package com.baidu.dao.base;

import com.baidu.domain.Role;
import com.baidu.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by ZhangPei on 2017/8/14.
 */
public interface RoleRepository extends JpaRepository<Role,Integer> {

    List<Role> findByUsers(User user);
}
