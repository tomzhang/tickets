package com.tickets.tickets.service.impl;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.thoughtworks.xstream.XStream;
import com.tickets.tickets.domain.PassengerVO;
import com.tickets.tickets.domain.TrainInfoVO;
import com.tickets.tickets.domain.TrainLineInfoVO;
import com.tickets.tickets.domain.TrainStationInfoVO;
import com.tickets.tickets.service.Headers;

import com.tickets.tickets.utils.FileUtils;
import com.tickets.tickets.utils.StationDataUtils;
import com.tickets.tickets.utils.TextTable;
import javafx.scene.text.Text;
import net.dongliu.requests.*;

public class TicketsServiceImpl2 {
	
	String url ="";
	static Session session = Requests.session();
	static XStream xs = new XStream();
	String[] strs = new String[]{"35,35","105,35","175,35","245,35","35,105","105,105","175,105","245,105"};
	static Gson gson = new Gson();
	String captcha_path="D:\\work2\\img\\captcha.jpg";

	public void toLogin() {
		//#==================================================登录====================================================================
		String url_init ="https://kyfw.12306.cn/otn/login/init";
		session.get(url_init).verify(false).headers(Headers.initHeader()).timeout(20*1000).send();

		String url_logdevice="https://kyfw.12306.cn/otn/HttpZF/logdevice?algID=QRqR6wPyKZ&hashCode=lkUTDoJPpSm3zCb7GSoeajQQHkpHcXusReeMHhKXtMk&FMQw=0&q4f3=en-US&VySQ=FGH9RLTQe0Il8zvsUhAS0D1MpIRW0RrY&VPIf=1&custID=133&VEek=unknown&dzuS=0&yD16=1&EOQP=a6588b471456c0b2345c44dc367af87d&jp76=223017b546a29cfe7aa7f1efcefc0b88&hAqN=Win32&platform=WEB&ks0Q=ebda3f4fca91cb13d8a03858320fadbb&TeRS=777x1449&tOHY=24xx815x1449&Fvje=i1l1s1&q5aJ=-8&wNLf=99115dfb07133750ba677d055874de87&0aew={0}&E3gR=009220e11f272d503c007d3cdb571082&timestamp={1}";
		url_logdevice=url_logdevice.replace("{0}","Mozilla/5.0%20(Windows%20NT%2010.0;%20WOW64;%20Trident/7.0;%20rv:11.0)%20like%20Gecko");
		url_logdevice=url_logdevice.replace("{1}",String.valueOf(System.currentTimeMillis()));
		String response_logdevice = session.get(url_logdevice).verify(false).headers(Headers.initHeader()).timeout(30*1000).send().readToText();
		String callbackFunction=response_logdevice.substring(18,response_logdevice.length()-2);
		Map<String,Object> map_callbackFunction =  gson.fromJson(callbackFunction, HashMap.class);
		String exp = map_callbackFunction.get("exp").toString();
		String dfp = map_callbackFunction.get("dfp").toString();


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

		String url_login = "https://kyfw.12306.cn/passport/web/login";
		String response_login =session.post(url_login).verify(false).headers(Headers.loginHeader()).body(login_map).timeout(30*1000).send().readToText();
		System.out.println("login输出结果"+response_login);
		Map<String,Object> login_Map = gson.fromJson(response_login, HashMap.class);
		if( (double)(login_Map.get("result_code")) != 0 )
			return;

		String url_userLoginRedirect="https://kyfw.12306.cn/otn/passport?redirect=/otn/login/userLogin";
		session.get(url_userLoginRedirect).verify(false).timeout(30*1000).send().readToText();


		String response_uamtk =session.post(url_uamtk).verify(false).body(request_data_uamtk).headers(Headers.uamtkHeader()).send().readToText();
		System.out.println("uamtk输出结果 "+response_uamtk);
		
		
		Map<String,Object> resutUamtkMap = gson.fromJson(response_uamtk, HashMap.class);
		if( (double)(resutUamtkMap.get("result_code")) ==1 )
			return;

		String newapptk = resutUamtkMap.get("newapptk").toString();
		
		
		Map<String,Object> uamauthclientMap = new HashMap();
		uamauthclientMap.put("tk", newapptk);
		url = "https://kyfw.12306.cn/otn/uamauthclient";
		String resp_uamauthclient = session.post(url).verify(false).headers(getHeaders()).cookies(getCookMap(exp,dfp)).timeout(20*1000).forms(uamauthclientMap).send().readToText();
		//uamauthclient输出结果 {"apptk":"IxNXmsOXNoxibILuVt5x70CaTLs_Ywe-M7S0Lg92l2l0","result_code":0,"result_message":"验证通过","username":"刘中学"}
		
	//	url = "https://kyfw.12306.cn/otn/login/userLogin";
	//	session.get(url).verify(false).headers(getHeaders()).cookies(getCookMap()).forms(uamauthclientMap).timeout(40*1000).send().readToText();
		
		System.out.println("uamauthclient输出结果 "+resp_uamauthclient);
		Map<String,Object> response_uamauthclient_data = gson.fromJson(resp_uamauthclient,HashMap.class);
		if( (double)(response_uamauthclient_data.get("result_code")) != 0 )
			return;
		String username = response_uamauthclient_data.get("username").toString();
		System.out.println("欢迎：【"+username+"】登录");

		List<Cookie> cookies = session.currentCookies();
		FileUtils.writeXmlToFile(xs.toXML(cookies));


		// url = "https://kyfw.12306.cn/otn/index/initMy12306";
		// String loginSucess = session.get(url).verify(false).headers(getHeaders()).cookies(getCookMap()).forms(uamauthclientMap).timeout(20*1000).send().readToText();

		 
		//#==================================================获取联系人====================================================================

		String url_getPassengerDTOs ="https://kyfw.12306.cn/otn/confirmPassenger/getPassengerDTOs";
		Map cookie_getPassengerDTOs = new HashMap();
		cookie_getPassengerDTOs.put("tk",newapptk);
		cookie_getPassengerDTOs.put("RAIL_EXPIRATION",exp);
		cookie_getPassengerDTOs.put("RAIL_DEVICEID",dfp);
		String response_getPassengerDTOs= session.post(url_getPassengerDTOs).verify(false).headers(Headers.getPassengerDTOsHeader()).cookies(cookie_getPassengerDTOs).timeout(100*1000).send().readToText();
		System.out.println("获取联系人返回信息为："+"\n"+response_getPassengerDTOs);
		JsonObject obj = (JsonObject)new Gson().fromJson(response_getPassengerDTOs, JsonObject.class);
		JsonObject json_data =  obj.get("data").getAsJsonObject();
		JsonArray normal_passengers = json_data.getAsJsonArray("normal_passengers");
		Type type = new TypeToken<List<PassengerVO>>(){}.getType();
		List<PassengerVO> list = new Gson().fromJson(normal_passengers,type);
		System.out.println("获取的联系人信息为："+"\n"+list.toArray().toString());

		//#==================================================车票预定====================================================================
		//南阳-北京
/*		passengerscookMap.put("current_captcha_type", "Z");
		passengerscookMap.put("_jc_save_fromStation", "南阳,NFF");
		passengerscookMap.put("_jc_save_toStation", "北京,BJP");
		passengerscookMap.put("_jc_save_fromDate", "2018-09-21");
		passengerscookMap.put("_jc_save_toDate", "2018-09-23");
		passengerscookMap.put("_jc_save_wfdc_flag", "dc");
		url ="https://kyfw.12306.cn/otn/leftTicket/init";
		session.get(url).verify(false).headers(getHeaders()).cookies(passengerscookMap).timeout(20*1000).send().readToText();
		url="https://kyfw.12306.cn/otn/dynamicJs/qgdbwtc";
		session.get(url).verify(false).headers(getHeaders()).cookies(passengerscookMap).timeout(20*1000).send().readToText();*/

		String queryA_url="https://kyfw.12306.cn/otn/leftTicket/queryA?leftTicketDTO.train_date={0}&leftTicketDTO.from_station={1}&leftTicketDTO.to_station={2}&purpose_codes=ADULT";
		queryA_url = queryA_url.replace("{0}","2018-09-24");
		queryA_url = queryA_url.replace("{1}","BXP");
		queryA_url = queryA_url.replace("{2}","FHP");

		Map<String, Object> cookie_queryA = new HashMap<>();
		try {
			cookie_queryA.put("_jc_save_fromStation", URLEncoder.encode("北京西,BXP","UTF-8"));
			cookie_queryA.put("_jc_save_toStation", URLEncoder.encode("滨海,FHP","UTF-8"));
			cookie_queryA.put("_jc_save_fromDate", "2018-09-24");
			cookie_queryA.put("_jc_save_toDate", "2018-09-19");
			cookie_queryA.put("_jc_save_wfdc_flag", "dc");
		}catch (Exception e){
			e.printStackTrace();
		}
		String response_queryA = session.get(queryA_url).verify(false).headers(Headers.queryAHeader()).cookies(cookie_queryA).timeout(30*1000).send().readToText();
		System.out.println(response_queryA);
		TrainInfoVO trainInfoVO = getTrainInfoVO(response_queryA);
		printTrainInfoVO(trainInfoVO);

	}


