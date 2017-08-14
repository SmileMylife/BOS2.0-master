package com.baidu.web.action;

import com.baidu.domain.TakeTime;
import com.baidu.service.base.TakeTimeService;
import com.baidu.web.action.base.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * Created by ZhangPei on 2017/8/1.
 */
@Controller
@ParentPackage("json-default")
@Namespace("/")
@Scope("prototype")
public class TakeTimeAction extends BaseAction<TakeTime> {

    //模型类
    @Autowired
    private TakeTimeService tts;

    @Action(value = "findTakeTime",results = {@Result(name = "success",type = "json")})
    //获取派送时间
    public String findTakeTime() {
        List<TakeTime> list = tts.findTakeTime();
        ActionContext.getContext().getValueStack().push(list);
        return SUCCESS;
    }

}

