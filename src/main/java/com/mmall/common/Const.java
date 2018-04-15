 /*
  * Copyright (C), 2015-2018
  * FileName: Const
  * Author:   Administrator
  * Date:     2018/3/8 0008 21:05
  * Description:
  */

 package com.mmall.common;

 import com.google.common.collect.Sets;

 import java.util.Set;

 /**
  * 〈一句话功能简述〉<br>
  * 〈〉
  *
  * @author Administrator
  * @create 2018/3/8 0008
  * @since 1.0.0
  */
 public class Const {
     public static final String CURRENT_USER = "currentUser";
     public static final String TOKEN_PREFIX = "token_";
     public static final String EMAIL = "email";
     public static final String USERNAME = "username";


     public interface  ProductListOrderBy{
         Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc","price_asc");
     }

     public interface Cart{
         // 选中状态
         int CHECKED = 1;
         // 未选中状态
         int UNCHECKED = 0;
         String LIMIT_MUM_FAIL = "LIMIT_MUM_FAIL";
         String LIMIT_MUM_SUCCESS = "LIMIT_MUM_SUCCESS";
     }

     public interface Role
     {
         // 普通用户
        int ROLE_CUSTOMER = 0;
         // admin

         int ROLE_ADMIN = 1;
     }

     public enum ProductStatusElm{
         ON_SALE(1,"在线");
         private String value;
         private int code;

         ProductStatusElm(int code, String value) {
             this.code = code;
             this.value = value;
         }

         public String getValue() {
             return value;
         }

         public void setValue(String value) {
             this.value = value;
         }

         public int getCode() {
             return code;
         }

         public void setCode(int code) {
             this.code = code;
         }
     }
 }
