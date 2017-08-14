package com.baidu.web.action;

import com.alibaba.fastjson.JSONObject;
import com.baidu.domain.Promotion;
import com.baidu.service.base.PromotionService;
import com.baidu.web.action.base.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import org.apache.commons.io.FileUtils;
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

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Created by ZhangPei on 2017/8/3.
 */
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class PromotionAction extends BaseAction<Promotion> {
    //接收上传图片的信息
    private File imgFile;
    private String imgFileFileName;
    private String imgFileContentType;

    public void setImgFile(File imgFile) {
        this.imgFile = imgFile;
    }

    public void setImgFileFileName(String imgFileFileName) {
        this.imgFileFileName = imgFileFileName;
    }

    public void setImgFileContentType(String imgFileContentType) {
        this.imgFileContentType = imgFileContentType;
    }

    //上传图片
    @Action(value = "imageUpload", results = {@Result(name = "success", type = "json")})
    public String imageUpload() throws IOException {
        if (imgFile.exists()) {
            String savePath = ServletActionContext.getServletContext().getRealPath("/upload/");
            String fileName = UUID.randomUUID().toString().replace("-", "") + imgFileFileName.substring(imgFileFileName.lastIndexOf("."));
            String saveUrl = ServletActionContext.getServletContext().getContextPath() + "/upload/" + fileName;
            FileUtils.copyFile(imgFile, new File(savePath + "/" + fileName));
            Map<String, Object> map = new HashMap<>();
            map.put("error", 0);
            map.put("url", saveUrl);
            ServletActionContext.getContext().getValueStack().push(map);
            return SUCCESS;
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("error", 1);
            map.put("message", "上传图片不能为空！");
            ServletActionContext.getContext().getValueStack().push(map);
            return NONE;
        }
    }

    //使用文件管理
    @Action(value = "imageManage", results = {@Result(name = "success", type = "json")})
    public String imageManage() {
        // 根目录路径，可以指定绝对路径，比如 d:/xxx/upload/xxx.jpg
        String rootPath = ServletActionContext.getServletContext().getRealPath(
                "/")
                + "upload/";
        // 根目录URL，可以指定绝对路径，比如 http://www.yoursite.com/attached/
        String rootUrl = ServletActionContext.getRequest().getContextPath()
                + "/upload/";

        // 遍历目录取的文件信息
        List<Map<String, Object>> fileList = new ArrayList<Map<String, Object>>();
        // 当前上传目录
        File currentPathFile = new File(rootPath);
        // 图片扩展名
        String[] fileTypes = new String[]{"gif", "jpg", "jpeg", "png", "bmp"};

        if (currentPathFile.listFiles() != null) {
            for (File file : currentPathFile.listFiles()) {
                Map<String, Object> hash = new HashMap<String, Object>();
                String fileName = file.getName();
                if (file.isDirectory()) {
                    hash.put("is_dir", true);
                    hash.put("has_file", (file.listFiles() != null));
                    hash.put("filesize", 0L);
                    hash.put("is_photo", false);
                    hash.put("filetype", "");
                } else if (file.isFile()) {
                    String fileExt = fileName.substring(
                            fileName.lastIndexOf(".") + 1).toLowerCase();
                    hash.put("is_dir", false);
                    hash.put("has_file", false);
                    hash.put("filesize", file.length());
                    hash.put("is_photo", Arrays.<String>asList(fileTypes)
                            .contains(fileExt));
                    hash.put("filetype", fileExt);
                }
                hash.put("filename", fileName);
                hash.put("datetime",
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file
                                .lastModified()));
                fileList.add(hash);
            }
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("moveup_dir_path", "");
        result.put("current_dir_path", rootPath);
        result.put("current_url", rootUrl);
        result.put("total_count", fileList.size());
        result.put("file_list", fileList);
        ActionContext.getContext().getValueStack().push(result);
        return SUCCESS;
    }

    //接收上传的宣传项目并处理
    @Autowired
    private PromotionService ps;

    @Action(value = "savePromotion", results = {@Result(name = "success", type = "redirect", location = "./pages/take_delivery/promotion.html")})
    public String savePromotion() throws IOException {
        String contextPath = ServletActionContext.getServletContext().getContextPath();
        String extension = imgFileFileName.substring(imgFileFileName.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString().replace("-", "") + extension;
        String imgUrl = contextPath + "/upload/" + fileName;
        //保存图片到服务器
        String realPath = ServletActionContext.getServletContext().getRealPath("/");
        String imgPath = realPath + "/upload/" + fileName;
        FileUtils.copyFile(imgFile, new File(imgPath));
        //设置图片路径，用于保存图片
        model.setTitleImg("http://localhost:8080" + imgUrl);
        ps.savePromotion(model);
        return SUCCESS;
    }

    //宣传活动分页
    @Action(value = "promotionPageQuery", results = {@Result(name = "success", type = "json")})
    public String promotionPageQuery() throws IOException {
        Pageable pageable = new PageRequest(page - 1, rows);
        Page<Promotion> page = ps.findPromotion(pageable);
        Map<String, Object> map = new HashMap<>();
        map.put("total", page.getTotalElements());
        map.put("rows", page.getContent());
        ServletActionContext.getContext().getValueStack().push(map);
        return SUCCESS;
    }
}
