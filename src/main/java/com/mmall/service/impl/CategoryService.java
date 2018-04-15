package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author : Administrator
 * @create 2018-04-12 19:22
 */
@Service("iCategoryService")
public class CategoryService implements ICategoryService {

    private Logger logger = LoggerFactory.getLogger(CategoryService.class);

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ServerResponse addCategory(String categoryName, Integer parentId) {
        if (parentId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMassage("添加品类参数错误");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        // 分类可用
        category.setStatus(true);

        int rowCount = categoryMapper.insert(category);
        if (rowCount > 0) {
            return ServerResponse.createBySuccess("添加行数成功");
        }
        return ServerResponse.createByErrorMassage("添加品类失败");
    }

    /**
     * 更新品类名称
     * @param categoryId
     * @param categoryName
     * @return 成功返回成功信息， 失败则返回提示信息
     */
    @Override
    public ServerResponse updateCategoryName(Integer categoryId, String categoryName) {
        if (categoryId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMassage("更新品类的参数错误 ");
        }
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);

        int rowCount = categoryMapper.updateByPrimaryKey(category);
        if (rowCount > 0) {
            return ServerResponse.createBySuccess("更新品类名字成功");
        }
        return ServerResponse.createByErrorMassage("更新品类名称名字失败");
    }

    @Override
    public ServerResponse<List<Category>> getChildrenParalleCategory(Integer category) {
        List<Category> categoryList = categoryMapper.selectCategoryByChildrenByParentId(category);
        if (CollectionUtils.isEmpty(categoryList)) {
            logger.info("未找到当前分类的子分类");
        }
        return ServerResponse.createBySuccess(categoryList);
    }

    /**
     * 递归查询本节点的id 以及 子节点的id
     * @param categoryId
     * @return
     */
    @Override
    public ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId) {
        Set<Category> categorySet = Sets.newHashSet();
        findChildCategory(categorySet, categoryId);

        List<Integer> categoryIdList = Lists.newArrayList();

        if (categoryId != null) {
            for (Category categoryItem : categorySet) {
                categoryIdList.add(categoryItem.getId());
            }
        }
        return ServerResponse.createBySuccess(categoryIdList);
    }

    /**
     * 辅助函数：对目前节点、以及剩下的子节点的递归算法
     * @param categorySet
     * @param categoryId
     * @return 返回递归之后的所有的子节点
     */
    private Set<Category> findChildCategory(Set<Category> categorySet ,Integer categoryId) {
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category != null) {
            categorySet.add(category);
        }
        // 查找子节点，递归算法一定要有个退出的条件
        List<Category> categoryList = categoryMapper.selectCategoryByChildrenByParentId(categoryId);
        for (Category categoriseItem : categoryList) {
            findChildCategory(categorySet, categoriseItem.getId());
        }
        return categorySet;
    }
}
