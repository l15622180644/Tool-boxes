package com.lzk.toolboxes.config.base;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.io.Serializable;

public class BaseResult implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Long id;

    private int count = 0;

    private String msg;

    private int code;

    private Object data;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setResult(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public BaseResult setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


    public BaseResult(Status resultStatus) {
        this.code = resultStatus.code();
        this.msg = resultStatus.msg();
    }

    public BaseResult(Status resultStatus, Object data) {
        this.code = resultStatus.code();
        this.msg = resultStatus.msg();
        this.data = data;
    }

    public BaseResult(Status resultStatus, Page page) {
        this.code = resultStatus.code();
        this.msg = resultStatus.msg();
        this.data = page.getRecords();
        this.count = longToInt(page.getTotal());  //总数据量
    }


    public static BaseResult success(int flag) {
        return flag == 1 ? new BaseResult(Status.OPSUCCESS, flag) : new BaseResult(Status.OPFAIL, flag);
    }

    public static BaseResult success(boolean flag){
        return flag == true ? new BaseResult(Status.OPSUCCESS,flag) : new BaseResult(Status.OPFAIL,flag);
    }

    public static BaseResult success(Object data) {
        if (data instanceof Page) {
            Page page = (Page) data;
            return page.getRecords() == null ? new BaseResult(Status.FAIL) : new BaseResult(Status.SUCCESS, page);
        }
        return data == null ? new BaseResult(Status.FAIL) : new BaseResult(Status.SUCCESS, data);
    }

    public static BaseResult success(){
        return new BaseResult(Status.SUCCESS);
    }


    public static BaseResult error(Status status) {
        return new BaseResult(status);
    }

    private int longToInt(long size) {
        return Integer.valueOf(String.valueOf(size));
    }

    public String toJson() {
        return JSON.toJSONString(this);
    }


    @Override
    public String toString() {
        return "BaseResult{" +
                "id=" + id +
                ", count=" + count +
                ", msg='" + msg + '\'' +
                ", code=" + code +
                ", data=" + data +
                '}';
    }
}
