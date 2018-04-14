package com.mmall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author : Administrator
 * @create 2018-04-14 12:20
 */
public interface IFileService {
    String upload(MultipartFile file, String path);
}