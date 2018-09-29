package com.tickets.tickets.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.tickets.tickets.content.CookiesContent;
import com.tickets.tickets.content.Headers;
import com.tickets.tickets.domain.PassengerVO;
import com.tickets.tickets.service.TicketPassengerService;
import com.tickets.tickets.utils.SessionUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TicketPassengerServiceImpl implements TicketPassengerService {

    @Override
    public List<PassengerVO> getPassengerDTOs() {

        String url_getPassengerDTOs ="https://kyfw.12306.cn/otn/confirmPassenger/getPassengerDTOs";
        Map cookie_getPassengerDTOs = new HashMap();
        cookie_getPassengerDTOs.put("tk", CookiesContent.NEWAPPTK);
        cookie_getPassengerDTOs.put("RAIL_EXPIRATION",CookiesContent.RAIL_EXPIRATION);
        cookie_getPassengerDTOs.put("RAIL_DEVICEID",CookiesContent.RAIL_DEVICEID);
        String response_getPassengerDTOs= SessionUtils.session().post(url_getPassengerDTOs).verify(false).headers(Headers.getPassengerDTOsHeader()).cookies(cookie_getPassengerDTOs).timeout(100*1000).send().readToText();
        System.out.println("获取联系人返回信息为："+"\n"+response_getPassengerDTOs);
        JsonObject obj = (JsonObject)new Gson().fromJson(response_getPassengerDTOs, JsonObject.class);
        JsonObject json_data =  obj.get("data").getAsJsonObject();
        JsonArray normal_passengers = json_data.getAsJsonArray("normal_passengers");
        Type type = new TypeToken<List<PassengerVO>>(){}.getType();
        List<PassengerVO> list = new Gson().fromJson(normal_passengers,type);
        System.out.println("获取的联系人信息为："+"\n"+list.toArray().toString());

        return list;
    }
}
