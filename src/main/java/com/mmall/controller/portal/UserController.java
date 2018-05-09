 /*
  * Copyright (C), 2015-2018
  * FileName: UserController
  * Author:   Administrator
  * Date:     2018/3/8 0008 16:30
  * Description: 用户管理控制
  */

 package com.mmall.controller.portal;

 import com.mmall.common.Const;
 import com.mmall.common.ResponseCode;
 import com.mmall.common.ServerResponse;
 import com.mmall.pojo.User;
 import com.mmall.service.IUserService;
 import com.mmall.util.CookieUtil;
 import com.mmall.util.JsonUtil;
 import com.mmall.util.RedisPoolUtil;
 import org.apache.commons.lang3.StringUtils;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Controller;
 import org.springframework.web.bind.annotation.RequestMapping;
 import org.springframework.web.bind.annotation.RequestMethod;
 import org.springframework.web.bind.annotation.ResponseBody;

 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpServletResponse;
 import javax.servlet.http.HttpSession;

 /**
  * 〈一句话功能简述〉<br>
  * 〈用户管理控制〉
  *
  * @author Administrator
  * @create 2018/3/8 0008
  * @since 1.0.0
  */
 @Controller
 @RequestMapping("/user/")
 public class UserController {

     @Autowired
     private IUserService iUserService;

     /**
      * 用户登陆
      *
      * @param username username
      * @param password password
      * @param session  session
      * @return ServerResponse<User>
      */
     @RequestMapping(value = "login.do", method = RequestMethod.POST)
     @ResponseBody // 自动将返回的值序列化成json对象
     public ServerResponse<User> login(String username, String password, HttpSession session, HttpServletResponse httpServletResponse) {
         ServerResponse<User> response = iUserService.login(username, password);
         if (response.isSuccess()) {
             CookieUtil.writeLoginToken(httpServletResponse, session.getId());
             // session 放入redis 之中
             RedisPoolUtil.setEXTime(session.getId(), JsonUtil.obj2String(response.getData()), Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
         }
         return response;
     }


     /**
      * 登出操作
      *
      * @param httpServletRequest  httpServletRequest
      * @param httpServletResponse httpServletResponse
      * @return ServerResponse<String>
      */
     @RequestMapping(value = "logout.do", method = RequestMethod.POST)
     @ResponseBody
     public ServerResponse<String> logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
         String loginToken = CookieUtil.readLoginToken(httpServletRequest);
         CookieUtil.delLoginToken(httpServletRequest, httpServletResponse);
         RedisPoolUtil.del(loginToken);
         return ServerResponse.createBySuccess();
     }

     /**
      * 注册接口
      *
      * @param user user
      * @return ServerResponse<String>
      */
     @RequestMapping(value = "register.do", method = RequestMethod.POST)
     @ResponseBody
     public ServerResponse<String> register(User user) {
         return iUserService.register(user);
     }

     /**
      * 校验接口传过来的是否正确验证邮箱、以及帐户名
      *
      * @param str  传入的字符串数值
      * @param type 有两个 username、email
      * @return 返回是否邮箱以及帐户名注册重复
      */
     @RequestMapping(value = "check_valid.do", method = RequestMethod.POST)
     @ResponseBody
     public ServerResponse<String> checkValid(String str, String type) {
         return iUserService.checkValid(str, type);
     }


     /**
      * 获得用户登录的信息
      *
      * @param httpServletRequest HttpServletRequest
      * @return 成功返回用户信息，失败错误信息
      */
     @RequestMapping(value = "get_user_info.do", method = RequestMethod.POST)
     @ResponseBody
     public ServerResponse<User> getUserInfo(HttpServletRequest httpServletRequest) {
         String loginToken = CookieUtil.readLoginToken(httpServletRequest);
         if (StringUtils.isEmpty(loginToken)) {
             return ServerResponse.createByErrorMassage("用户未登录,无法获取当前用户的信息");
         }
         String userJsonStr = RedisPoolUtil.get(loginToken);
         User user = JsonUtil.string2Obj(userJsonStr, User.class);
         if (user != null) {
             return ServerResponse.createBySuccess(user);
         }
         return ServerResponse.createByErrorMassage("用户未登录,无法获取当前用户的信息");
     }


     /**
      * 根据用户名返回忘记密码的提示问题
      *
      * @param username 用户名提示
      * @return 成功返回密码提示问题， 失败返回错误提示
      */
     @RequestMapping(value = "forget_get_question.do", method = RequestMethod.POST)
     @ResponseBody
     public ServerResponse<String> forgetGetQuestion(String username) {
         return iUserService.selectQuestion(username);
     }


     /**
      * 传入用户名，问题，和答案
      *
      * @param username
      * @param question
      * @param answer
      * @return 回答正确 返回TOKEN， 错误提示
      */
     @RequestMapping(value = "forget_check_answer.do", method = RequestMethod.POST)
     @ResponseBody
     public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer) {
         return iUserService.checkAnswer(username, question, answer);
     }


     /**
      * 传入用户名、新密码、以及token来重置密码
      *
      * @param username
      * @param passwordNew
      * @param forgetToken
      * @return 返回成功提示信息，重置密码成功，返回失败则提示修改密码失败
      */
     @RequestMapping(value = "forget_reset_password.do", method = RequestMethod.POST)
     @ResponseBody
     public ServerResponse<String> forgetRestPassword(String username, String passwordNew, String forgetToken) {
         return iUserService.forgetRestPassword(username, passwordNew, forgetToken);
     }

     /**
      * 已登陆的用户，重置密码功能，
      *
      * @param session     登录用户的 session
      * @param passwordOld 原来的旧密码
      * @param passwordNew 新密码
      * @return 返回重置信息的成功与否
      */
     @RequestMapping(value = "reset_password.do", method = RequestMethod.POST)
     @ResponseBody
     public ServerResponse<String> resetPassword(HttpServletRequest httpServletRequest, String passwordOld, String passwordNew) {
         String loginToken = CookieUtil.readLoginToken(httpServletRequest);
         if (StringUtils.isEmpty(loginToken)) {
             return ServerResponse.createByErrorMassage("用户未登录,无法获取当前用户的信息");
         }
         String userJsonStr = RedisPoolUtil.get(loginToken);
         User user = JsonUtil.string2Obj(userJsonStr, User.class);
         if (user == null) {
             return ServerResponse.createByErrorMassage("用户未登录");
         }
         return iUserService.resetPassword(passwordNew, passwordOld, user);
     }

     /**
      * 登录用户更新个人的信息
      *
      * @param session 登录的session
      * @param user    传入的用户
      * @return 更新成功，返回提示信息和跟新之后的用户信息，更新失败返回 错误信息
      */
     @RequestMapping(value = "update_information.do", method = RequestMethod.POST)
     @ResponseBody
     public ServerResponse<User> update_information(HttpServletRequest httpServletRequest, User user) {
         String loginToken = CookieUtil.readLoginToken(httpServletRequest);
         if (StringUtils.isEmpty(loginToken)) {
             return ServerResponse.createByErrorMassage("用户未登录,无法获取当前用户的信息");
         }
         String userJsonStr = RedisPoolUtil.get(loginToken);
         User currentUser = JsonUtil.string2Obj(userJsonStr, User.class);
         if (currentUser == null) {
             return ServerResponse.createByErrorMassage("用户未登录");
         }
         // 防止越权问题时候，利用用户前端传入的id 非用户原始的id
         user.setId(currentUser.getId());
         // 不允许调用接口强行修改用户名
         user.setUsername(currentUser.getUsername());
         ServerResponse<User> response = iUserService.updateInformation(user);
         // 更新用户信息
         if (response.isSuccess()) {
             response.getData().setUsername(currentUser.getUsername());
             RedisPoolUtil.setEXTime(loginToken, JsonUtil.obj2String(response.getData()), Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
         }
         return response;
     }

     /**
      * 获得用户的登录信息，请求方式是session
      *
      * @param session
      * @return 成功返回用户登录的成功信息，失败就显示未登录的提示信息
      */
     @RequestMapping(value = "get_information.do", method = RequestMethod.POST)
     @ResponseBody
     public ServerResponse<User> get_information(HttpServletRequest httpServletRequest) {
         String loginToken = CookieUtil.readLoginToken(httpServletRequest);
         if (StringUtils.isEmpty(loginToken)) {
             return ServerResponse.createByErrorMassage("用户未登录,无法获取当前用户的信息");
         }
         String userJsonStr = RedisPoolUtil.get(loginToken);
         User currentUser = JsonUtil.string2Obj(userJsonStr, User.class);
         if (currentUser == null) {
             return ServerResponse.createByErrorCodeMassage(ResponseCode.NEED_LOGIN.getCode(), "未登录需要强制登录status=10");
         }
         return iUserService.getInformation(currentUser.getId());
     }
 }
