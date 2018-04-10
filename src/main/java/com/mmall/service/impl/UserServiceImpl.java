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
 import com.mmall.common.TokenCache;
 import com.mmall.dao.UserMapper;
 import com.mmall.pojo.User;
 import com.mmall.service.IUserService;
 import com.mmall.util.MD5Util;
 import org.apache.commons.lang3.StringUtils;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Service;

 import java.util.UUID;

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
         // 得到结果集的数据
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

     /**
      * 用户注册的实现
      * @param user user
      * @return ServerResponse<String>
      */
     @Override
     public ServerResponse<String> register(User user) {
         ServerResponse validResponse = this.checkValid(user.getUsername(), Const.USERNAME);
         if (!validResponse.isSuccess()) {
             return validResponse;
         }
         validResponse = this.checkValid(user.getEmail(), Const.EMAIL);
         if (!validResponse.isSuccess()) {
             return validResponse;
         }

         // 设定普通的用户
         user.setRole(Const.Role.ROLE_CUSTOMER);
         // MD5 加密
         user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

         // 插入注册
         int resultCount = userMapper.insert(user);

         if (resultCount == 0) {
             return ServerResponse.createByErrorMassage("注册失败");
         }

         return ServerResponse.createByErrorMassage("注册成功");
     }

     /**
      *  校验邮箱以及密码的正确与否
      * @param str  传入的值
      * @param type : email 或者 username 两种
      * @return 返回验证信息
      */
     @Override
     public ServerResponse<String> checkValid(String str, String type) {
         if (StringUtils.isNoneBlank(type)) {
            // 开始校验
             if (Const.USERNAME.equals(type)) {
                 int resultCount = userMapper.checkUsername(str);
                 if (resultCount > 0) {
                     return ServerResponse.createByErrorMassage("用户名已经存在");
                 }
             }
             if (Const.EMAIL.equals(type)) {
                 int resultCount = userMapper.checkEmail(str);
                 if (resultCount > 0) {
                     return ServerResponse.createByErrorMassage("email已经存在");
                 }
             }
         } else {
             return ServerResponse.createByErrorMassage("参数错误");
         }
         return ServerResponse.createBySuccess("校验成功");
     }

     /**
      *   根据用户名返回忘记密码的提示问题
      * @param username 用户名提示
      * @return  成功返回密码提示问题， 失败返回错误提示
      */
     @Override
     public ServerResponse selectQuestion(String username) {
         ServerResponse validResponse = this.checkValid(username, Const.USERNAME);
         if (validResponse.isSuccess()) {
             // 用户不存在
             return ServerResponse.createByErrorMassage("用户不存在");
         }
         String question = userMapper.selectQustionByUserName(username);
         if (StringUtils.isNoneBlank(question)) {
             return ServerResponse.createBySuccess(question);
         }
         return ServerResponse.createByErrorMassage("找回密码的问题是空的");
     }

     /**
      * 传入用户名，问题，和答案
      * @param username
      * @param qusetion
      * @param answer
      * @return 回答正确 返回TOKEN， 错误提示
      */
     @Override
     public ServerResponse<String> checkAnswer(String username, String qusetion, String answer) {
         int resultCount = userMapper.checkAnswer(username, qusetion, answer);
         if (resultCount > 0) {
             // 说明 问题 及问题答案是正确的
             String forgotToken = UUID.randomUUID().toString();
             // forgotToKEN 放到本地的cache之中
             TokenCache.setKey("token_" + username, forgotToken);
             return ServerResponse.createBySuccess(forgotToken);
         }
         return ServerResponse.createByErrorMassage("问题的答案错误");
     }

 }
