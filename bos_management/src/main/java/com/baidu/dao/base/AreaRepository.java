package com.baidu.dao.base;

import com.baidu.domain.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by ZhangPei on 2017/7/27.
 */
public interface AreaRepository extends JpaRepository<Area,String>,JpaSpecificationExecutor<Area> {
    //查询区域名称
    public Area findByProvinceAndCityAndDistrict(String province, String city, String district);
}
