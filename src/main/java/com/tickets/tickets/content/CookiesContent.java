package com.tickets.tickets.content;

import java.util.HashMap;
import java.util.Map;

public class CookiesContent {
    public static String RAIL_EXPIRATION =null;
    public static String RAIL_DEVICEID = null;

    public static Map getCookMap(){
        Map<String, Object> cookMap = new HashMap();
        cookMap.put("RAIL_EXPIRATION", RAIL_EXPIRATION);
        cookMap.put("RAIL_DEVICEID", RAIL_DEVICEID);
        return cookMap;
    }
}
