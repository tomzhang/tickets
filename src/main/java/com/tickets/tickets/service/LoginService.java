package com.tickets.tickets.service;

import java.util.Map;

public interface LoginService {
    /**
     * 去登录
     */
    void toLogin();

    /**
     * 获取验证码
     * @return
     */
    String getCaptcha();

    /**
     * 校验验证码
     * @return
     */
    boolean checkCaptcha(String captcha);

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    Map doLogin(String username,String password);
}
