package com.tickets.tickets.content;

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

    public static Map uamauthclientHeader(){
        Map<String, Object> headers = new HashMap<>();
        headers.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko");
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        headers.put("X-Requested-With", "xmlHttpRequest");
        headers.put("Referer", "https://kyfw.12306.cn/otn/login/init");
        return headers;
    }




    public static Map getPassengerDTOsHeader(){
        Map map = new HashMap();
        map.put("Accept","*/*");
        map.put("Accept-Language","zh-Hans-CN,zh-Hans;q=0.8,en-US;q=0.5,en;q=0.3");
        map.put("Accept-Encoding","gzip, deflate");
        map.put("Connection","close");
        map.put("Content-Length","0");
        map.put("Content-Type","application/x-www-form-urlencoded");
        map.put("Host","kyfw.12306.cn");
        map.put("Referer","https://kyfw.12306.cn/otn/index/initMy12306");
        map.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko");
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


    public static Map  queryAHeader(){
        Map map = new HashMap();
        map.put("Accept","*/*");
        map.put("Accept-Language","zh-Hans-CN,zh-Hans;q=0.8,en-US;q=0.5,en;q=0.3");
        map.put("Accept-Encoding","gzip, deflate");
        map.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko");
        map.put("Cache-Control","no-cache");
        map.put("X-Requested-With","XMLHttpRequest");
        map.put("If-Modified-Since","0");
        map.put("Referer","https://kyfw.12306.cn/otn/leftTicket/init");
        map.put("Host","kyfw.12306.cn");
        map.put("Connection","close");
        return map;
    }


}
