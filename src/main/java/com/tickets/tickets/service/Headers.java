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

    public static Map passengersInitHeader(){
        Map map = new HashMap();
        map.put("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        map.put("Accept-Encoding","gzip, deflate, br");
        map.put("Accept-Language","zh-CN,zh;q=0.9");
        map.put("Cache-Control","max-age=0");
        map.put("Connection","keep-alive");
        map.put("Content-Length","10");
        map.put("Content-Type","application/x-www-form-urlencoded");
        map.put("Host","kyfw.12306.cn");
        map.put("Origin","https://kyfw.12306.cn");
        map.put("Referer","https://kyfw.12306.cn/otn/index/initMy12306");
        map.put("Upgrade-Insecure-Requests","1");
        map.put("User-Agent","Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.81 Mobile Safari/537.36");

        return map;
    }


}
