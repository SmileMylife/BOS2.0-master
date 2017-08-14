package com.baidu.dao.base;

import com.baidu.domain.TakeTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by ZhangPei on 2017/8/1.
 */
public interface TakeTimeRepository extends JpaRepository<TakeTime,Integer>,JpaSpecificationExecutor<TakeTime> {

}
