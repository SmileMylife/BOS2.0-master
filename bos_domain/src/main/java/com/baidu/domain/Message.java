package com.baidu.domain;

/**
 * Created by ZhangPei on 2017/8/10.
 */
public class Message {
    private Integer status;
    private String message;

    public Message(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public Message() {

    }
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
