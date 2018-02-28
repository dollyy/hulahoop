package com.yc.hulahoop.common;

/**
 * Created by ts on 18-2-28.
 */
public class ServerResponse<T> {

    private int status;
    private String msg;
    private T data;

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public ServerResponse(int status) {
        this.status = status;
    }

    public ServerResponse(String msg) {
        this.msg = msg;
    }

    public ServerResponse(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public ServerResponse(int status, T data) {
        this.status = status;
        this.data = data;
    }

    public ServerResponse(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public boolean isSuccess() {
        return this.status == Const.ResponseCode.SUCCESS.getCode();
    }

    public static <T> ServerResponse<T> createBySuccess() {
        return new ServerResponse<T>(Const.ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServerResponse<T> createBySuccessData(T data) {
        return new ServerResponse<T>(Const.ResponseCode.SUCCESS.getCode(), data);
    }

    public static <T> ServerResponse<T> createBySuccess(String msg, T data) {
        return new ServerResponse<T>(Const.ResponseCode.SUCCESS.getCode(), msg, data);
    }

    public static <T> ServerResponse<T> createByError(){
        return new ServerResponse<T>(Const.ResponseCode.ERROR.getCode());
    }

    public static <T> ServerResponse<T> createByErrorMessage(String msg){
        return new ServerResponse<T>(msg);
    }

    public static <T> ServerResponse<T> createByErrorCodeMessage(int code, String msg) {
        return new ServerResponse<T>(code, msg);
    }

}