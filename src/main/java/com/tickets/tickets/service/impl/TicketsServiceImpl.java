package com.tickets.tickets.service.impl;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.JsonSyntaxException;
import com.thoughtworks.xstream.XStream;
import com.tickets.tickets.domain.PassengerVO;
import com.tickets.tickets.domain.TrainInfoVO;
import com.tickets.tickets.domain.TrainLineInfoVO;
import com.tickets.tickets.domain.TrainStationInfoVO;
import com.tickets.tickets.service.TicketsService;
import com.tickets.tickets.utils.DateUtils;
import com.tickets.tickets.utils.StationDataUtils;

import net.dongliu.requests.Requests;
import net.dongliu.requests.Session;

@Service
public class TicketsServiceImpl implements TicketsService {
	
	static	Session session = Requests.session();
	static XStream xs = new XStream();
	String[] strs = new String[]{"35,35","105,35","175,35","245,35","35,105","105,105","175,105","245,105"};
	private static Gson gson = new Gson();
	static 	Map passengerscookMap = null;
	static TrainInfoVO trainInfoVO=null;
	static Map<String, Object> cookMap = new HashMap();
	static String submitToken  =""; //提交订单获取联系人时使用
	static String keyCheckIsChange  =""; //订单确认时使用
	static String orderId="";		//orderId
	 
	
	
	
	@Override
	public void login12306() {
		toLogin();
	}
	
	
	public void toLogin() {
			
		String url ="https://kyfw.12306.cn/otn/login/init";
		session.get(url).verify(false).headers(getHeaders()).timeout(30*1000).cookies(getCookMap()).send();

		//验证码处理'
		boolean booleanCaptcha = getCaptcha();
		
		while(!booleanCaptcha) {
			booleanCaptcha = getCaptcha();
		}
		//登录
		Map<String, Object> map = new HashMap<>();
		map.put("username", "");
		map.put("password", "");
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
	    passengerscookMap = getCookMap();
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
		
		System.out.println("登录成功，session hashcode:"+session.hashCode());
		
		
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
		headers.put("Host", "kyfw.12306.cn");
		//headers.put("Origin", "https://kyfw.12306.cn");
		//headers.put("X-Requested-With", "XMLHttpRequest");
		//headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		headers.put("Referer", "https://kyfw.12306.cn/otn/login/init");
		//headers.put("Accept", "*/*");
		//headers.put("Accept-Encoding", "gzip, deflate, br");
		//headers.put("Accept-Language", "zh-CN,zh;q=0.8");
		headers.put("User-Agent", "	Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Win64; x64; Trident/5.0)");
	
		return headers;
	}
	
