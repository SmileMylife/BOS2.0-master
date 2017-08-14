package com.baidu.web.action;

import com.baidu.domain.Standard;
import com.baidu.service.base.StandardService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
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
import java.util.List;
import java.util.Map;


/**
 * Created by ZhangPei on 2017/7/26.
 */
@Controller
@ParentPackage("json-default")
@Namespace("/")
@Scope("prototype")     //注意这里一定要设置成prototype默认是多例的。
public class StandardAction extends ActionSupport implements ModelDriven<Standard> {

    @Autowired
    private StandardService ss;
    //模型驱动
    private Standard standard = new Standard();

    public Standard getModel() {
        return standard;
    }
    //由数据表格传送传送上来的当前页和
    private Integer page;
    private Integer rows;

    public void setPage(Integer page) {
        this.page = page;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    //新增快件标准
    @Action(value = "standard_save",results = {@Result(name = "success",type = "redirect",location = "./pages/base/standard.html")})
    public String standardSave() {
        ss.save(standard);
        return SUCCESS;
    }
    //分页查询操作
    @Action(value = "standard_pageQuery",results = {@Result(name = "success",type = "json")})
    public String standardPageQuery() {
        Pageable pageable = new PageRequest(page - 1,rows);
        Page<Standard> page = ss.standardPageQuery(pageable);
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("total",page.getNumberOfElements());
        map.put("rows",page.getContent());
        ActionContext.getContext().getValueStack().push(map);
        return  SUCCESS;
    }
    //查询所有取派标准
    @Action(value = "findAllStandard",results = {@Result(name = "success",type = "json")})
    public String findAllStandard() {
        List<Standard> list = ss.findAllStandard();
        ActionContext.getContext().getValueStack().push(list);
        return SUCCESS;
    }
}
