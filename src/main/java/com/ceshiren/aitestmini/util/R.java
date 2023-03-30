package com.ceshiren.aitestmini.util;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;

@Getter
@Setter
public class R<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private int code;
    private String message;
    private T data;


    private R(){}
    public R(int code) {
        this.code = code;
    }
    public R(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    public R(ResultCode resultCode, T data) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.data = data;
    }
    public static <T> R<T> ok(){
        return new R<>(ResultCode.SUCCESS, (T)new HashMap<>());
    }

    //public static <T> R<T> error(){
    //    return new R<>(ResultCode.INTERNAL_SERVER_ERROR, (T)new HashMap<>());
    //}
    public static <T> R<T> error(ResultCode resultCode){
        return new R<>(resultCode, (T)new HashMap<>());
    }

    public R<T> status(int code){
        this.setCode(code);
        return this;
    }

    public R<T> message(String message){
        this.setMessage(message);
        return this;
    }
    public R<T> data(T value){
        this.setData(value);
        return this;
    }
}