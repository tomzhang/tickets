package com.tickets.tickets.service.impl;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class Test {
    public static void main(String[] args) {
        String json ="{\"result_message\":\"登录失败\",\"result_code\":1}";
        Gson gson = new Gson();
        Map<String,Object> map = gson.fromJson(json,HashMap.class);
        System.out.println(map);
        System.out.println( (double)(map.get("result_code")) ==1);
    }
}
