package com.baidu.web.action;

import com.baidu.domain.Courier;
import com.baidu.domain.Standard;
import com.baidu.service.base.CourierService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by ZhangPei on 2017/7/26.
 */
@Controller
@ParentPackage("json-default")
@Namespace("/")
@Scope("prototype")
public class CourierAction extends ActionSupport implements ModelDriven<Courier> {
    //分页条件
    private Integer page;
    private Integer rows;

    public void setPage(Integer page) {
        this.page = page;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    //查询条件
    private String standard;

    public void setStandard(String standard) {
        this.standard = standard;
    }

    @Autowired
    private CourierService cs;
    //模型驱动
    private Courier courier = new Courier();

    public Courier getModel() {
        return courier;
    }

    //添加快递员
    @Action(value = "addCourier", results = {@Result(name = "success", type = "redirect", location = "./pages/base/courier.html")})
    public String addCourier() {
        cs.save(courier);
        return SUCCESS;
    }

    //快递员分页查询
    @Action(value = "courierPageQuery", results = {@Result(name = "success", type = "json")})
    public String courierPageQuery() {
        // 封装Pageable对象
        Pageable pageable = new PageRequest(page - 1, rows);
        // 封装条件查询对象 Specification
        Specification<Courier> specification = new Specification<Courier>() {
            // Root 用于获取属性字段，CriteriaQuery可以用于简单条件查询，CriteriaBuilder 用于构造复杂条件查询
            public Predicate toPredicate(Root<Courier> root,
                                         CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<Predicate>();

                // 简单单表查询
                if (StringUtils.isNotBlank(courier.getCourierNum())) {
                    Predicate p1 = cb.equal(
                            root.get("courierNum").as(String.class),
                            courier.getCourierNum());
                    list.add(p1);
                }
                if (StringUtils.isNotBlank(courier.getCompany())) {
                    Predicate p2 = cb.like(
                            root.get("company").as(String.class),
                            "%" + courier.getCompany() + "%");
                    list.add(p2);
                }
                if (StringUtils.isNotBlank(courier.getType())) {
                    Predicate p3 = cb.equal(root.get("type").as(String.class),
                            courier.getType());
                    list.add(p3);
                }
                // 多表查询
                Join<Courier, Standard> standardJoin = root.join("standard",
                        JoinType.INNER);
                if (courier.getStandard() != null
                        && StringUtils.isNotBlank(courier.getStandard()
                        .getName())) {
                    Predicate p4 = cb.like(
                            standardJoin.get("name").as(String.class), "%"
                                    + courier.getStandard().getName() + "%");
                    list.add(p4);
                }
                return cb.and(list.toArray(new Predicate[0]));  //这块传入的是一个数组，源码中传入的是可变参数，可以用数组替代可变参数，这相当于一个数组的创建方法。
            }
        };

        // 调用业务层 ，返回 Page
        Page<Courier> pageData = cs.courierPageQuery(specification,
                pageable);
        // 将返回page对象 转换datagrid需要格式
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("total", pageData.getTotalElements());
        result.put("rows", pageData.getContent());
        // 将结果对象 压入值栈顶部
        ActionContext.getContext().getValueStack().push(result);

        return SUCCESS;
    }

    //接收需要删除的id
    private String ids;

    public void setIds(String ids) {
        this.ids = ids;
    }

    //删除快递员
    @Action(value = "deleteCourier", results = {@Result(name = "success", type = "redirect", location = "./pages/base/courier.html")})
    public String deleteCourier() {
        String[] arrId = ids.split("-");
        for (int i = 0; i < arrId.length; i++) {
            cs.deleteCourier(Integer.parseInt(arrId[i]));
        }
        return SUCCESS;
    }

    //查询所有快递员
    @Action(value = "findCouriers",results = {@Result(name = "success",type = "json")})
    public String findCouriers() {
        List<Courier> list = cs.findCouriers();
        ActionContext.getContext().getValueStack().push(list);
        return SUCCESS;
    }

}
