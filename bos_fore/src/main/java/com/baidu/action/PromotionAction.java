package com.baidu.action;

import com.baidu.domain.PageBean;
import com.baidu.domain.Promotion;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.FileUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ZhangPei on 2017/8/5.
 */
@ParentPackage("json-default")
@Namespace("/")
@Scope("prototype")
@Controller
public class PromotionAction extends ActionSupport implements ModelDriven<Promotion> {
    //模型驱动
    private Promotion promotion = new Promotion();
    private Integer page;
    private Integer rows;

    public void setPage(Integer page) {
        this.page = page;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    @Override
    public Promotion getModel() {
        return promotion;
    }

    //分页查询宣传活动
    @Action(value = "pageQueryOfFront", results = {@Result(name = "success", type = "json")})
    public String pageQueryOfFront() {
        String url = "http://localhost:8080/bos_management/services/promotionService/pageQueryOfFront?page=" + page + "&rows=" + rows;
        PageBean pageBean = WebClient.create(url).accept(MediaType.APPLICATION_JSON).get(PageBean.class);
        Map<String, Object> map = new HashMap<>();
        map.put("total",pageBean.getTotal());
        map.put("rows",pageBean.getRows());
        ServletActionContext.getContext().getValueStack().push(pageBean);
        return SUCCESS;
    }

    @Action(value = "showDetails")
    public String showDetails() throws IOException, TemplateException {
        //判断对象的id是否存在相对应的模板
        String realPath = ServletActionContext.getServletContext().getRealPath("/freeMarker");
        File file = new File(realPath + "/" + promotion.getId() + ".html");
        if (!file.exists()) {
            //文件不存在，则调用freemarker
            Configuration configuration = new Configuration(Configuration.VERSION_2_3_0);
            configuration.setDirectoryForTemplateLoading(new File(ServletActionContext.getServletContext().getRealPath("/WEB-INF/template")));
            Template template = configuration.getTemplate("promotionDetail.ftl");
            //向服务器请求数据
            String url = "http://localhost:8080/bos_management/services/promotionService/findPromotion?id=" + promotion.getId();
            Promotion resultPromotion = WebClient.create(url).accept(MediaType.APPLICATION_JSON).get(Promotion.class);
            Map<String,Object> map = new HashMap<>();
            map.put("promotion",resultPromotion);
            //将两者结合
            template.process(map,new OutputStreamWriter(new FileOutputStream(file),"UTF-8"));
        }
        //最终将结果返回到客户端
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("text/html;charset=utf-8");
        FileUtils.copyFile(file,response.getOutputStream());
        return NONE;
    }
}
