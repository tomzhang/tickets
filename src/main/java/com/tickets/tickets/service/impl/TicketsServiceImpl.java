package com.tickets.tickets.service.impl;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.thoughtworks.xstream.XStream;
import com.tickets.tickets.domain.TrainInfoVO;
import com.tickets.tickets.domain.TrainLineInfoVO;
import com.tickets.tickets.domain.TrainStationInfoVO;
import com.tickets.tickets.service.TicketsService;
import com.tickets.tickets.utils.StationDataUtils;

import net.dongliu.requests.Requests;
import net.dongliu.requests.Session;

public class TicketsServiceImpl implements TicketsService {
	
	
	Session session = Requests.session();
	static XStream xs = new XStream();
	String[] strs = new String[]{"35,35","105,35","175,35","245,35","35,105","105,105","175,105","245,105"};
	private static Gson gson = new Gson();
	
	
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
		String resp =session.post(url).verify(false).headers(getHeaders()).cookies(getCookMap()).timeout(20*1000).forms(map).send().readToText();
		
		
		Map<String,Object> uamtkMap = new HashMap();
		uamtkMap.put("appid", "otn");
	    url = "https://kyfw.12306.cn/passport/web/auth/uamtk";
		String resp_uamtk =session.post(url).verify(false).headers(getHeaders()).cookies(getCookMap()).timeout(20*1000).forms(uamtkMap).send().readToText();
		System.out.println("uamtk输出结果 "+resp_uamtk);
		
		
		Map<String,String> resutUamtkMap = gson.fromJson(resp_uamtk, HashMap.class);
		String newapptk = resutUamtkMap.get("newapptk");
		
		
		Map<String,Object> uamauthclientMap = new HashMap();
		uamauthclientMap.put("tk", newapptk);
		url = "https://kyfw.12306.cn/otn/uamauthclient";
		String resp_uamauthclient = session.post(url).verify(false).headers(getHeaders()).cookies(getCookMap()).timeout(20*1000).forms(uamauthclientMap).send().readToText();
		
		//System.out.println("uamauthclient输出结果 "+resp_uamauthclient);
		
		 url = "https://kyfw.12306.cn/otn/index/initMy12306";
		 String loginSucess = session.get(url).verify(false).headers(getHeaders()).cookies(getCookMap()).timeout(20*1000).send().readToText();
		 
		
		//System.out.println(sucess);
		 
		 //#==================================================获取联系人====================================================================
		url ="https://kyfw.12306.cn/otn/passengers/init"; 
		Map passengerscookMap = getCookMap();
		passengerscookMap.put("tk", newapptk);
		String res_passengers= session.post(url).verify(false).headers(getHeaders()).cookies(passengerscookMap).timeout(40*1000).forms(uamauthclientMap).send().readToText();
		String passengers_json = res_passengers.substring(res_passengers.indexOf("[{'passenger_type_name'"), res_passengers.indexOf("'}];")+3);
		System.out.println("常用联系人信息为："+passengers_json);
		
		
		//#==================================================车票查询====================================================================
		//南阳-北京
		passengerscookMap.put("current_captcha_type", "Z");
		passengerscookMap.put("_jc_save_fromStation", "北京,BJP");
		passengerscookMap.put("_jc_save_toStation", "天津,TJP");
		passengerscookMap.put("_jc_save_fromDate", "2018-02-20");
		passengerscookMap.put("_jc_save_toDate", "2018-01-24");
		passengerscookMap.put("_jc_save_wfdc_flag", "dc");
		url ="https://kyfw.12306.cn/otn/leftTicket/init";
		session.get(url).verify(false).headers(getHeaders()).cookies(passengerscookMap).timeout(30*1000).send().readToText();
		url="https://kyfw.12306.cn/otn/dynamicJs/qgdbwtc";
		session.get(url).verify(false).headers(getHeaders()).cookies(passengerscookMap).timeout(30*1000).send().readToText();
		

