package com.mmall.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 结合了产品和购物车的抽象对象
 *
 * @author : Administrator
 * @create 2018-04-15 14:52
 */
@Getter
@Setter
public class CartProductVo {
    // 结合了产品和购物车的一个抽象对象

    private Integer id;
    private Integer userId;
    private Integer productId;
    // 购物车的数量
    private Integer quantity;
    private String productName;
    private String productSubtitle;
    private String prodcutMainImage;
    private BigDecimal productPrice;
    private Integer productStatus;
    private BigDecimal productTotalPrice;
    private Integer productStock;
    // 此商品是否勾选
    private Integer productChecked;
    // 限制数量的一个返回结果
    private String limitQuantity;
}
