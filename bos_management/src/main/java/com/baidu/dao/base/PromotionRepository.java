package com.baidu.dao.base;

import com.baidu.domain.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

/**
 * Created by ZhangPei on 2017/8/5.
 */
public interface PromotionRepository extends JpaRepository<Promotion,Integer> {
    @Query(value = "update Promotion set status = '0' where ?1 > endDate and status = '1'")
    @Modifying
    void updatePromotion(Date date);
}
