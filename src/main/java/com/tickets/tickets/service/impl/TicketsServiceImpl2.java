package com.tickets.tickets.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.thoughtworks.xstream.XStream;
import com.tickets.tickets.service.TicketsService;

import net.dongliu.requests.Requests;
import net.dongliu.requests.Session;

public class TicketsServiceImpl2 implements TicketsService {
	
	
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
		
		
		//#==================================================登录====================================================================
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
		String resp =session.post(url).verify(false).headers(getHeaders()).cookies(getCookMap()).timeout(20*1000).forms(map).send().readToText();
		
		
		Map<String,Object> uamtkMap = new HashMap();
		uamtkMap.put("appid", "otn");
	    url = "https://kyfw.12306.cn/passport/web/auth/uamtk";
		String resp_uamtk =session.post(url).verify(false).headers(getHeaders()).cookies(getCookMap()).timeout(20*1000).forms(uamtkMap).send().readToText();
		System.out.println("uamtk输出结果 "+resp_uamtk);
		
		
		Map<String,String> resutUamtkMap = gson.fromJson(resp_uamtk, HashMap.class);
		String newapptk = resutUamtkMap.get("newapptk");
		
		
		Map<String,Object> uamauthclientMap = new HashMap();
		uamtkMap.put("tk", newapptk);
		url = "https://kyfw.12306.cn/otn/uamauthclient";
		String resp_uamauthclient = session.post(url).verify(false).headers(getHeaders()).cookies(getCookMap()).timeout(20*1000).forms(uamauthclientMap).send().readToText();
		
		
	//	url = "https://kyfw.12306.cn/otn/login/userLogin";
	//	session.get(url).verify(false).headers(getHeaders()).cookies(getCookMap()).forms(uamauthclientMap).timeout(40*1000).send().readToText();
		
		System.out.println("uamauthclient输出结果 "+resp_uamauthclient);
		
		 url = "https://kyfw.12306.cn/otn/index/initMy12306";
		 String loginSucess = session.get(url).verify(false).headers(getHeaders()).cookies(getCookMap()).forms(uamauthclientMap).timeout(20*1000).send().readToText();
		 
		
		System.out.println(loginSucess);
		 
		//#==================================================获取联系人====================================================================
		Map passengerscookMap = getCookMap();
		passengerscookMap.put("tk", newapptk);
		 
		url ="https://kyfw.12306.cn/otn/passengers/init"; 
		String res_passengers= session.post(url).verify(false).headers(getHeaders()).cookies(passengerscookMap).timeout(40*1000).send().readToText();
		System.out.println(res_passengers);
		String passengers_json = res_passengers.substring(res_passengers.indexOf("[{'passenger_type_name'"), res_passengers.indexOf("'}];")+3);
		System.out.println("常用联系人信息为："+passengers_json);
		
		//#==================================================车票预定====================================================================
		//南阳-北京
		passengerscookMap.put("current_captcha_type", "Z");
		passengerscookMap.put("_jc_save_fromStation", "南阳,NFF");
		passengerscookMap.put("_jc_save_toStation", "北京,BJP");
		passengerscookMap.put("_jc_save_fromDate", "2018-02-21");
		passengerscookMap.put("_jc_save_toDate", "2018-01-23");
		passengerscookMap.put("_jc_save_wfdc_flag", "dc");
		url ="https://kyfw.12306.cn/otn/leftTicket/init";
		session.get(url).verify(false).headers(getHeaders()).cookies(passengerscookMap).timeout(20*1000).send().readToText();
		url="https://kyfw.12306.cn/otn/dynamicJs/qgdbwtc";
		session.get(url).verify(false).headers(getHeaders()).cookies(passengerscookMap).timeout(20*1000).send().readToText();
		
		
		Map<String, Object> leftTicketDTOMap = new HashMap<>();
		leftTicketDTOMap.put("leftTicketDTO.train_date", "2018-02-21");
		leftTicketDTOMap.put("leftTicketDTO.from_station", "NFF");
		leftTicketDTOMap.put("leftTicketDTO.to_station", "BJP");
		leftTicketDTOMap.put("purpose_codes", "ADULT");
		url ="https://kyfw.12306.cn/otn/leftTicket/queryZ?leftTicketDTO.train_date=2018-02-21&leftTicketDTO.from_station=NFF&leftTicketDTO.to_station=BJP&purpose_codes=ADULT";
		String leftTicket_info= session.get(url).verify(false).headers(getHeaders()).cookies(passengerscookMap).timeout(20*1000).forms(leftTicketDTOMap).send().readToText();
		
		System.out.println(leftTicket_info);
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
		cookMap.put("RAIL_EXPIRATION", "1516917960445");
		cookMap.put("RAIL_DEVICEID", "TmT78ekQQpUwFhuGfYzfDiymQ7sGBdpN5Dc-Xt_NyP4zUrLOaCJ1JKsnlbg2CYAa7KcSWz_lyTC_vqUi--9RT3rU0-OWJZqDkXbynCROMvJH7QsEsTNHPsUdI0HsGKUC4RgPVe0rpHo_gFtazAgs5faOE1P-zRgK");
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
		TicketsServiceImpl2 i = new TicketsServiceImpl2();
		i.toLogin();
		//i.checkCaptcha();
		//i.getCaptcha();
	}
	

}
