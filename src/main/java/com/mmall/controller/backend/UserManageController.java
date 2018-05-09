package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisPoolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 后台管理员登录的用户管理
 *
 * @author : Administrator
 * @create 2018-04-12 10:01
 */
@Controller
@RequestMapping("/manage/user")
public class UserManageController {


    @Autowired
    private IUserService iUserService;

    /**
     * 管理员后台登录接口
     *
     * @param username
     * @param password
     * @param session
     * @return 成功返回响应，失败提示
     */
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    public ServerResponse<User> login(String username, String password, HttpSession session, HttpServletResponse httpServletResponse) {
        ServerResponse<User> response = iUserService.login(username, password);
        if (response.isSuccess()) {
            User user = response.getData();
            if (user.getRole() == Const.Role.ROLE_ADMIN) {
                // 说明登录的是管理员
                CookieUtil.writeLoginToken(httpServletResponse, session.getId());
                RedisPoolUtil.setEXTime(session.getId(), JsonUtil.obj2String(response.getData()),Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
                return response;
            } else {
                return ServerResponse.createByErrorMassage("不是管理员无法登录");
            }
        }
        return response;
    }
}
