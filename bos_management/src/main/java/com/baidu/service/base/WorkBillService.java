package com.baidu.service.base;

import com.baidu.domain.Order;

/**
 * Created by ZhangPei on 2017/8/9.
 */
public interface WorkBillService {
    //生成工单并发送短信
    public void produceWorkBillAndSendMessage(final Order order);
}
