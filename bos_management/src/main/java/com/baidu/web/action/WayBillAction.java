package com.baidu.web.action;

import com.baidu.domain.Message;
import com.baidu.domain.WayBill;
import com.baidu.service.base.WayBillService;
import com.baidu.web.action.base.BaseAction;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by ZhangPei on 2017/8/10.
 */
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class WayBillAction extends BaseAction<WayBill> {
    @Autowired
    private WayBillService wbs;

    //保存运单
    @Action(value = "saveWayBill", results = {@Result(name = "success", type = "json")})
    public String saveWayBill() {
        if (model == null) {
            Message message = new Message(1,"保存失败，请完整填写表单！！");
            ServletActionContext.getContext().getValueStack().push(message);
            return SUCCESS;
        }
        if (model.getOrder() == null || model.getOrder().getId() == null) {
            model.setOrder(null);
        }
        wbs.saveWayBill(model);
        Message message = new Message(1,"保存成功！");
        ServletActionContext.getContext().getValueStack().push(message);
        return SUCCESS;
    }

    //运单分页查询
    @Action(value = "wayBillPageQuery",results = {@Result(name = "success",type = "json")})
    public String wayBillPageQuery() {
        Pageable pageable = new PageRequest(page - 1,rows);
        Page<WayBill> page = wbs.wayBillPageQuery(pageable);
        pushResultToValueStack(page);
        return SUCCESS;
    }

    //查询运单
    @Action(value = "findWayBillByWayBillNum",results = {@Result(name = "success",type = "json")})
    public String findWayBillByWayBillNum() {
        WayBill wayBill = wbs.findWayBillByWayBillNum(model.getWayBillNum());
        Map<String,Object> map = new HashMap<>();
        if (wayBill == null) {
            map.put("status",0);
        }else {
            map.put("status",1);
            map.put("data",wayBill);
        }
        ServletActionContext.getContext().getValueStack().push(map);
        return SUCCESS;
    }
}
