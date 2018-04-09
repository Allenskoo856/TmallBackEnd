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
 @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL) //不要null
/**
 * 保证序列化json对象的时候, 如果为null的对象,key也会消失
 * 高富用的，通用相应对象
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

     /**
      * 创建响应成功信息代码
      * @param <T>
      * @return
      */
     public static <T> ServerResponse<T> createBySuccess() {
         return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
     }

     /**
      * 创建响应成功信息
      * @param msg
      * @param <T>
      * @return
      */
     public static <T> ServerResponse<T> createBySuccessMessage(String msg) {
         return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), msg);
     }

     /**
      * 根据传入的数据不同，创建响应的代码片段的代码
      * @param data
      * @param <T>
      * @return
      */
     public static <T> ServerResponse<T> createBySuccess(T data) {
         return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), data);
     }

     /**
      *创建成功的服务器响应，需要把消息和数据仪器传入
      * @param msg
      * @param data
      * @param <T>
      * @return
      */
     public static <T> ServerResponse<T> createBySuccess(String msg, T data) {
         return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), msg, data);
     }

     /**
      * 传入错误的响应代码和信息
      * @param <T>
      * @return
      */
     public static <T> ServerResponse<T> createByError() {
         return new ServerResponse<T>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getDesc());
     }

     /**
      * 传入错误的响应信息
      * @param errorMassage
      * @param <T>
      * @return
      */
     public static <T> ServerResponse<T> createByErrorMassage(String errorMassage) {
         return new ServerResponse<T>(ResponseCode.ERROR.getCode(), errorMassage);
     }

     /**
      *
      * @param errorCode
      * @param errorMessage
      * @param <T>
      * @return
      */
     public static <T> ServerResponse<T> createByErrorCodeMassage(int errorCode, String errorMessage) {
         return new ServerResponse<T>(errorCode, errorMessage);
     }


 }
