package com.baidu.service.base;

import com.baidu.domain.WayBill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by ZhangPei on 2017/8/10.
 */
public interface WayBillService {
    //保存运单
    void saveWayBill(WayBill model);

    //分页查询运单
    Page<WayBill> wayBillPageQuery(Pageable pageble);

    WayBill findWayBillByWayBillNum(String wayBillNum);
}
