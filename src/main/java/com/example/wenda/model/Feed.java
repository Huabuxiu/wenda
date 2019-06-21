package com.example.wenda.model;

import com.alibaba.fastjson.JSONObject;

import java.util.Date;

/**
 * @program: wenda
 * @description:
 * @author: Huabuxiu
 * @create: 2019-06-21 14:19
 **/
public class Feed {
    private int id;
    private int userId;
    private int type;
    private String data;
    private Date createdDate;
    private JSONObject dataJSON = null;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
        dataJSON = JSONObject.parseObject(data);
    }

    public String get(String key){
        return dataJSON == null ? null : dataJSON.getString(key);
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
