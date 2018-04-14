package com.mmall.controller.backend;

import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IFileService;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import com.mmall.util.Propertiesutil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

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

    @Autowired
    private IFileService iFileService;


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
     * 接口-获得产品列表的信息--利用（分页逻辑）
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
            return iProductService.getProductList(pageNum, pageSize);
        } else {
            return ServerResponse.createByErrorMassage("你所在的用户组无权限操作");
        }
    }


    /**
     * 接口-- 后台产品搜索接口--的使用
     * @param session
     * @param productId
     * @return
     */
    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse productSearch(HttpSession session, String productName, Integer productId, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMassage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登陆管理员");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            // 填充业务代码
            return iProductService.searchProduct(productName, productId, pageNum, pageSize);
        } else {
            return ServerResponse.createByErrorMassage("你所在的用户组无权限操作");
        }
    }

    /**
     * 上传文件--接口
     * @param file
     * @param request
     * @return
     */
    @RequestMapping("upload.do")
    @ResponseBody
    public ServerResponse upload(HttpSession session ,@RequestParam(value = "upload_file",required = false) MultipartFile file, HttpServletRequest request) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMassage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登陆管理员");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            // 填充我们的业务代码
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = iFileService.upload(file, path);
            String url = Propertiesutil.getProperty("ftp.server.http.prefix") + targetFileName;
            Map fileMap = Maps.newHashMap();
            fileMap.put("uri", targetFileName);
            fileMap.put("url", url);
            return ServerResponse.createBySuccess(fileMap);
        } else {
            return ServerResponse.createByErrorMassage("你所在的用户组无权限操作");
        }
    }


    /**
     * 富文本的上传--接口
     * @param session
     * @param file
     * @param request
     * @return
     */
    @RequestMapping("richtext_img_upload.do")
    @ResponseBody
    public Map richtext_img_upload(HttpSession session , @RequestParam(value = "upload_file",required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        Map resultMap = Maps.newHashMap();
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            resultMap.put("success", false);
            resultMap.put("msg", "请登录管理员");
            return resultMap;
        }
        // 富文本中对于返回值有自己的要求,  我们使用simditor返回
        if (iUserService.checkAdminRole(user).isSuccess()) {
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = iFileService.upload(file, path);
            if (StringUtils.isBlank(targetFileName)) {
                resultMap.put("success", false);
                resultMap.put("msg", "上传失败");
                return resultMap;
            }
            String url = Propertiesutil.getProperty("ftp.server.http.prefix") + targetFileName;

            resultMap.put("success", true);
            resultMap.put("msg", "上传成功");
            resultMap.put("file_path", url);
            response.addHeader("Access-Control-Allow-Headers","X-File-Name");
            return resultMap;
        } else {
            resultMap.put("success", false);
            resultMap.put("msg", "无权限操作");
            return resultMap;
        }
    }
}
