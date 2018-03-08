 /*
  * Copyright (C), 2015-2018
  * FileName: UserServiceImpl
  * Author:   Administrator
  * Date:     2018/3/8 0008 17:00
  * Description:
  */

 package com.mmall.service.impl;

 import com.mmall.common.ServerResponse;
 import com.mmall.dao.UserMapper;
 import com.mmall.pojo.User;
 import com.mmall.service.IUserService;
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

         //todo 密码登陆MD5

         User user = userMapper.selectLogin(username, password);
         if (user == null) {
             return ServerResponse.createByErrorMassage("密码错误");
         }

         user.setPassword(StringUtils.EMPTY);
         return ServerResponse.createBySuccess("登陆成功", user);
     }
 }
