package com.tickets.tickets.service;

import java.util.HashMap;
import java.util.Map;

public class Headers {

    public static Map initHeader(){
        Map map = new HashMap();
        map.put("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        map.put("Referer","https://kyfw.12306.cn/otn/leftTicket/init");
        return map;
    }

    public static Map uamtkHeader(){
        Map map = new HashMap();
        map.put("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
        map.put("Referer","https://kyfw.12306.cn/otn/passport?redirect=/otn/login/userLogin");
        return map;
    }

    public static Map captcha_checkHeader(){
        Map map = new HashMap();
        map.put("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
        map.put("Referer","https://kyfw.12306.cn/otn/login/init");
        return map;
    }

    public static Map loginHeader(){
        Map map = new HashMap();
        map.put("Content-Type","application/x-www-form-urlencoded; charset=utf-8");
        map.put("X-Requested-With","xmlHttpRequest");
        map.put("Referer","https://kyfw.12306.cn/otn/login/init");
        return map;
    }

}
