 /*
  * Copyright (C), 2015-2018
  * FileName: ServerResponse
  * Author:   Administrator
  * Date:     2018/3/8 0008 17:01
  * Description:
  */

 package com.mmall.common;

 import org.codehaus.jackson.annotate.JsonIgnore;
 import org.codehaus.jackson.map.annotate.JsonSerialize;

 import java.io.Serializable;

 /**
  * 〈一句话功能简述〉<br>
  * 〈〉
  *
  * @author Administrator
  * @create 2018/3/8 0008
  * @since 1.0.0
  */
 @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
 /*
  *保证序列化json对象的时候, 如果为null的对象,key也会消失
  */
 public class ServerResponse<T> implements Serializable {
     private int status;
     private String msg;
     private T data;

     private ServerResponse(int status) {
         this.status = status;
     }

     private ServerResponse(int status, T data) {
         this.status = status;
         this.data = data;
     }

     private ServerResponse(int status, String msg, T data) {
         this.status = status;
         this.data = data;
         this.msg = msg;
     }

     private ServerResponse(int status, String msg) {
         this.status = status;
         this.msg = msg;
     }

     @JsonIgnore   // 使其不再json序列化之中
     public boolean isSuccess() {
         return this.status == ResponseCode.SUCCESS.getCode();
     }

     public int getStatus() {
         return status;
     }

     public T getData() {
         return data;
     }

     public String getMsg() {
         return msg;
     }

     public static <T> ServerResponse<T> createBySuccess() {
         return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
     }

     public static <T> ServerResponse<T> createBySuccessMessage(String msg) {
         return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), msg);
     }

     public static <T> ServerResponse<T> createBySuccess(T data) {
         return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), data);
     }

     public static <T> ServerResponse<T> createBySuccess(String msg, T data) {
         return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), msg, data);
     }

     public static <T> ServerResponse<T> createByError() {
         return new ServerResponse<T>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getDesc());
     }

     public static <T> ServerResponse<T> createByErrorMassage(String errorMassage) {
         return new ServerResponse<T>(ResponseCode.ERROR.getCode(), errorMassage);
     }

     public static <T> ServerResponse<T> createByErrorCodeMassage(int errorCode, String errorMessage) {
         return new ServerResponse<T>(errorCode, errorMessage);
     }

 }