	public void printTrainInfoVO(TrainInfoVO trainInfoVO){
		List<TrainLineInfoVO> trainLineInfos = trainInfoVO.getTrainLineInfos();
		List<String> names = new ArrayList<>();
		names.add("车次");
		names.add("出发地");
		names.add("目的地");
		names.add("历时");
		names.add("商务/特等");
		names.add("一等座");
		names.add("二等座");
		names.add("高级软卧");
		names.add("软卧");
		names.add("动卧");
		names.add("硬卧");
		names.add("软座");
		names.add("硬座");
		names.add("无座");
		names.add("其它");
		names.add("日期");

		List<List<String>> values = new ArrayList<>();
		for(TrainLineInfoVO v:trainLineInfos ){
			List<String> value = new ArrayList<>();
			value.add(v.getStation_train_code()); //车次
			value.add(v.getFrom_station_name()); //出发地
			value.add(v.getTo_station_name()); //目的地
			value.add(v.getDuration()); //历时
			value.add(v.getSwz_num()); //商务/特等
			value.add(v.getZy_num()); //一等座
			value.add(v.getZe_num()); //二等座
			value.add(v.getGjrw_num()); //高级软卧
			value.add(v.getRw_num()); //软卧
			value.add(v.getDw_num()); //动卧
			value.add(v.getYw_num()); //硬卧
			value.add(v.getRz_num()); //软座
			value.add(v.getYz_num()); //硬座
			value.add(v.getWz_num()); //无座
			value.add(v.getQt_num()); //其它
			value.add(v.getStart_time()); //日期
			values.add(value);
		}

		System.out.println(new TextTable(names,values).printTable());
	}

