package com.baidu.web.action;

import com.baidu.domain.Customer;
import com.baidu.domain.FixedArea;
import com.baidu.service.base.FixedAreaService;
import com.baidu.web.action.base.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import org.apache.commons.lang.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
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

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Collection;


/**
 * Created by ZhangPei on 2017/7/28.
 */
@ParentPackage("json-default")
@Namespace("/")
@Scope("prototype")
@Controller
public class FixedAreaAction extends BaseAction<FixedArea> {

    @Autowired
    private FixedAreaService fas;

    //新增定区操作
    @Action(value = "addFixedArea", results = {@Result(name = "success", type = "redirect", location = "./pages/base/fixed_area.html")})
    public String addFixedArea() {
        fas.addFixedArea(model);
        return SUCCESS;
    }

    //定区分页显示
    @Action(value = "fixedAreaPageQuery", results = {@Result(name = "success", type = "json")})
    public String fixedAreaPageQuery() {
        Pageable pageable = new PageRequest(page - 1, rows);
        Specification specification = new Specification() {
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                ArrayList<Predicate> list = new ArrayList();
                if (StringUtils.isNotBlank(model.getId())) {
                    Predicate predicate1 = criteriaBuilder.like(root.get("id").as(String.class), "%" + model.getId() + "%");
                    list.add(predicate1);
                }
                if (StringUtils.isNotBlank(model.getCompany())) {
                    Predicate predicate2 = criteriaBuilder.like(root.get("company").as(String.class), "%" + model.getCompany() + "%");
                    list.add(predicate2);
                }
                return criteriaBuilder.and(list.toArray(new Predicate[0]));
            }
        };
        Page<FixedArea> page = fas.fixedAreaPageQuery(specification, pageable);
        pushResultToValueStack(page);
        return SUCCESS;
    }

    //查询未关联客户
    private String fixedAreaId;

    public void setFixedAreaId(String fixedAreaId) {
        this.fixedAreaId = fixedAreaId;
    }

    @Action(value = "findNoAssociationCustomer", results = {@Result(name = "success", type = "json")})
    public String findNoAssociationCustomer() {
        //因为有业务层在服务器上发布，所以此处应该是客户端。
        Collection<? extends Customer> collection = WebClient.create("http://localhost:9008/crm_management/services/customerService/findNoAssociationCustomers").accept(MediaType.APPLICATION_JSON).getCollection(Customer.class);
        ActionContext.getContext().getValueStack().push(collection);
        return SUCCESS;
    }

    //查询已经关联的客户
    @Action(value = "findAssociationCustomers", results = {@Result(name = "success", type = "json")})
    public String findAssociationCustomesr() {
        String url = "http://localhost:9008/crm_management/services/customerService/findAssociationCustomers/" + fixedAreaId;
        Collection<? extends Customer> collection = WebClient.create(url).accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).getCollection(Customer.class);
        ActionContext.getContext().getValueStack().push(collection);
        return SUCCESS;
    }

    //关联客户到定区
    private String associationCustomers;
    private String noAssociationCustomers;

    public String getAssociationCustomers() {
        return associationCustomers;
    }

    public void setAssociationCustomers(String associationCustomers) {
        this.associationCustomers = associationCustomers;
    }

    public String getNoAssociationCustomers() {
        return noAssociationCustomers;
    }

    public void setNoAssociationCustomers(String noAssociationCustomers) {
        this.noAssociationCustomers = noAssociationCustomers;
    }

    //关联客户
    @Action(value = "associationCustomers")
    public String associationCustomers() {
        String url = "http://localhost:9008/crm_management/services/customerService/associationFixedAreaToCustomer?noAssociationCustomers=" + noAssociationCustomers + "&associationCustomers=" + associationCustomers + "&fixedAreaId=" + fixedAreaId;
        WebClient.create(url).put(null);
        return NONE;
    }

    //关联快递员
    private Integer courierId;
    private Integer takeTimeId;

    public void setCourierId(Integer courierId) {
        this.courierId = courierId;
    }

    public void setTakeTimeId(Integer takeTimeId) {
        this.takeTimeId = takeTimeId;
    }

    //关联快递员到定区
    @Action(value = "associationCourier",results = {@Result(name = "success",type = "redirect",location = "./pages/base/fixed_area.html")})
    public String associationCourier() {
        //这块牵扯关联两张表，首先是定区关联到快递员，然后快递员关联到取派时间
        fas.associationCourier(courierId, takeTimeId,fixedAreaId);
        return SUCCESS;
    }

}
