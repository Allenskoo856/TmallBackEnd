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

 import javax.servlet.http.HttpSession;
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

         return ServerResponse.createBySuccessMessage("注册成功");
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
             TokenCache.setKey(Const.TOKEN_PREFIX + username, forgotToken);
             return ServerResponse.createBySuccess(forgotToken);
         }
         return ServerResponse.createByErrorMassage("问题的答案错误");
     }

     /**
      * 传入用户名、新密码、以及token来重置密码
      * @param username
      * @param passwordNew
      * @param forgetToken
      * @return 返回成功提示信息，重置密码成功，返回失败则提示修改密码失败
      */
     @Override
     public ServerResponse<String> forgetRestPassword(String username, String passwordNew, String forgetToken) {
         if (StringUtils.isBlank(forgetToken)) {
             return ServerResponse.createByErrorMassage("参数错误，token需要传递");
         }
         ServerResponse validResponse = this.checkValid(username, Const.USERNAME);
         if (validResponse.isSuccess()) {
             return ServerResponse.createByErrorMassage("用户不存在");
         }

         String token = TokenCache.getKey(Const.TOKEN_PREFIX + username);

         if (StringUtils.isBlank(token)) {
             return ServerResponse.createByErrorMassage("token无效或者过期");
         }
         if (StringUtils.equals(forgetToken, token)) {
             String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
             int rowCount = userMapper.updatePasswordByUsername(username, md5Password);
             if (rowCount > 0) {
                 return ServerResponse.createBySuccessMessage("修改密码成功");
             }
         } else {
             return ServerResponse.createByErrorMassage("token错误，请重新获取重置密码的token");
         }
         return ServerResponse.createByErrorMassage("修改密码失败");
     }

     /**
      *  此为 登录用户的重置密码功能
      * @param passwordNew
      * @param passwordOld
      * @param user
      * @return 更新成功返回提示，更新失败返回密码更新失败
      */
     @Override
     public ServerResponse<String> resetPassword(String passwordNew, String passwordOld, User user) {
         // 防止横向越权，要校验一下这个用户的旧密码，一定要制定是这个用户，因为我们会查询一个count（1），如果不指定id，那么结果就是true count  >  0
         int resultCount = userMapper.chackPassword(MD5Util.MD5EncodeUtf8(passwordOld), user.getId());
         if (resultCount == 0) {
             return ServerResponse.createByErrorMassage("旧密码错误");
         }
         user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
         int updateCount = userMapper.updateByPrimaryKeySelective(user);
         if (updateCount > 0) {
             return ServerResponse.createBySuccessMessage("密码更新成功");
         }
         return ServerResponse.createByErrorMassage("密码更新失败");
     }

     /**
      * 登录用户更新个人的信息
      * @param user
      * @return 更新成功，返回提示信息和跟新之后的用户信息，更新失败返回 错误信息
      */
     @Override
     public ServerResponse<User> updateInformation(User user) {
         // username 是不能被更新的
         // email 要进行校验，校验新的Email 是不是已经存在了，并且存在的Email如果相同的化，不能是我们当前的这个用户的
         int resultCount = userMapper.checkEmailByUserId(user.getEmail(), user.getId());
         if (resultCount > 0) {
             return ServerResponse.createByErrorMassage("email已经存在，请更换Email在尝试更新");
         }
         User updateUser = new User();
         updateUser.setId(user.getId());
         updateUser.setEmail(user.getEmail());
         updateUser.setPhone(user.getPhone());
         updateUser.setQuestion(user.getQuestion());
         updateUser.setAnswer(user.getAnswer());

         int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
         if (updateCount > 0) {
             return ServerResponse.createBySuccess("更新个人信息成功", updateUser);
         }
         return ServerResponse.createByErrorMassage("更新个人信息失败");
     }


     /**
      *
      * @param userId
      * @return
      */
     @Override
     public ServerResponse<User> getInformation(Integer userId) {
         User user = userMapper.selectByPrimaryKey(userId);
         if (user == null) {
             return ServerResponse.createByErrorMassage("找不到当前用户");
         }
         user.setPassword(StringUtils.EMPTY);
         return ServerResponse.createBySuccess(user);
     }
 }
