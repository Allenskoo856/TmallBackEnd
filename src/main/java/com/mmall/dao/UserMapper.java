package com.mmall.dao;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    /**
     * 校验用户名是否存在
     * @param username 前端传入的用户名
     * @return int
     */
    int checkUsername(String username);

    User selectLogin(@Param("username") String username, @Param("password") String password);

    /**
     * 校验邮箱
     * @param email
     * @return
     */
    int checkEmail(String email);

    /**
     *  得到返回的密码问题
     * @param username
     * @return
     */
    String selectQustionByUserName(String username);

    /**
     *
     * @param username
     * @param question
     * @param answer
     * @return
     */
    int checkAnswer(@Param("username") String username, @Param("question") String question, @Param("answer") String answer);

    int updatePasswordByUsername(@Param("username") String username, @Param("passwordNew") String passwordNew);

    int chackPassword(@Param("password") String password, @Param("userId") Integer userId);

    int checkEmailByUserId(@Param("email") String email, @Param("userId") Integer userId);


}