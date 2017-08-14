package com.baidu.dao.base;

import com.baidu.domain.FixedArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by ZhangPei on 2017/7/28.
 */
public interface FixedAreaRepository extends JpaRepository<FixedArea,String>,JpaSpecificationExecutor<FixedArea> {
}
