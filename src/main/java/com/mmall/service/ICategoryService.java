package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;

import java.util.List;

/**
 * categoryService 的接口
 *
 * @author : Administrator
 * @create 2018-04-12 19:20
 */
public interface ICategoryService {

    ServerResponse addCategory(String categoryName, Integer parentId);

    ServerResponse updateCategoryName(Integer categoryId, String categoryName);

    ServerResponse<List<Category>> getChildrenParalleCategory(Integer category);

    ServerResponse selectCategoryAndChildrenById(Integer categoryId);
}
