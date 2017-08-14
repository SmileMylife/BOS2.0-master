package com.baidu.dao.base;

import com.baidu.domain.WayBill;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by ZhangPei on 2017/8/10.
 */
public interface WayBillRepository extends JpaRepository<WayBill,Integer> {

    WayBill findByWayBillNum(String wayBillNum);
}
