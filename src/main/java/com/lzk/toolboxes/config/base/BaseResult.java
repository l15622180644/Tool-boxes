package com.lzk.toolboxes.config.base;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.io.Serializable;

public class BaseResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private int count = 0;

    private String msg;

    private int code;

    private T data;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public BaseResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BaseResult(Status resultStatus) {
        this.code = resultStatus.getCode();
        this.msg = resultStatus.getMsg();
    }

    public BaseResult(Status resultStatus, T data) {
        this.code = resultStatus.getCode();
        this.msg = resultStatus.getMsg();
        this.data = data;
    }

    public BaseResult(Status resultStatus, T data, Long id) {
        this.code = resultStatus.getCode();
        this.msg = resultStatus.getMsg();
        this.data = data;
        this.id = id;
    }

    public BaseResult(Status resultStatus, Page page) {
        this.code = resultStatus.getCode();
        this.msg = resultStatus.getMsg();
        this.data = (T)page.getRecords();
        this.count = longToInt(page.getTotal());  //总数据量
    }

    public BaseResult(Status resultStatus, String msg){
        this.code = resultStatus.getCode();
        this.msg = msg;
    }

    public static <T> BaseResult<T> returnResult(int flag) {
        return flag == 1 ? new BaseResult(Status.OPSUCCESS, true) : new BaseResult(Status.OPFAIL, false);
    }

    public static <T> BaseResult<T> returnResult(boolean flag){
        return flag == true ? new BaseResult(Status.OPSUCCESS,flag) : new BaseResult(Status.OPFAIL,flag);
    }

    public static <T> BaseResult<T> returnResult(int flag,Long id) {
        return flag == 1 ? new BaseResult(Status.OPSUCCESS, true,id) : new BaseResult(Status.OPFAIL, false);
    }

    public static <T> BaseResult<T> returnResult(boolean flag,Long id){
        return flag == true ? new BaseResult(Status.OPSUCCESS,flag,id) : new BaseResult(Status.OPFAIL,flag);
    }

    public static <T> BaseResult<T> returnResult(T data) {
        if (data instanceof Page) {
            Page page = (Page) data;
            return page.getRecords() == null ? new BaseResult(Status.FAIL) : new BaseResult(Status.SUCCESS, page);
        }
        return data == null ? new BaseResult(Status.FAIL) : new BaseResult(Status.SUCCESS, data);
    }

    public static <T> BaseResult<T> returnResult(Status status,T data) {
        return new BaseResult(status,data);
    }

    public static <T> BaseResult<T> success(){
        return new BaseResult(Status.SUCCESS);
    }

    public static <T> BaseResult<T> success(Status status){
        return new BaseResult(status);
    }

    public static <T> BaseResult<T> success(String msg){
        return new BaseResult(Status.OPSUCCESS,msg);
    }

    public static <T> BaseResult<T> error(Status status) {
        return new BaseResult(status);
    }

    public static <T> BaseResult<T> error(Status status,String msg) {
        return new BaseResult(status,msg);
    }

    public static <T> BaseResult<T> error(String msg) {
        return new BaseResult(Status.OPFAIL,msg);
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