	/**
	 * 将车票返回信息实例化
	 * @param json
	 * @return
	 */
	public static TrainInfoVO getTrainInfoVO(String json) {
		Map mapRoot = gson.fromJson( json, HashMap.class);
		Map<String,Object> mapdata =  (Map) mapRoot.get("data");

		String flag= (String) mapdata.get("flag");

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

		List<TrainLineInfoVO> trainLineInfos = new ArrayList();

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
			trainLineInfo.setLeftTicket(sp[12]); // leftTicket
			trainLineInfo.setTrainLocation(sp[15]); //15位
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


			trainLineInfos.add(trainLineInfo);
		}

		TrainInfoVO trainInfoVO = new TrainInfoVO();
		trainInfoVO.setFlag(flag);
		trainInfoVO.setTrainLineInfos(trainLineInfos);
		trainInfoVO.setTrainStationInfos(trainStationInfos);

		return trainInfoVO;
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
	
	public Map getCookMap(String RAIL_EXPIRATION,String RAIL_DEVICEID ){
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

	public  void getPersons(){
		String cookies_xml = FileUtils.readXmlToFile();
		List<Cookie> cookies = (List<Cookie>) xs.fromXML(cookies_xml);
		String url_getPassengerDTOs ="https://kyfw.12306.cn/otn/confirmPassenger/getPassengerDTOs";
		Map request_data_getPassengerDTOs = new HashMap();
		request_data_getPassengerDTOs.put("_json_att","");
		//request_data_getPassengerDTOs.put("REPEAT_SUBMIT_TOKEN","");
		String res_passengers= session.get(url_getPassengerDTOs).verify(false).headers(Headers.passengersInitHeader()).cookies(cookies).body(request_data_getPassengerDTOs).timeout(40*1000).send().readToText();
		System.out.println(res_passengers);


/*
		Map passengerscookMap = getCookMap();
		passengerscookMap.put("_json_att", "");

		url ="https://kyfw.12306.cn/otn/passengers/init";
		String res_passengers= session.post(url).verify(false).headers(Headers.passengersInitHeader()).cookies(cookies).timeout(180*1000).send().readToText();
		System.out.println(res_passengers);
	//	String passengers_json = res_passengers.substring(res_passengers.indexOf("[{'passenger_type_name'"), res_passengers.indexOf("'}];")+3);
	//	System.out.println("常用联系人信息为："+passengers_json);*/
	}
	
	
	public static void main(String[]args) {

		TicketsServiceImpl2 i = new TicketsServiceImpl2();
		//i.login12306();
	//	i.toLogin();
	//	i.printTrainInfoVO();



		//i.getPersons();
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
