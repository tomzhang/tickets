package com.tickets.tickets.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.thoughtworks.xstream.XStream;
import com.tickets.tickets.domain.TrainInfoVO;
import com.tickets.tickets.service.Headers;
import com.tickets.tickets.service.TicketsService;

import net.dongliu.requests.Parameter;
import net.dongliu.requests.Requests;
import net.dongliu.requests.Session;
import net.sourceforge.htmlunit.corejs.javascript.ObjToIntMap;

public class TicketsServiceImpl2 {
	
	String url ="";
	static Session session = Requests.session();
	XStream xs = new XStream();
	String[] strs = new String[]{"35,35","105,35","175,35","245,35","35,105","105,105","175,105","245,105"};
	Gson gson = new Gson();
	String captcha_path="D:\\work\\gitee\\ticekets\\database\\captcha.jpg";

	public void toLogin() {
		//#==================================================登录====================================================================
		String url_init ="https://kyfw.12306.cn/otn/login/init";
		session.get(url_init).verify(false).headers(Headers.initHeader()).send();

		String url_uamtk="https://kyfw.12306.cn/passport/web/auth/uamtk";
		Map request_data_uamtk =new HashMap();
		request_data_uamtk.put("appid","otn");
		session.post(url_uamtk).verify(false).body(request_data_uamtk).headers(Headers.uamtkHeader()).send();
		//验证码处理'
		boolean booleanCaptcha = getCaptcha();
		while(!booleanCaptcha) {
			booleanCaptcha = getCaptcha();
		}
		//登录
		Map<String, Object> login_map = new HashMap<>();
		login_map.put("username", "");
		login_map.put("password", "");
		login_map.put("appid", "otn");

		url = "https://kyfw.12306.cn/passport/web/login";
		String resp =session.post(url).verify(false).headers(Headers.loginHeader()).body(login_map).timeout(30*1000).send().readToText();
		System.out.println("login输出结果"+resp);
		Map<String,Object> login_Map = gson.fromJson(resp, HashMap.class);
		if( (double)(login_Map.get("result_code")) ==1 )
			return;

		Map<String,Object> uamtkMap = new HashMap();
		uamtkMap.put("appid", "otn");
	    url = "https://kyfw.12306.cn/passport/web/auth/uamtk";
		String resp_uamtk =session.post(url).verify(false).headers(getHeaders()).cookies(getCookMap()).timeout(20*1000).forms(uamtkMap).send().readToText();
		System.out.println("uamtk输出结果 "+resp_uamtk);
		
		
		Map<String,Object> resutUamtkMap = gson.fromJson(resp_uamtk, HashMap.class);
		if( (double)(resutUamtkMap.get("result_code")) ==1 )
			return;

		String newapptk = resutUamtkMap.get("newapptk").toString();
		
		
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
		passengerscookMap.put("_jc_save_fromDate", "2018-09-21");
		passengerscookMap.put("_jc_save_toDate", "2018-09-23");
		passengerscookMap.put("_jc_save_wfdc_flag", "dc");
		url ="https://kyfw.12306.cn/otn/leftTicket/init";
		session.get(url).verify(false).headers(getHeaders()).cookies(passengerscookMap).timeout(20*1000).send().readToText();
		url="https://kyfw.12306.cn/otn/dynamicJs/qgdbwtc";
		session.get(url).verify(false).headers(getHeaders()).cookies(passengerscookMap).timeout(20*1000).send().readToText();
		
		
		Map<String, Object> leftTicketDTOMap = new HashMap<>();
		leftTicketDTOMap.put("leftTicketDTO.train_date", "2018-09-21");
		leftTicketDTOMap.put("leftTicketDTO.from_station", "NFF");
		leftTicketDTOMap.put("leftTicketDTO.to_station", "BJP");
		leftTicketDTOMap.put("purpose_codes", "ADULT");
		url ="https://kyfw.12306.cn/otn/leftTicket/queryZ?leftTicketDTO.train_date=2018-09-21&leftTicketDTO.from_station=NFF&leftTicketDTO.to_station=BJP&purpose_codes=ADULT";
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
			session.get(url).verify(false).send().writeToFile(captcha_path);
			Runtime run = Runtime.getRuntime();
			run.exec("cmd.exe /c "+captcha_path);
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
			String url_captcha_check ="https://kyfw.12306.cn/passport/captcha/captcha-check";
			String resp =session.post(url_captcha_check).verify(false).headers(Headers.captcha_checkHeader()).forms(map).send().readToText();
			System.out.println("输出结果："+resp);
			
			
			Map<String,String> resMap = gson.fromJson(resp, HashMap.class);
			String result_code = resMap.get("result_code");
			if("4".equals(result_code)) {
				flag = true;
			}
		} catch (JsonSyntaxException e) {
			//e.printStackTrace();
			System.out.println(e);
		}finally {
			return flag;
		}

	}
	
	
	
	public Map getHeaders() {
		Map<String, Object> headers = new HashMap<>();
		headers.put("User-Agent","Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.81 Mobile Safari/537.36");
		headers.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		headers.put("X-Requested-With", "xmlHttpRequest");
		headers.put("Referer", "https://kyfw.12306.cn/otn/login/init");
		return headers;
	}
	
	public Map getCookMap(){
		Map<String, Object> cookMap = new HashMap();
		cookMap.put("RAIL_EXPIRATION", "1536772772843");
		cookMap.put("RAIL_DEVICEID", "jo1LogO-O2jIGRAO5TgN3FaxGxZRDuC5jSnIdFox2JFxxnO-GUDtT2GxBIAVUmje91ej1BkmE0W7qBrtiZWsQe-Xo0n8umQG1PJKMUa8JsPvrl08X8-O6pD8zwXbxZTrEKyQ8SEqh6lzC_Xs7_w8gFkqImy9rmVx");
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
		//i.login12306();
		i.toLogin();
		//i.checkCaptcha();
		//i.getCaptcha();
/*		String url ="http://localhost:8080/index";
		Map map = new HashMap();
		map.put("info","test1");
		String result = session.post(url).body(map).send().readToText();
		System.out.println(result);*/
	}


	public TrainInfoVO query() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
