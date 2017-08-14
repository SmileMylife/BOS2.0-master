package com.baidu.web.action.base;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.springframework.data.domain.Page;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ZhangPei on 2017/7/28.
 */
public class BaseAction<T> extends ActionSupport implements ModelDriven<T> {

    //用于拿到模型对象的class对象
    public Class<T> clazz;

    //模型驱动
    public T model;        //这块需要实例化model，但是不能通过直接new的方式。

    public T getModel() {
        return model;
    }

    //在初始化baseaction时进行创建model对象。
    public BaseAction() {
        Type superType = this.getClass().getGenericSuperclass();
        //将泛型类型转化成参数类型。
        ParameterizedType pt = (ParameterizedType) superType;
        clazz = (Class<T>) pt.getActualTypeArguments()[0];
        try {
            model = clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取分页属性
    public Integer page;
    public Integer rows;

    public void setPage(Integer page) {
        this.page = page;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    //按条件分页查询
    public void pushResultToValueStack(Page<T> page) {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("total",page.getTotalElements());
        map.put("rows",page.getContent());
        ActionContext.getContext().getValueStack().push(map);
    }
}
