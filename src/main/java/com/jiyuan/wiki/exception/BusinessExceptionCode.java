package com.jiyuan.wiki.exception;

public enum BusinessExceptionCode {

    USER_LOGIN_NAME_EXIST("Login name already exists"),
    LOGIN_USER_ERROR("Username does not exist or password is wrong"),
    VOTE_REPEAT("You have already liked");

    private String desc;

    BusinessExceptionCode(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
