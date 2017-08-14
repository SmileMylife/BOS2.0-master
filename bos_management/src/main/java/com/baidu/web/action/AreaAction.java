package com.baidu.web.action;

import com.baidu.domain.Area;
import com.baidu.service.base.AreaService;
import com.baidu.web.utils.PinYin4jUtils;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by ZhangPei on 2017/7/27.
 */
@Controller
@ParentPackage("json-default")
@Namespace("/")
@Scope("prototype")
public class AreaAction extends ActionSupport implements ModelDriven<Area> {
    //文件上传属性
    private File upload;
    private String uploadContentType;
    private String uploadFileName;

    public void setUpload(File upload) {
        this.upload = upload;
    }

    public void setUploadContentType(String uploadContentType) {
        this.uploadContentType = uploadContentType;
    }

    public void setUploadFileName(String uploadFileName) {
        this.uploadFileName = uploadFileName;
    }

    //区域模型驱动
    private Area area = new Area();

    public Area getModel() {
        return area;
    }

    @Autowired
    private AreaService as;

    //解析上传文件
    @Action(value = "uploadDeal")
    public String uploadDeal() throws IOException {
        ArrayList<Area> list = new ArrayList<Area>();
        //解析excle文档
        XSSFWorkbook xwk = new XSSFWorkbook(new FileInputStream(upload));
        XSSFSheet xs = xwk.getSheetAt(0);
        //解析每行数据
        for (Row row : xs) {
            if (row.getRowNum() != 0) {
                if (!StringUtils.isBlank(row.getCell(0).getStringCellValue())) {
                    Area area = new Area();
                    area.setId(row.getCell(0).getStringCellValue());
                    String province = row.getCell(1).getStringCellValue();
                    area.setProvince(province);
                    String city = row.getCell(2).getStringCellValue();
                    area.setCity(city);
                    String district = row.getCell(3).getStringCellValue();
                    String cityCode = PinYin4jUtils.hanziToPinyin(city.substring(0, city.length() - 1), "").toUpperCase();
                    area.setCitycode(cityCode);
                    String[] strs = PinYin4jUtils.getHeadByString(province + city + district);
                    StringBuilder sb = new StringBuilder();
                    for (String str : strs) {
                        sb.append(str);
                    }
                    String shortCode = sb.toString();
                    area.setShortcode(shortCode);
                    area.setDistrict(district);
                    area.setPostcode(row.getCell(4).getStringCellValue());
                    list.add(area);
                }
            }
        }
        as.saveArea(list);
        return NONE;
    }

    //分页属性
    private Integer page;
    private Integer rows;

    public void setPage(Integer page) {
        this.page = page;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    //区域分页查询
    @Action(value = "areaPageQuery", results = {@Result(name = "success", type = "json")})
    public String areaPageQuery() {
        Pageable pageable = new PageRequest(page - 1, rows);
        //这块类似于hibernamte中的criteria，它可以拼接条件，直接将条件进行拼接并且向后传递。
        Specification<Area> specification = new Specification<Area>() {
            public Predicate toPredicate(Root<Area> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<Predicate>();
                if (StringUtils.isNotBlank(area.getProvince())) {
                    Predicate predicate1 = criteriaBuilder.like(root.get("province").as(String.class), "%" + area.getProvince() + "%");
                    list.add(predicate1);
                }
                if (StringUtils.isNotBlank(area.getCity())) {
                    Predicate predicate2 = criteriaBuilder.like(root.get("city").as(String.class),"%" + area.getCity() + "%");
                    list.add(predicate2);
                }
                if (StringUtils.isNotBlank(area.getDistrict())) {
                    Predicate predicate3 = criteriaBuilder.like(root.get("district").as(String.class),"%" + area.getDistrict() + "%");
                    list.add(predicate3);
                }
                return criteriaBuilder.and(list.toArray(new Predicate[0]));
            }
        };
        Page<Area> page = as.areaPageQuery(specification,pageable);
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("total",page.getTotalElements());
        map.put("rows",page.getContent());
        ActionContext.getContext().getValueStack().push(map);
        return SUCCESS;
    }
}
