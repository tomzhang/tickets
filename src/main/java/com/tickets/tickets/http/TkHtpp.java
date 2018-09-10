package com.tickets.tickets.http;
import net.dongliu.requests.Requests;
import net.dongliu.requests.Session;

import java.util.HashMap;
import java.util.Map;


public class TkHtpp {
    private TkHtpp(){
    }
    private  static Session session = null;

    public static Session requestSession(){
        if(session==null){
            session = Requests.session();
        }
        return session;
    }


    public static Map getHeaders() {
        Map<String, Object> headers = new HashMap<String,Object>();
        headers.put("User-Agent", "	Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Win64; x64; Trident/5.0)");
        headers.put("Host", "kyfw.12306.cn");
        headers.put("X-Requested-With", "xmlHttpRequest");
        headers.put("Referer", "https://kyfw.12306.cn/otn/passport?redirect=/otn/");
        return headers;
    }

    public static Map getCookMap(){
        Map<String, Object> cookMap = new HashMap();
        cookMap.put("RAIL_EXPIRATION", "1536772772843");
        cookMap.put("RAIL_DEVICEID", "jo1LogO-O2jIGRAO5TgN3FaxGxZRDuC5jSnIdFox2JFxxnO-GUDtT2GxBIAVUmje91ej1BkmE0W7qBrtiZWsQe-Xo0n8umQG1PJKMUa8JsPvrl08X8-O6pD8zwXbxZTrEKyQ8SEqh6lzC_Xs7_w8gFkqImy9rmVx");
        return cookMap;
    }

}
