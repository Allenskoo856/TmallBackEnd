package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author : Administrator
 * @create 2018-04-12 17:09
 */
@Controller
@RequestMapping("manage/category")
public class CategoryManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICategoryService iCategoryService;

    /**
     * 管理员登录，增加目录节点
     * @param session
     * @param categoryName
     * @param parentId
     * @return 返回跟新成功，或者报错
     */
    @RequestMapping(value = "add_category.do",  method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse addCategory(HttpSession session, String categoryName, @RequestParam(value = "parentId", defaultValue = "0") int parentId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMassage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登陆");
        }
        // 校验是否为管理员
        if (iUserService.checkAdminRole(user).isSuccess()) {
            // 是管理员
            // 增加我们处理的逻辑
            return iCategoryService.addCategory(categoryName, parentId);
        } else {
            return ServerResponse.createByErrorMassage("无权限操作，需要管理员权限");
        }
    }

    /**
     *  品类的重命名功能
     * @param session
     * @param categoryId
     * @param categoryName
     * @return 添加管理员节点, 成功则开启
     */
    @RequestMapping(value = "set_category_name.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse setCategoryName(HttpSession session, Integer categoryId, String categoryName) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMassage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登陆");
        }
        // 校验是否为管理员
        if (iUserService.checkAdminRole(user).isSuccess()) {
            // 更新categoryName
            return iCategoryService.updateCategoryName(categoryId, categoryName);
        } else {
            return ServerResponse.createByErrorMassage("无权限操作，需要管理员权限");
        }
    }

    /**
     * 查询子节点的category信息，并且不递归，保持平级
     * @param session
     * @param categoryId
     * @return
     */
    @RequestMapping(value = "get_category.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getChildrenParallelCategory(HttpSession session, @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMassage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登陆");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            // 查询子节点的category信息，并且不递归，保持平级
            return iCategoryService.getChildrenParalleCategory(categoryId);
        } else {
            return ServerResponse.createByErrorMassage("无权限操作，需要管理员权限");
        }
    }

    /**
     * 递归查询本节点的id 以及 子节点的id
     * @param session
     * @param categoryId
     * @return
     */
    @RequestMapping(value = "get_deep_category.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getCateoryAndDeepChildrenCategory(HttpSession session, @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMassage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登陆");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            // 查询当前节点的id、 和递归子节点的 id , category信息，并且递归
            return iCategoryService.selectCategoryAndChildrenById(categoryId);
        } else {
            return ServerResponse.createByErrorMassage("无权限操作，需要管理员权限");
        }
    }
}
