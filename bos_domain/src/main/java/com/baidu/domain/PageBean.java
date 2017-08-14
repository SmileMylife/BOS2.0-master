package com.baidu.domain;

import org.apache.poi.ss.formula.functions.T;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.util.List;

/**
 * Created by ZhangPei on 2017/8/6.
 */
@XmlRootElement
@XmlSeeAlso({Promotion.class})
public class PageBean<T> {
    private Integer total;
    private List<T> rows;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