		url ="https://kyfw.12306.cn/otn/leftTicket/queryZ?leftTicketDTO.train_date=2018-02-20&leftTicketDTO.from_station=BJP&leftTicketDTO.to_station=TJP&purpose_codes=ADULT";
		String leftTicket_info= session.get(url).verify(false).headers(getHeaders()).cookies(passengerscookMap).timeout(30*1000).send().readToText();		
		System.out.println("车票信息："+leftTicket_info);
		
		//#==================================================购票====================================================================
		//checkUser
		
		passengerscookMap.put("acw_tc", "AQAAABYlIhI3IAYAWgpRKn++Jm3h7s9v");
		url ="https://kyfw.12306.cn/otn/login/checkUser";
		
		Map<String,Object> checkUserMap = new HashMap();
		checkUserMap.put("_json_att", "");
		
		String checkUser_rpInfo =session.post(url).verify(false).headers(getHeaders()).cookies(passengerscookMap).forms(checkUserMap).timeout(30*1000).send().readToText();		
		System.out.println("checkUser校验消息:"+checkUser_rpInfo);
		
		url="https://kyfw.12306.cn/otn/leftTicket/submitOrderRequest";
		Map<String,Object> submitOrderMap = new HashMap();
		//secretStr 取数有问题，如何取到 是一个问题
		submitOrderMap.put("secretStr", "LiG+qj4KwpVp0OEdoVXc/0hMPFxs1HzpcGsQ9cTUtrDjjAWvlWVNOeM3g3Vflybew6syQSTYc5FeJ5ndx1NRzIKnUv6aZyEu9sodSjnHsnPNHRDKoQsZkvP67S2yqBM1c0+iJOnmElNzYOc9WMoMPZ7StRgeEIGcTBsud8whVS2Niw8pGtizQDFteQC/74fdsQpxw8452T2jY63EhPg9Ro+V5Vj8bEAX1d7Jhy39bKmdyqDEmluUsaYw6PZN4D3gBmux27kBvWY=");
		submitOrderMap.put("train_date", "2018-02-20");
		submitOrderMap.put("back_train_date", "2018-01-24");
		submitOrderMap.put("tour_flag", "dc");
		submitOrderMap.put("purpose_codes", "ADULT");
		submitOrderMap.put("query_from_station_name", "北京");
		submitOrderMap.put("query_to_station_name", "天津");
		submitOrderMap.put("undefined", "");
		String submitOrderRequest_rpInfo =session.post(url).verify(false).headers(getHeaders()).cookies(passengerscookMap).forms(submitOrderMap).timeout(30*1000).send().readToText();		
		System.out.println("submitOrderRequest信息："+submitOrderRequest_rpInfo);
		
		
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
		cookMap.put("RAIL_EXPIRATION", "1517009786454");
		cookMap.put("RAIL_DEVICEID", "EYbU2FQc6rs9SYvH8RCQr3IwqvE0a812bFZ_t1PTeIz5yiUMOHLu6Fb857A3fIScnPe_B1m80mpGW-jLRCUuCaJrX5wau0ovw115cWJbHb2p0sixVieHV9yhQJJwDg_Ppnfv3TydIPyLOSIhP5_9R4Fo5q4sR9XL");
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
	
	public void test() {
		String url ="https://kyfw.12306.cn/otn/leftTicket/queryZ?leftTicketDTO.train_date=2018-02-21&leftTicketDTO.from_station=NFF&leftTicketDTO.to_station=BJP&purpose_codes=ADULT";
		String leftTicket_info= session.get(url).verify(false).timeout(30*1000).send().readToText();
		System.out.println(leftTicket_info);
	}
	
