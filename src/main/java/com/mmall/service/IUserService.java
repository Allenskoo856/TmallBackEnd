/**
 * Copyright (C), 2015-2018
 * FileName: IUserService
 * Author:   Administrator
 * Date:     2018/3/8 0008 16:57
 * Description:
 */

package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

/**`
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Administrator
 * @create 2018/3/8 0008
 * @since 1.0.0
 */
public interface IUserService {

    ServerResponse<User> login(String username, String password);

    ServerResponse<String> register(User user);

    ServerResponse<String> checkValid(String str, String type);

    ServerResponse selectQuestion(String username);

    ServerResponse<String> checkAnswer(String username, String qusetion, String answer);

    ServerResponse<String> forgetRestPassword(String username, String passwordNew, String forgetToken);

    ServerResponse<String> resetPassword(String passwordNew, String passwordOld, User user);

    ServerResponse<User> updateInformation(User user);
}
