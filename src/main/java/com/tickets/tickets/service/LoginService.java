package com.tickets.tickets.service;

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
}