	public void test2() {

		URI uri=null;
		try {
			uri = new URI("https://kyfw.12306.cn/otn/login/init");
			Desktop.getDesktop().browse(uri);  
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        
	}
	
	
	public static void main(String[]args) throws Exception {
		TicketsServiceImpl i = new TicketsServiceImpl();
		i.toLogin();
		//i.checkCaptcha();
		//i.getCaptcha();
		//i.test2();
		//i.homePage();
		//jiexi();

	}
	
	
	public static void jiexi() {
		 File file = new File("E:/data.txt");  
        Long filelength = file.length();  
        byte[] filecontent = new byte[filelength.intValue()];  
        try {  
            FileInputStream in = new FileInputStream(file);  
            in.read(filecontent);  
            in.close();  
            String text = new String(filecontent,"UTF-8"); 
            System.out.println(text);
            
            TrainInfoVO trainInfoVO = new TrainInfoVO();
            
            Map mapRoot = gson.fromJson( text, HashMap.class);
            Map<String,Object> mapdata =  (Map) mapRoot.get("data");
            
            
            //获取mp中的值
            Map<String,String> map_jsonMap = (Map) mapdata.get("map");
            List<TrainStationInfoVO> trainStationInfos = new ArrayList<TrainStationInfoVO>();
            Set<String> set = map_jsonMap.keySet(); //取出所有的key值
            for (String key:set) {
            	TrainStationInfoVO trainStationInfo = new TrainStationInfoVO();
            	trainStationInfo.setCode(key);
            	trainStationInfo.setName(map_jsonMap.get(key));
            	trainStationInfos.add(trainStationInfo);
            }
            
            //获取result中的值
           List<String> result_trains =  (ArrayList) mapdata.get("result");
           
           
          for(String trainInfo:result_trains) {
        	 String[] sp = trainInfo.split("\\|");
        	 TrainLineInfoVO trainLineInfo = new TrainLineInfoVO();
        	 
        	 trainLineInfo.setSecretStr(sp[0]); //订单请求时使用
        	 trainLineInfo.setStatus(sp[1]); //状态
        	 trainLineInfo.setTrain_no(sp[2]); //获取价格，获取车次车站信息使用
        	 trainLineInfo.setStation_train_code(sp[3]); //车次
        	 trainLineInfo.setFrom_station_y_code(sp[4]); //列车始发站code
        	 trainLineInfo.setFrom_station_y_name(StationDataUtils.getstation_name(sp[4]));
        	 trainLineInfo.setTo_station_y_code(sp[5]); //列车终点站code
        	 trainLineInfo.setTo_station_y_name(StationDataUtils.getstation_name(sp[5]));
        	 trainLineInfo.setFrom_station_code(sp[6]);//出发站点 
        	 trainLineInfo.setFrom_station_name(StationDataUtils.getstation_name(sp[6]));
        	 trainLineInfo.setTo_station_code(sp[7]); //到站点
        	 trainLineInfo.setTo_station_name(StationDataUtils.getstation_name(sp[7]));
        	 trainLineInfo.setStart_time(sp[8]); //出发时间点
        	 trainLineInfo.setArrive_time(sp[9]);//到达时间
        	 trainLineInfo.setDuration(sp[10]); //历时
        	 trainLineInfo.setIsBuy(sp[11]); //是否可预定
        	 
        	 
        	//座位信息
    	 	trainLineInfo.setSwz_num(sp[32]);  //商务座/特等座
    		trainLineInfo.setZy_num(sp[31]); //一等座
    		trainLineInfo.setZe_num(sp[30]);//二等座
    		trainLineInfo.setGjrw_num(sp[21]);//高级软卧---
    		trainLineInfo.setRw_num(sp[23]); //软卧
    		trainLineInfo.setDw_num(sp[33]); //动卧--
    		trainLineInfo.setYw_num(sp[28]);//硬卧
    		trainLineInfo.setRz_num(sp[24]);//软座
    		trainLineInfo.setYz_num(sp[29]); //硬座
    		trainLineInfo.setWz_num(sp[26]);//无座
    		trainLineInfo.setQt_num(sp[22]);//其它----
        	
        	 
        	 System.out.println(xs.toXML(trainLineInfo));
          
          }
            
         
            
            
            
        } catch (Exception e) {  
            e.printStackTrace();  
        } 
	}
	
	

}