package com.baidu.service.base;

import com.baidu.domain.Area;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;

/**
 * Created by ZhangPei on 2017/7/27.
 */
public interface AreaService {
    void saveArea(ArrayList<Area> list);

    Page<Area> areaPageQuery(Specification<Area> specification, Pageable pageable);
}
