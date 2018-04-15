package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.vo.CartVo;

/**
 * @author : Administrator
 * @create 2018-04-15 14:17
 */
public interface ICartService {

    ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count);

    ServerResponse<CartVo> update(Integer userId, Integer productId, Integer count);


    ServerResponse<CartVo> deleteProduct(Integer userId, String productIds);

    ServerResponse<CartVo> getListProduct(Integer userId);

    ServerResponse<CartVo> selectOrUnSelectAll(Integer userId, Integer prodcutId, Integer checked);
}
