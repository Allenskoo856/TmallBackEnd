package com.mmall.service;

import com.mmall.common.ServerResponse;

import java.io.IOException;

/**
 * @author : Administrator
 * @create 2018-04-17 19:35
 */
public interface IorderService {
    ServerResponse pay(Long orderNo, Integer userId, String path) throws IOException;
}
