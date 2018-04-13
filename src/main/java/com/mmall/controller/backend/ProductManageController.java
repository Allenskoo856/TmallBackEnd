package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * 产品模块控制器
 *
 * @author : Administrator
 * @create 2018-04-13 9:40
 */
@Controller
@RequestMapping("/manage/product")
public class ProductManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IProductService iProductService;


    /**
     * 添加或者更改商品详情信息
     * @param session
     * @param product
     * @return
     */
    @RequestMapping("save.do")
    @ResponseBody
    public ServerResponse productSave(HttpSession session, Product product) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMassage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登陆管理员");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            // 填充我们增加产品的接口
          return  iProductService.saveOrUpdateProduct(product);
        } else {
            return ServerResponse.createByErrorMassage("你所在的用户组无权限操作");
        }
    }

    /**
     * 更改产品的状态
     * @param session
     * @param productId
     * @param status
     * @return
     */
    @RequestMapping("set_sale_status.do")
    @ResponseBody
    public ServerResponse setSaleStatus(HttpSession session, Integer productId, Integer status) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMassage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登陆管理员");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            // 填充我们增加产品的接口
            return iProductService.setSaleStatus(productId, status);
        } else {
            return ServerResponse.createByErrorMassage("你所在的用户组无权限操作");
        }
    }

    /**
     * 接口-- 获取商品详情的信息
     * @param session
     * @param productId
     * @return
     */
    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse getDetail(HttpSession session, Integer productId) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMassage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登陆管理员");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            // 填充业务代码
            return iProductService.manageProductDetail(productId);
        } else {
            return ServerResponse.createByErrorMassage("你所在的用户组无权限操作");
        }
    }

    /**
     * 接口-获得产品列表的信息
     * @param session
     * @param pageNum  初始的页码数
     * @param pageSize  默认的前台的页码数字
     * @return
     */
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse getList(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMassage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登陆管理员");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            // 填充我们的业务代码

        } else {
            return ServerResponse.createByErrorMassage("你所在的用户组无权限操作");
        }
    }
}
