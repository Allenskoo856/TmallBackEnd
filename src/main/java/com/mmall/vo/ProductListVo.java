package com.mmall.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author : Administrator
 * @create 2018-04-14 10:44
 */
@Getter
@Setter
public class ProductListVo {

    private Integer id;
    private Integer categoryId;

    private String name;
    private String subtitle;
    private String mainImage;
    private BigDecimal price;
    private String imageHost;
    private Integer status;
}
