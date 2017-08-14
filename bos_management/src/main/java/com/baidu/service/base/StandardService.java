package com.baidu.service.base;

import com.baidu.domain.Standard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by ZhangPei on 2017/7/26.
 */
public interface StandardService {
    void save(Standard standard);

    Page<Standard> standardPageQuery(Pageable pageable);

    List<Standard> findAllStandard();
}
