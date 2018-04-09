 /*
  * Copyright (C), 2015-2018
  * FileName: ResponseCode
  * Author:   Administrator
  * Date:     2018/3/8 0008 19:07
  * Description:
  */

 package com.mmall.common;

 /**
  * 〈一句话功能简述〉<br>
  * 〈〉
  *
  * @author Administrator
  * @create 2018/3/8 0008
  * @since 1.0.0
  */
 public enum ResponseCode {
     /**
      * success: 成功
      * error： 失败
      * need_login: 需要登录
      * illegal_argument: 非法的参数
      */
     SUCCESS(0, "success"),
     ERROR(1, "error"),
     NEED_LOGIN(10, "NEED_LOGIN"),
     ILLEGAL_ARGUMENT(2, "ILLEGAL_ARGUMENT");

     private final int code;
     private final String desc;

     public int getCode() {
         return code;
     }

     public String getDesc() {
         return desc;
     }

     ResponseCode(int code, String desc) {
         this.code = code;
         this.desc = desc;
     }
 }
