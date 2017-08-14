package com.baidu.web.quartz;

import com.baidu.service.base.PromotionService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created by ZhangPei on 2017/8/7.
 */
public class ListeningPromotionJob implements Job {
    @Autowired
    private PromotionService ps;

    //监听宣传活动是否过期
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.err.println("执行了调度器！");
//        ps.updatePromotion(new Date());
    }
}
