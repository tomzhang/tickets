package com.tickets.tickets.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.thoughtworks.xstream.XStream;
import com.tickets.tickets.service.TicketsService;

import net.dongliu.requests.RawResponse;
import net.dongliu.requests.Requests;
import net.dongliu.requests.Session;

public class TicketsServiceImpl implements TicketsService {
	
	
	Session session = Requests.session();
	XStream xs = new XStream();
	String[] strs = new String[]{"35,35","105,35","175,35","245,35","35,105","105,105","175,105","245,105"};
	Gson gson = new Gson();
	
	
	@Override
	public void login12306() {
		String url1 ="https://kyfw.12306.cn/otn/passport?redirect=/otn/";
		String resp1 = session.get(url1).verify(false).headers(getHeaders()).timeout(20*1000).send().readToText();
		
		//System.out.println(resp1);
		System.out.println(xs.toXML(session));
	}
	
	
	public void toLogin() {
			
		String url ="https://kyfw.12306.cn/otn/login/init";
		session.get(url).verify(false).headers(getHeaders()).cookies(getCookMap()).send();

		//验证码处理'
		boolean booleanCaptcha = getCaptcha();
		
		while(!booleanCaptcha) {
			booleanCaptcha = getCaptcha();
		}
		//登录
		Map<String, Object> map = new HashMap<>();
		map.put("username", "l18622091327");
		map.put("password", "li1861327");
		map.put("appid", "otn");
		
		
		url = "https://kyfw.12306.cn/passport/web/login";
		String resp =session.post(url).verify(false).headers(getHeaders()).cookies(getCookMap()).forms(map).send().readToText();
		
		
		Map<String,Object> uamtkMap = new HashMap();
		uamtkMap.put("appid", "otn");
	    url = "https://kyfw.12306.cn/passport/web/auth/uamtk";
		String resp_uamtk =session.post(url).verify(false).headers(getHeaders()).cookies(getCookMap()).forms(uamtkMap).send().readToText();
		System.out.println(resp_uamtk);
		Map<String,String> resutUamtkMap = gson.fromJson(resp_uamtk, HashMap.class);
		String newapptk = resutUamtkMap.get("newapptk");
		
		
		Map<String,Object> uamauthclientMap = new HashMap();
		uamtkMap.put("tk", newapptk);
		url = "https://kyfw.12306.cn/otn/uamauthclient";
		String resp_uamauthclient = session.post(url).verify(false).headers(getHeaders()).cookies(getCookMap()).forms(uamauthclientMap).send().readToText();
		
		System.out.println(resp_uamauthclient);
		
		 url = "https://kyfw.12306.cn/otn/index/initMy12306";
		 String sucess = session.get(url).verify(false).headers(getHeaders()).cookies(getCookMap()).send().readToText();
		 
		
		System.out.println(sucess);
	}
	
	
	/**
	 * 获取验证码
	 * @throws Exception 
	 */
	public boolean getCaptcha(){
		boolean flag = false;
		try {
			String url ="https://kyfw.12306.cn/passport/captcha/captcha-image?login_site=E&module=login&rand=sjrand&0.7581446374982701";
			session.get(url).verify(false).headers(getHeaders()).send().writeToFile("D:/img/c1.jpg");
			Runtime run = Runtime.getRuntime();
			run.exec("cmd.exe /c D:/img/c1.jpg");
			if(checkCaptcha()) {
				flag = true;
			}
		} catch (IOException e) {
			//e.printStackTrace();
			System.out.println(e);
		}finally{
			return flag;
		}		
		
	}
	
	
	/**
	 * 发送验证码
	 */
	public boolean checkCaptcha() {
		boolean flag = false;
		try {
			System.out.println("请输入验证码：");
			Scanner scan = new Scanner(System.in);
			String read = scan.nextLine();
			read = getPostion(read);
			System.out.println("输入的验证码："+read); 
			Map<String, Object> map = new HashMap<>();
			map.put("answer", read);
			map.put("login_site", "E");
			map.put("rand", "sjrand");
			String url ="https://kyfw.12306.cn/passport/captcha/captcha-check";
			//Response<String> resp =session.get(url).verify(false).headers(getHeaders()).params(map).send().toTextResponse();
			String resp =session.post(url).verify(false).headers(getHeaders()).forms(map).send().readToText();		
			System.out.println("输出结果："+resp);
			
			
			Map<String,String> resMap = gson.fromJson(resp, HashMap.class);
			String result_code = resMap.get("result_code");
			if("4".equals(result_code)) {
				flag = true;
			}
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println(e);
		}finally {
			return flag;
		}

	}
	
	
	
	public Map getHeaders() {
		Map<String, Object> headers = new HashMap<>();
		headers.put("User-Agent", "	Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Win64; x64; Trident/5.0)");
		headers.put("Host", "kyfw.12306.cn");
		headers.put("Referer", "https://kyfw.12306.cn/otn/passport?redirect=/otn/");
		return headers;
	}
	
	public Map getCookMap(){
		Map<String, Object> cookMap = new HashMap();
		cookMap.put("RAIL_EXPIRATION", "1516653250781");
		cookMap.put("RAIL_DEVICEID", "aOlcdnbOkZk1M_t90VHz_UahJb4z_bhqp5vZSBI2LRJMDvz6-de4hGiGIzm4ftGiE-n4EIdv9bd9V1Tt0n9e7gtaKshFCV4-wVnaWIwhGUSnpbDCrX_sye9sNCyopn70I-TTxKJc-u9DWM58zaJB5PWN0gW_ng5Q");
		return cookMap;
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
	
	
	public static void main(String[]args) {
		TicketsServiceImpl i = new TicketsServiceImpl();
		i.toLogin();
		//i.checkCaptcha();
		//i.getCaptcha();
	}
	

}
