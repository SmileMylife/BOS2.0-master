package com.baidu.service.base;

import com.baidu.domain.PageBean;
import com.baidu.domain.Promotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.ws.rs.*;
import java.util.Date;

/**
 * Created by ZhangPei on 2017/8/5.
 */
public interface PromotionService {
    //保存宣传内容
    void savePromotion(Promotion model);

    //查找所有宣传活动
    Page<Promotion> findPromotion(Pageable pageable);

    //查找宣传活动
    @Path("/pageQueryOfFront")
    @GET
    @Produces({"application/json", "application/xml"})
    @Consumes({"application/json", "application/xml"})
    PageBean<Promotion> pageQueryOfFront(@QueryParam("page") Integer page,@QueryParam("rows") Integer rows);
    //查找详情页面
    @Path("/findPromotion")
    @GET
    @Produces({"application/json","application/xml"})
    public Promotion findPromotion(@QueryParam("id") Integer id);
    //更新活动状态
    public void updatePromotion(Date date);
}
