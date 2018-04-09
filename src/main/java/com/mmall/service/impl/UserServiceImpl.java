 /*
  * Copyright (C), 2015-2018
  * FileName: UserServiceImpl
  * Author:   Administrator
  * Date:     2018/3/8 0008 17:00
  * Description:
  */

 package com.mmall.service.impl;

 import com.mmall.common.Const;
 import com.mmall.common.ServerResponse;
 import com.mmall.dao.UserMapper;
 import com.mmall.pojo.User;
 import com.mmall.service.IUserService;
 import com.mmall.util.MD5Util;
 import org.apache.commons.lang3.StringUtils;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Service;

 /**
  * 〈一句话功能简述〉<br>
  * 〈〉
  *
  * @author Administrator
  * @create 2018/3/8 0008
  * @since 1.0.0
  */
 @Service("iUserService")
 public class UserServiceImpl implements IUserService {

     @Autowired
     private UserMapper userMapper;

     @Override
     public ServerResponse<User> login(String username, String password) {
         int resultCount = userMapper.checkUsername(username);
         if (resultCount == 0) {
             return ServerResponse.createByErrorMassage("用户名不存在");
         }

         // 密码登陆MD5
         String md5Password = MD5Util.MD5EncodeUtf8(password);
         User user = userMapper.selectLogin(username, md5Password);
         if (user == null) {
             return ServerResponse.createByErrorMassage("密码错误");
         }

         user.setPassword(StringUtils.EMPTY);
         return ServerResponse.createBySuccess("登陆成功", user);
     }

     public ServerResponse<String> register(User user) {
         int resultCount = userMapper.checkUsername(user.getUsername());
         if (resultCount > 0) {
             return ServerResponse.createByErrorMassage("当前用户名已经存在");
         }
         resultCount = userMapper.checkEmail(user.getEmail());
         if (resultCount > 0) {
             return ServerResponse.createByErrorMassage("email已经存在");
         }

         user.setRole(Const.Role.ROLE_CUSTOMER);
         // MD5 加密
         user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

         resultCount = userMapper.insert(user);

         if (resultCount == 0) {
             return ServerResponse.createByErrorMassage("注册失败");
         }

         return ServerResponse.createByErrorMassage("注册成功");

     }

     public ServerResponse<String> checkValid(String str, String type) {
         if (StringUtils.isNoneBlank(type)) {
            // 开始校验
             if (Const.USERNAME.equals(type)) {
                 int resultCount = userMapper.checkUsername(str);
                 if (resultCount == 0) {
                     return ServerResponse.createByErrorMassage("用户名不存在");
                 }
             }
             if (Const.EMAIL.equals(type)) {
                 return null;
             }
         } else {
             return ServerResponse.createByErrorMassage("参数错误");
         }
         return null;
     }
 }
