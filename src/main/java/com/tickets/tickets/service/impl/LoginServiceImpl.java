package com.tickets.tickets.service.impl;

import com.google.gson.Gson;
import com.tickets.tickets.content.CookiesContent;
import com.tickets.tickets.content.Headers;
import com.tickets.tickets.content.WebContent;
import com.tickets.tickets.service.LoginService;
import com.tickets.tickets.utils.SessionUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {

    static Gson gson = new Gson();
    String captcha_path="D:\\work2\\img\\captcha.jpg";
    String[] strs = new String[]{"35,35","105,35","175,35","245,35","35,105","105,105","175,105","245,105"};

    @Override
    public void toLogin() {

        String url_init ="https://kyfw.12306.cn/otn/login/init";
        SessionUtils.session().get(url_init).verify(false).headers(Headers.initHeader()).timeout(20*1000).send();

        String url_logdevice="https://kyfw.12306.cn/otn/HttpZF/logdevice?algID=QRqR6wPyKZ&hashCode=lkUTDoJPpSm3zCb7GSoeajQQHkpHcXusReeMHhKXtMk&FMQw=0&q4f3=en-US&VySQ=FGH9RLTQe0Il8zvsUhAS0D1MpIRW0RrY&VPIf=1&custID=133&VEek=unknown&dzuS=0&yD16=1&EOQP=a6588b471456c0b2345c44dc367af87d&jp76=223017b546a29cfe7aa7f1efcefc0b88&hAqN=Win32&platform=WEB&ks0Q=ebda3f4fca91cb13d8a03858320fadbb&TeRS=777x1449&tOHY=24xx815x1449&Fvje=i1l1s1&q5aJ=-8&wNLf=99115dfb07133750ba677d055874de87&0aew={0}&E3gR=009220e11f272d503c007d3cdb571082&timestamp={1}";
        url_logdevice=url_logdevice.replace("{0}","Mozilla/5.0%20(Windows%20NT%2010.0;%20WOW64;%20Trident/7.0;%20rv:11.0)%20like%20Gecko");
        url_logdevice=url_logdevice.replace("{1}",String.valueOf(System.currentTimeMillis()));
        String response_logdevice = SessionUtils.session().get(url_logdevice).verify(false).headers(Headers.initHeader()).timeout(30*1000).send().readToText();
        String callbackFunction=response_logdevice.substring(18,response_logdevice.length()-2);
        Map<String,Object> map_callbackFunction =  gson.fromJson(callbackFunction, HashMap.class);
        String exp = map_callbackFunction.get("exp").toString();
        String dfp = map_callbackFunction.get("dfp").toString();
        CookiesContent.RAIL_EXPIRATION =exp;
        CookiesContent.RAIL_DEVICEID = dfp;


        String url_uamtk="https://kyfw.12306.cn/passport/web/auth/uamtk";
        Map request_data_uamtk =new HashMap();
        request_data_uamtk.put("appid","otn");
        SessionUtils.session().post(url_uamtk).verify(false).body(request_data_uamtk).headers(Headers.uamtkHeader()).send();
    }



    /**
     * 获取验证码
     *
     */
    public String getCaptcha(){
        String url ="https://kyfw.12306.cn/passport/captcha/captcha-image?login_site=E&module=login&rand=sjrand&"+ System.currentTimeMillis();
        SessionUtils.session().get(url).verify(false).timeout(30*1000).send().writeToFile(captcha_path);
        return captcha_path;
    }

    @Override
    public boolean checkCaptcha(String captcha) {
        String read = getPostion(captcha);
        Map<String, Object> map = new HashMap<>();
        map.put("answer", read);
        map.put("login_site", "E");
        map.put("rand", "sjrand");
        String url_captcha_check ="https://kyfw.12306.cn/passport/captcha/captcha-check";
        String resp =SessionUtils.session().post(url_captcha_check).verify(false).headers(Headers.captcha_checkHeader()).body(map).timeout(30*1000).send().readToText();
        System.out.println("输出结果："+resp);
        Map<String,String> resMap = gson.fromJson(resp, HashMap.class);
        String result_code = resMap.get("result_code");
        if("4".equals(result_code)) {
           return  true;
        }
        return false;


}

    @Override
    public Map doLogin(String username, String password) {
        Map resultMap = new HashMap();

        //登录
        Map<String, Object> login_map = new HashMap<>();
        login_map.put("username", username);
        login_map.put("password", password);
        login_map.put("appid", "otn");

        String url_login = "https://kyfw.12306.cn/passport/web/login";
        String response_login =SessionUtils.session().post(url_login).verify(false).headers(Headers.loginHeader()).body(login_map).timeout(30*1000).send().readToText();
        System.out.println("login输出结果"+response_login);
        Map<String,Object> login_Map = gson.fromJson(response_login, HashMap.class);
        if( (double)(login_Map.get("result_code")) != 0 ){
            resultMap.put("code","0");
            return  resultMap;
        }

        String url_userLoginRedirect="https://kyfw.12306.cn/otn/passport?redirect=/otn/login/userLogin";
        SessionUtils.session().get(url_userLoginRedirect).verify(false).timeout(30*1000).send().readToText();

        String url_uamtk="https://kyfw.12306.cn/passport/web/auth/uamtk";
        Map request_data_uamtk =new HashMap();
        request_data_uamtk.put("appid","otn");
        String response_uamtk =SessionUtils.session().post(url_uamtk).verify(false).body(request_data_uamtk).headers(Headers.uamtkHeader()).send().readToText();
        System.out.println("uamtk输出结果 "+response_uamtk);


        Map<String,Object> resutUamtkMap = gson.fromJson(response_uamtk, HashMap.class);
        if( (double)(resutUamtkMap.get("result_code")) ==1 ){
            resultMap.put("code","0");
            return  resultMap;
        }

        String newapptk = resutUamtkMap.get("newapptk").toString();
        CookiesContent.NEWAPPTK = newapptk;
        Map<String,Object> uamauthclientMap = new HashMap();
        uamauthclientMap.put("tk", newapptk);
        String url_uamauthclient = "https://kyfw.12306.cn/otn/uamauthclient";
        String resp_uamauthclient = SessionUtils.session().post(url_uamauthclient).verify(false).headers(Headers.uamauthclientHeader()).cookies(CookiesContent.getCookMap()).timeout(20*1000).forms(uamauthclientMap).send().readToText();

        System.out.println("uamauthclient输出结果 "+resp_uamauthclient);
        Map<String,Object> response_uamauthclient_data = gson.fromJson(resp_uamauthclient,HashMap.class);
        if( (double)(response_uamauthclient_data.get("result_code")) != 0 ){
            resultMap.put("code","0");
            return  resultMap;
        }
        String usernames = response_uamauthclient_data.get("username").toString();
        WebContent.usernames = usernames;
        System.out.println("欢迎：【"+usernames+"】登录");
        resultMap.put("code","1");
        resultMap.put("usernames",usernames);
        return  resultMap;
    }

    public String getPostion(String inputs){
        String result ="";
        if(inputs.equals("")|| inputs==null){
            return result;
        }else{
            String[] inputArray =  inputs.split(",");
            for(int i=0;i<inputArray.length;i++){
                result+= strs[Integer.valueOf(inputArray[i])]+",";
            }
            if(result.endsWith(",")){
                result = result.substring(0,result.length()-1);
            }
            return result;
        }
    }

}

