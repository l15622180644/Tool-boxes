package com.lzk.toolboxes.config.base;

public enum Status {

    SUCCESS(0, "请求成功"),
    FAIL(1, "请求错误"),
    OPSUCCESS(2, "操作成功"),
    OPFAIL(3, "操作失败"),

    TOKENTIMEOUT(-1, "token已过期"),
    TOKENNOTEXIST(-11, "token不存在"),
    TOKENCHANGED(-12, "用户信息或权限已发生改变，请重新登陆"),
    FORBIDDEN(-13,"您没有权限访问"),

    PARAMEXCEPTION(-2, "参数格式有误"),
    EXCEPTION(-3, "网络异常"),

    LOGINSUCCESS(10, "登录成功"),
    LOGINFAILCAUSEPWD(11, "登录失败，用户名或密码错误"),
    LOGINFAILCAUSECODE(12, "登录失败，验证码错误"),
    LOGINFAILCAUSEPER(13, "登录失败，账号未分配角色或权限，请联系系统管理员"),
    LOGINFAILCAUSEBAN(14, "登录失败，账号已被禁用，请联系系统管理员"),
    LOGINXCXFAIL(15, "登录小程序失败"),
    LOGIN_FAIL_CAUSE_CODE_INVALID(16, "登录失败，验证码失效"),

    UPLOADSUCCESS(90, "上传成功"),
    UPLOADFAIL(91, "上传失败，网络异常"),
    UPLOADFILEEXCEPTION(92, "上传失败，文件类型不符合上传标准"),
    DOWNLOADKEYFAIL(93, "下载失败，验证码异常"),
    DOWNLOADFILENOTEXIST(94, "下载失败，文件不存在"),

    INTERNAL_SERVER_ERROR(500, "服务器内部错误!"),
    METHOD_NOT_ALLOWED(405,"请求方法不正确"),
    NOT_FOUND(404,"请求未找到");


    private int code;
    private String msg;

    private Status(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
