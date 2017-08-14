package com.baidu.dao.base;//package com.baidu.dao.base;

import com.baidu.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by ZhangPei on 2017/8/13.
 */
public interface UserRepository extends JpaRepository<User,Integer> {
    User findByUsername(String username);
}
