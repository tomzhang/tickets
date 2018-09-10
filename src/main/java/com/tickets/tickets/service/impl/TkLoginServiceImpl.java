package com.tickets.tickets.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.tickets.tickets.http.TkHtpp;
import lombok.extern.slf4j.Slf4j;
import net.dongliu.requests.Session;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@Slf4j
public class TkLoginServiceImpl {

    static Session session = TkHtpp.requestSession();
    static boolean captcha_flag = false;
    static String[] strs = new String[]{"35,35","105,35","175,35","245,35","35,105","105,105","175,105","245,105"};
    static Gson gson = new Gson();
    static String captcha_path="D:\\work\\gitee\\ticekets\\database\\captcha.jpg";
    /**
     * 登录
     * @param username
     * @param userpwd
     * @return
     */
    public static Map login(String username, String userpwd){
        Map rmp = new HashMap();

        String url1 ="https://kyfw.12306.cn/otn/passport?redirect=/otn/";
        String resp1 = session.get(url1).verify(false).headers(TkHtpp.getHeaders()).timeout(20*1000).send().readToText();

        String url_init ="https://kyfw.12306.cn/otn/login/init";
        session.get(url_init).verify(false).headers(TkHtpp.getHeaders()).send().readToText();
        log.info("请求结束："+url_init);

        String url_uamtk="https://kyfw.12306.cn/passport/web/auth/uamtk";
        Map request_uamtk_data = new HashMap();
        request_uamtk_data.put("appid","otn");
        String response_uamtk = session.post(url_uamtk).verify(false).headers(TkHtpp.getHeaders()).params(request_uamtk_data).send().readToText();
        log.info("请求结束："+url_uamtk);

        //获取验证码
        String url_captcha="https://kyfw.12306.cn/passport/captcha/captcha-image?login_site=E&module=login&rand=sjrand&"+ Math.random();
        while(!captcha_flag){
            //下载验证码
            downCaptcha(url_captcha);
            //验证验证码
            checkCaptcha();
        }
        log.info("验证码校验正确");

        String login_url ="https://kyfw.12306.cn/passport/web/login";
        Map request_login_data = new HashMap();
        request_login_data.put("username",username);
        request_login_data.put("password",userpwd);
        request_login_data.put("appid", "otn");
        String response_login =session.post(login_url).verify(false).headers(TkHtpp.getHeaders()).cookies(TkHtpp.getCookMap()).params(request_login_data).send().readToText();
        log.info("login返回信息："+response_login);




        return rmp;
    }

    /**
     * 下载验证码 并打开
     * @param url
     */
    private static void downCaptcha(String url){
        session.get(url).verify(false).send().writeToFile(captcha_path);
        Runtime run = Runtime.getRuntime();
        try {
            run.exec("cmd.exe /c "+captcha_path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    };


    /**
     * 发送验证码
     */
    public static boolean checkCaptcha() {
        boolean flag = false;
        try {
            System.out.println("#=======================================================================  ");
            System.out.println("#根据打开的图片识别验证码后手动输入，输入正确验证码对应的位置，例如：2,5  ");
            System.out.println("# ---------------------------------------   ");
            System.out.println("#         |         |         |  ");
            System.out.println("#    0    |    1    |    2    |     3  ");
            System.out.println("#         |         |         |  ");
            System.out.println("# ---------------------------------------  ");
            System.out.println("#         |         |         |  ");
            System.out.println("#    4    |    5    |    6    |     7  ");
            System.out.println("#         |         |         |  ");
            System.out.println("# ---------------------------------------  ");
            System.out.println(" #=======================================================================  ");
            Scanner scan = new Scanner(System.in);
            String read = scan.nextLine();
            read = getPostion(read);
            System.out.println("输入的验证码："+read);
            Map<String, Object> map = new HashMap<>();
            map.put("answer", read);
            map.put("login_site", "E");
            map.put("rand", "sjrand");
            String url ="https://kyfw.12306.cn/passport/captcha/captcha-check";
            String resp =session.post(url).verify(false).forms(map).send().readToText();

            log.info("校验验证码返回信息："+resp);
            Map<String,String> resMap = gson.fromJson(resp, HashMap.class);
            String result_code = resMap.get("result_code");
            if("4".equals(result_code)) {
                captcha_flag = true;
            }
        } catch (JsonSyntaxException e) {
            System.out.println(e);
        }finally {
            return captcha_flag;
        }

    }

    public static String getPostion(String inputs){
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

    public static void main(String[] args) {

    }

}