	public Map getCookMap(){
		/*if(cookMap.get("RAIL_EXPIRATION")== null || "".equals(cookMap.get("RAIL_EXPIRATION"))){
			String url=null;
			try {
				url = "https://kyfw.12306.cn/otn/HttpZF/logdevice?algID=WYEdoc45yu&hashCode=EhTtj7Znzyie6I21jpgekYReLAnA8fyGEB4VlIGbF0g&FMQw=0&q4f3=zh-CN&VPIf=1&custID=133&VEek=unknown&dzuS=20.0%20r0&yD16=0&EOQP=895f3bf3ddaec0d22b6f7baca85603c4&lEnu=3232235778&jp76=e8eea307be405778bd87bbc8fa97b889&hAqN=Win32&platform=WEB&ks0Q=2955119c83077df58dd8bb7832898892&TeRS=728x1366&tOHY=24xx768x1366&Fvje=i1l1o1s1&q5aJ=-8&wNLf=99115dfb07133750ba677d055874de87"
						+ "&0aew="+ URLEncoder.encode(gson.toJson(getHeaders()), "UTF-8")
						+ "&timestamp="+ (new Date()).getTime();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			String res= session.get(url).verify(false).headers(getHeaders()).timeout(30*1000).send().readToText();
			res = res.substring(res.indexOf("callbackFunction('")+"callbackFunction('".length(),res.lastIndexOf("')"));
			Map resmap =gson.fromJson(res, HashMap.class);
			System.out.println("重组cookie："+resmap);
			cookMap.put("RAIL_EXPIRATION", resmap.get("exp"));
			cookMap.put("RAIL_DEVICEID", resmap.get("dfp"));
			
		}*/
		cookMap.put("RAIL_EXPIRATION", "1517331316020");
		cookMap.put("RAIL_DEVICEID", "THEH5tjSC0PJh5auUKE0uuriS-20pr_u_mh723B9uQp1rTDnDpVuktoFs6tHS3a3u-njqndsXnJN3QDTE2wkZmL4zJINCK85YqZcpyteXujkBnF_CUrWsNVJnptfRV3XI2ASKgohuB-UKb4zlG_hvtW__KDV7oqS");
		
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
	
	
	/**
	 * 将车票返回信息实例化
	 * @param json 
	 * @return
	 */
	private TrainInfoVO getTrainInfoVO(String json) {
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
       
       List< TrainLineInfoVO> trainLineInfos = new ArrayList();
       
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


	@Override
	public TrainInfoVO query() {
		System.out.println("车票查询，session hashcode:"+session.hashCode());
		String url ="https://kyfw.12306.cn/otn/leftTicket/queryZ?leftTicketDTO.train_date=2018-02-20&leftTicketDTO.from_station=BJP&leftTicketDTO.to_station=TJP&purpose_codes=ADULT";
		String leftTicket_info= session.get(url).verify(false).headers(getHeaders()).cookies(passengerscookMap).timeout(30*1000).send().readToText();		
		System.out.println("车票信息："+leftTicket_info);
		trainInfoVO = getTrainInfoVO(leftTicket_info);
		return trainInfoVO;
		
	}


	@Override
	public void buyTicket() {
		
		String seatType = "O";//二等座		
		//#==================================================购票====================================================================
		//checkUser
		//passengerscookMap.put("acw_tc", "AQAAAPfkuVXZUA0AWgpRKhi69R8ktEex");
		Map ticketHeaderMap = getHeaders();
		ticketHeaderMap.put("Referer", "'https://kyfw.12306.cn/otn/leftTicket/init");
		String url ="https://kyfw.12306.cn/otn/login/checkUser";
		
		Map<String,Object> checkUserMap = new HashMap();
		checkUserMap.put("_json_att", "");
		
		String checkUser_rpInfo =session.post(url).verify(false).headers(ticketHeaderMap).cookies(passengerscookMap).forms(checkUserMap).timeout(30*1000).send().readToText();		
		System.out.println("checkUser校验消息:"+checkUser_rpInfo);
		
		List<TrainLineInfoVO> trainvos = trainInfoVO.getTrainLineInfos();

		
		url="https://kyfw.12306.cn/otn/leftTicket/submitOrderRequest";
		Map<String,Object> submitOrderMap = new HashMap();
		
		String SecretStr= null;
		TrainLineInfoVO trainvo = trainvos.get(3);
		try {
			
			 SecretStr =  java.net.URLDecoder.decode(trainvo.getSecretStr(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		submitOrderMap.put("secretStr", SecretStr);
		submitOrderMap.put("train_date", "2018-02-20");
		submitOrderMap.put("back_train_date", "2018-01-28");
		submitOrderMap.put("tour_flag", "dc");
		submitOrderMap.put("purpose_codes", "ADULT");
		submitOrderMap.put("query_from_station_name", "北京");
		submitOrderMap.put("query_to_station_name", "天津");
		submitOrderMap.put("undefined", "");
		String submitOrderRequest_rpInfo =session.post(url).verify(false).headers(ticketHeaderMap).cookies(passengerscookMap).forms(submitOrderMap).timeout(30*1000).send().readToText();		
		System.out.println("submitOrderRequest信息："+submitOrderRequest_rpInfo);
		
		
		url = "https://kyfw.12306.cn/otn/confirmPassenger/initDc";
		Map initDcDataMap = new HashMap();
		initDcDataMap.put("_json_att=", "");
		String resp_initDc= session.post(url).verify(false).headers(ticketHeaderMap).cookies(passengerscookMap).forms(initDcDataMap).timeout(30*1000).send().readToText();
		System.out.println("resp_initDc返回值:"+resp_initDc);
		
	    Pattern pattern = Pattern.compile("globalRepeatSubmitToken = '(.*?)'"); 
		Matcher matcher = pattern.matcher(resp_initDc); 
		while(matcher.find()){ 
			submitToken = resp_initDc.substring(matcher.start()+ "globalRepeatSubmitToken = '".length(), matcher.end()-1);
			break;
		} 
		Pattern pattern2 = Pattern.compile("key_check_isChange':'(.*?)'"); 
		Matcher matche2r = pattern2.matcher(resp_initDc); 
		while(matche2r.find()){ 
			keyCheckIsChange  = resp_initDc.substring(matche2r.start()+ "key_check_isChange':'".length(), matche2r.end()-1);
			break;
		} 
		System.out.println(submitToken  +"         " +keyCheckIsChange);
		System.out.println("正在获取乘客信息:"); 
		url = "https://kyfw.12306.cn/otn/confirmPassenger/getPassengerDTOs";
		Map getPassengerDTOsDataMap = new HashMap();
		getPassengerDTOsDataMap.put("_json_att", "");
		getPassengerDTOsDataMap.put("REPEAT_SUBMIT_TOKEN", submitToken);
		String resp_getPassengerDTOs= session.post(url).verify(false).headers(ticketHeaderMap).cookies(passengerscookMap).forms(getPassengerDTOsDataMap).timeout(30*1000).send().readToText();
		System.out.println("获取的乘客信息:"+resp_getPassengerDTOs);
		List<PassengerVO> passengers = getPassengers(resp_getPassengerDTOs);
		
		//提交
		url = "https://kyfw.12306.cn/otn/confirmPassenger/checkOrderInfo";
		Map checkOrderInfo_map =  new HashMap();
		checkOrderInfo_map.put("cancel_flag", "2");
		checkOrderInfo_map.put("bed_level_order_num", "000000000000000000000000000000");
		
		PassengerVO pv= passengers.get(2);
		String passengerTicketStr =seatType+ ","+pv.getPassenger_flag()+","+pv.getPassenger_type()+","+pv.getPassenger_name()+","+ pv.getPassenger_id_type_code() +","+pv.getPassenger_id_no()+","+pv.getMobile_no()+",N";
		checkOrderInfo_map.put("passengerTicketStr", passengerTicketStr);
		
		String oldPassengerStr =pv.getPassenger_name()+","+pv.getPassenger_id_type_code()+","+pv.getPassenger_id_no()+","+pv.getPassenger_type()+"_";
		checkOrderInfo_map.put("oldPassengerStr", oldPassengerStr);
		checkOrderInfo_map.put("tour_flag", "dc");
		checkOrderInfo_map.put("randCode", "");
		checkOrderInfo_map.put("whatsSelect", "1");
		checkOrderInfo_map.put("_json_att", "");
		checkOrderInfo_map.put("REPEAT_SUBMIT_TOKEN", submitToken);
		System.out.println(checkOrderInfo_map);
		String rep_checkOrderInfo = session.post(url).verify(false).headers(ticketHeaderMap).cookies(passengerscookMap).forms(checkOrderInfo_map).timeout(30*1000).send().readToText();
		System.out.println("checkOrderInfo返回信息："+rep_checkOrderInfo);
		
		//getQueueCount
		url = "https://kyfw.12306.cn/otn/confirmPassenger/getQueueCount";
		SimpleDateFormat sdf = new SimpleDateFormat("EEE+MMM+dd+yyyy+HH:mm:ss", java.util.Locale.US);
		String dateGMT = DateUtils.getDate("2018-02-20")+"+GMT+0800";
		Map getQueueCount_data_map = new HashMap();
		getQueueCount_data_map.put("train_date",dateGMT);
		getQueueCount_data_map.put("train_no",trainvo.getTrain_no());
		getQueueCount_data_map.put("stationTrainCode", trainvo.getStation_train_code());
		getQueueCount_data_map.put("seatType", seatType);
		getQueueCount_data_map.put("fromStationTelecode", trainvo.getFrom_station_code());
		getQueueCount_data_map.put("toStationTelecode", trainvo.getTo_station_code());
		getQueueCount_data_map.put("leftTicket", trainvo.getLeftTicket());
		getQueueCount_data_map.put("purpose_codes", "00");
		getQueueCount_data_map.put("train_location", trainvo.getTrainLocation());
		getQueueCount_data_map.put("_json_att", "");
		getQueueCount_data_map.put("REPEAT_SUBMIT_TOKEN", submitToken);
		String rep_getQueueCount = session.post(url).verify(false).headers(ticketHeaderMap).cookies(passengerscookMap).forms(getQueueCount_data_map).timeout(30*1000).send().readToText();
		System.out.println("rep_getQueueCount："+rep_getQueueCount);
		
		url = "https://kyfw.12306.cn/otn/confirmPassenger/confirmSingleForQueue";
		Map confirmSingleForQueue_data_map = new HashMap();
		confirmSingleForQueue_data_map.put("passengerTicketStr",passengerTicketStr);
		confirmSingleForQueue_data_map.put("oldPassengerStr",   oldPassengerStr);
		confirmSingleForQueue_data_map.put("randCode",   "");
		confirmSingleForQueue_data_map.put("purpose_codes",   "00");
		confirmSingleForQueue_data_map.put("key_check_isChange",   keyCheckIsChange);
		confirmSingleForQueue_data_map.put("leftTicketStr",   trainvo.getLeftTicket());
		confirmSingleForQueue_data_map.put("train_location",   trainvo.getTrainLocation());
		confirmSingleForQueue_data_map.put("choose_seats",   "");
		confirmSingleForQueue_data_map.put("choose_seats",   "000");
		confirmSingleForQueue_data_map.put("whatsSelect",   "1");
		confirmSingleForQueue_data_map.put("roomType",   "00");
		confirmSingleForQueue_data_map.put("dwAll",   "N");
		confirmSingleForQueue_data_map.put("_json_att",   "");
		confirmSingleForQueue_data_map.put("REPEAT_SUBMIT_TOKEN",   submitToken);
		String rep_confirmSingleForQueue = session.post(url).verify(false).headers(ticketHeaderMap).cookies(passengerscookMap).forms(confirmSingleForQueue_data_map).timeout(30*1000).send().readToText();
		System.out.println("rep_confirmSingleForQueue："+rep_getQueueCount);
		
	
		//queryOrderWaitTime
		int waitTimeErrorCount =0;
		while(waitTimeErrorCount<10) {
			try {
			url = "https://kyfw.12306.cn/otn/confirmPassenger/queryOrderWaitTime";
			Map queryOrderWaitTime_data_map = new HashMap();
			queryOrderWaitTime_data_map.put("random",new Date().getTime());
			queryOrderWaitTime_data_map.put("_json_att","");
			queryOrderWaitTime_data_map.put("REPEAT_SUBMIT_TOKEN",submitToken);
			queryOrderWaitTime_data_map.put("tourFlag","dc");
			String rep_queryOrderWaitTime = session.post(url).verify(false).headers(ticketHeaderMap).cookies(passengerscookMap).forms(queryOrderWaitTime_data_map).timeout(30*1000).send().readToText();
			Map<String,Object> json_map = gson.fromJson(rep_queryOrderWaitTime, HashMap.class);
			Map data_map = (Map) json_map.get("data");
			orderId = (String) data_map.get("orderId");
			if(orderId!=null || !"".equals(orderId)) {
				break;
			}
			System.out.println("rep_queryOrderWaitTime：" +rep_queryOrderWaitTime);
			waitTimeErrorCount++;
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
		
		//resultOrderForDcQueue
        url = "https://kyfw.12306.cn/otn/confirmPassenger/resultOrderForDcQueue";
        Map resultOrderForDcQueue_data_map = new HashMap();
        resultOrderForDcQueue_data_map.put("orderSequence_no", orderId);
        resultOrderForDcQueue_data_map.put("_json_att", "");
        resultOrderForDcQueue_data_map.put("REPEAT_SUBMIT_TOKEN", submitToken);
		String rep_resultOrderForDcQueue = session.post(url).verify(false).headers(ticketHeaderMap).cookies(passengerscookMap).forms(resultOrderForDcQueue_data_map).timeout(30*1000).send().readToText();
		System.out.println("rep_resultOrderForDcQueue：" +rep_resultOrderForDcQueue);
	}


	
	/**
	 * 获取乘客信息
	 * @param resp_getPassengerDTOs
	 * @return
	 */
	private List<PassengerVO> getPassengers(String resp_getPassengerDTOs) {
		List<PassengerVO> passengers = new ArrayList();
		Map<String,Object> json_map = gson.fromJson(resp_getPassengerDTOs,HashMap.class);
		Map<String,Object> data_map = (Map<String, Object>) json_map.get("data");
		List<Map> normal_passengers_json = (List) data_map.get("normal_passengers");
		for(Map<String,String> m:normal_passengers_json){
			PassengerVO vo = new PassengerVO();
			vo.setAddress(m.get("code"));
			vo.setPassenger_name(m.get("passenger_name"));
			vo.setSex_code(m.get("sex_code"));
			vo.setSex_name(m.get("sex_name"));
			vo.setBorn_date(m.get("born_date"));
			vo.setCountry_code(m.get("country_code"));
			vo.setPassenger_id_type_code(m.get("passenger_id_type_code"));
			vo.setPassenger_id_type_name(m.get("passenger_id_type_name"));
			vo.setPassenger_id_no(m.get("passenger_id_no"));
			vo.setPassenger_type(m.get("passenger_type"));
			vo.setPassenger_flag(m.get("passenger_flag"));
			vo.setPassenger_type_name(m.get("passenger_type_name"));
			vo.setMobile_no(m.get("mobile_no"));
			vo.setEmail(m.get("email"));
			vo.setAddress(m.get("address"));
			vo.setPostalcode(m.get("postalcode"));
			vo.setFirst_letter(m.get("first_letter"));
			vo.setRecordCount(m.get("recordCount"));
			vo.setTotal_times(m.get("total_times"));
			vo.setIndex_id(m.get("index_id"));
			passengers.add(vo);
		}
		return passengers;
	}
	
	
	

}