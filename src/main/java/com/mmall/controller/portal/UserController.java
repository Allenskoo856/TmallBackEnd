 /*
  * Copyright (C), 2015-2018
  * FileName: UserController
  * Author:   Administrator
  * Date:     2018/3/8 0008 16:30
  * Description: 用户管理控制
  */

 package com.mmall.controller.portal;

 import com.mmall.common.Const;
 import com.mmall.common.ServerResponse;
 import com.mmall.common.TokenCache;
 import com.mmall.pojo.User;
 import com.mmall.service.IUserService;
 import org.apache.commons.lang3.StringUtils;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Controller;
 import org.springframework.web.bind.annotation.RequestMapping;
 import org.springframework.web.bind.annotation.RequestMethod;
 import org.springframework.web.bind.annotation.ResponseBody;

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
      * @param username  username
      * @param password password
      * @param session  session
      * @return ServerResponse<User>
      */
     @RequestMapping(value = "login.do", method = RequestMethod.POST)
     @ResponseBody // 自动将返回的值序列化成json对象
     public ServerResponse<User> login(String username, String password, HttpSession session) {
        // service-mybatis-dao
         ServerResponse<User> response = iUserService.login(username, password);
         if (response.isSuccess()) {
             session.setAttribute(Const.CURRENT_USER, response.getData());
         }
         return response;
     }


     /**
      *  登出操作
      * @param session session
      * @return ServerResponse<String>
      */
     @RequestMapping(value = "logout.do",method = RequestMethod.GET)
     @ResponseBody
     public ServerResponse<String> logout(HttpSession session){
         session.removeAttribute(Const.CURRENT_USER);
         return ServerResponse.createBySuccess();
     }

     /**
      * 注册接口
      * @param user user
      * @return ServerResponse<String>
      */
     @RequestMapping(value = "register.do",method = RequestMethod.GET)
     @ResponseBody
     public ServerResponse<String> register(User user) {
         return iUserService.register(user);
     }

     /**
      * 校验接口传过来的是否正确验证邮箱、以及帐户名
      * @param str 传入的字符串数值
      * @param type 有两个 username、email
      * @return 返回是否邮箱以及帐户名注册重复
      */
     @RequestMapping(value = "checkValid.do", method = RequestMethod.GET)
     @ResponseBody
     public ServerResponse<String> checkValid(String str, String type) {
         return iUserService.checkValid(str, type);
     }


     /**
      * 获得用户登录的信息
      * @param session
      * @return 成功返回用户信息，失败错误信息
      */
     @RequestMapping(value = "get_user_info.do", method = RequestMethod.GET)
     @ResponseBody
     public ServerResponse<User> getUserInfo(HttpSession session) {
         User user = (User) session.getAttribute(Const.CURRENT_USER);
         if (user != null) {
             return ServerResponse.createBySuccess(user);
         }
         return ServerResponse.createByErrorMassage("用户未登录，无法获得当前用户信息");
     }

     /**
      *   根据用户名返回忘记密码的提示问题
      * @param username 用户名提示
      * @return  成功返回密码提示问题， 失败返回错误提示
      */
     @RequestMapping(value = "forget_get_question.do", method = RequestMethod.GET)
     @ResponseBody
     public ServerResponse<String> forgetGetQuestion(String username) {
         return iUserService.selectQuestion(username);
     }

     /**
      * 传入用户名，问题，和答案
      * @param username
      * @param question
      * @param answer
      * @return 回答正确 返回TOKEN， 错误提示
      */
     @RequestMapping(value = "forget_check_answer.do", method = RequestMethod.GET)
     @ResponseBody
     public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer) {
         return iUserService.checkAnswer(username, question, answer);
     }

     public ServerResponse<String> forgetRestPassword(String username, String passwordNew, String forgetToken) {
         if (StringUtils.isNoneBlank(forgetToken)) {
             return ServerResponse.createByErrorMassage("参数错误，token需要传递");
         }
         ServerResponse validResponse = this.checkValid(username, Const.USERNAME);
         if (validResponse.isSuccess()) {
             return ServerResponse.createByErrorMassage("用户不存在");
         }

         String token = TokenCache.getKey(Const.TOKEN_PREFIX + username);
     }
 }
