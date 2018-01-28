package com.tickets.tickets.tests;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thoughtworks.xstream.XStream;
import com.tickets.tickets.domain.PassengerVO;
import com.tickets.tickets.utils.DateUtils;

public class TicketTest3 {

	public static void main2(String[] args) {
//		String text ="var ctx='otn'; var globalRepeatSubmitToken = '610375624ef02c8e70d06ed6a5ee17fe';var global_lang = 'zh_CN';";
//		Pattern pattern = Pattern.compile("globalRepeatSubmitToken = '(.*?)'"); 
//		Matcher matcher = pattern.matcher(text); 
//		while(matcher.find()){ 
//			System.out.println(matcher.start()+"--"+matcher.end()); 
//			System.out.println(text.substring(matcher.start()+ "globalRepeatSubmitToken = '".length(), matcher.end()-1));
//
//		} 
		String resp_initDc = "";
		File file = new File("D:/work/12306work/code/data/iniDoc.txt");
	    Long filelength = file.length();  
        byte[] filecontent = new byte[filelength.intValue()];  
        try {  
            FileInputStream in = new FileInputStream(file);  
            in.read(filecontent);  
            in.close();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }
        try {  
        	resp_initDc=  new String(filecontent, "UTF-8");  
        } catch (Exception e) {  
            e.printStackTrace();  
        }
        
        Pattern pattern = Pattern.compile("globalRepeatSubmitToken = '(.*?)'"); 
		Matcher matcher = pattern.matcher(resp_initDc); 
		String submitToken ="";
		String keyCheckIsChange ="";
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
	}

	public static void main21(String []args) {
		Gson gson = new Gson();
		XStream xs = new XStream();
		String json ="{'validateMessagesShowId':'_validatorMessage','status':true,'httpstatus':200,'data':{'isExist':true,'exMsg':'','two_isOpenClick':['93','95','97','99'],'other_isOpenClick':['91','93','98','99','95','97'],'normal_passengers':[{'code':'3','passenger_name':'李正统','sex_code':'M','sex_name':'男','born_date':'2014-09-19 00:00:00','country_code':'CN','passenger_id_type_code':'1','passenger_id_type_name':'二代身份证','passenger_id_no':'411303198810085915','passenger_type':'1','passenger_flag':'0','passenger_type_name':'成人','mobile_no':'','phone_no':'','email':'','address':'','postalcode':'','first_letter':'LZT','recordCount':'9','total_times':'99','index_id':'0'},{'code':'1','passenger_name':'贾书改','sex_code':'M','sex_name':'男','born_date':'2014-08-09 00:00:00','country_code':'CN','passenger_id_type_code':'1','passenger_id_type_name':'二代身份证','passenger_id_no':'412924197603092240','passenger_type':'1','passenger_flag':'0','passenger_type_name':'成人','mobile_no':'','phone_no':'','email':'','address':'','postalcode':'','first_letter':'JSG','recordCount':'9','total_times':'99','index_id':'1'},{'code':'6','passenger_name':'吕丛丛','sex_code':'F','sex_name':'女','born_date':'2014-05-01 00:00:00','country_code':'CN','passenger_id_type_code':'1','passenger_id_type_name':'二代身份证','passenger_id_no':'411303198610095529','passenger_type':'1','passenger_flag':'0','passenger_type_name':'成人','mobile_no':'15938473650','phone_no':'','email':'','address':'','postalcode':'','first_letter':'LCC','recordCount':'9','total_times':'99','index_id':'2'},{'code':'2','passenger_name':'李付营','sex_code':'M','sex_name':'男','born_date':'2015-02-22 00:00:00','country_code':'CN','passenger_id_type_code':'1','passenger_id_type_name':'二代身份证','passenger_id_no':'412924197110282257','passenger_type':'1','passenger_flag':'0','passenger_type_name':'成人','mobile_no':'','phone_no':'','email':'','address':'','postalcode':'','first_letter':'LFY','recordCount':'9','total_times':'99','index_id':'3'},{'code':'4','passenger_name':'刘中学','sex_code':'F','sex_name':'女','born_date':'2017-03-17 00:00:00','country_code':'CN','passenger_id_type_code':'1','passenger_id_type_name':'二代身份证','passenger_id_no':'412924196405262224','passenger_type':'1','passenger_flag':'0','passenger_type_name':'成人','mobile_no':'','phone_no':'','email':'','address':'','postalcode':'','first_letter':'LZX','recordCount':'9','total_times':'99','index_id':'4'},{'code':'5','passenger_name':'鲁正源','sex_code':'M','sex_name':'男','born_date':'1900-01-01 00:00:00','country_code':'CN','passenger_id_type_code':'1','passenger_id_type_name':'二代身份证','passenger_id_no':'411302198808103436','passenger_type':'1','passenger_flag':'0','passenger_type_name':'成人','mobile_no':'13735817120','phone_no':'','email':'','address':'','postalcode':'','first_letter':'LZY','recordCount':'9','total_times':'99','index_id':'5'},{'code':'7','passenger_name':'马超','sex_code':'F','sex_name':'女','born_date':'1978-04-09 00:00:00','country_code':'CN','passenger_id_type_code':'1','passenger_id_type_name':'二代身份证','passenger_id_no':'410181197804095044','passenger_type':'1','passenger_flag':'0','passenger_type_name':'成人','mobile_no':'','phone_no':'','email':'','address':'','postalcode':'','first_letter':'MC','recordCount':'9','total_times':'99','index_id':'6'},{'code':'8','passenger_name':'王志克','sex_code':'M','sex_name':'男','born_date':'1900-01-01 00:00:00','country_code':'CN','passenger_id_type_code':'1','passenger_id_type_name':'二代身份证','passenger_id_no':'411302199010053219','passenger_type':'1','passenger_flag':'0','passenger_type_name':'成人','mobile_no':'18610315382','phone_no':'','email':'','address':'','postalcode':'','first_letter':'WZK','recordCount':'9','total_times':'99','index_id':'7'},{'code':'9','passenger_name':'张天华','sex_code':'M','sex_name':'男','born_date':'1970-01-01 00:00:00','country_code':'CN','passenger_id_type_code':'1','passenger_id_type_name':'二代身份证','passenger_id_no':'412924197110052558','passenger_type':'1','passenger_flag':'0','passenger_type_name':'成人','mobile_no':'18610315382','phone_no':'','email':'','address':'','postalcode':'','first_letter':'ZTH','recordCount':'9','total_times':'99','index_id':'8'}],'dj_passengers':[]},'messages':[],'validateMessages':{}}";
		Map<String,Object> json_map = gson.fromJson(json,HashMap.class);
		Map<String,Object> data_map = (Map<String, Object>) json_map.get("data");
		List<Map> normal_passengers_json = (List) data_map.get("normal_passengers");
		List<PassengerVO> passengers = new ArrayList();
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
		//	 =  gson.fromJson(normal_passengers_json,new TypeToken<List<PassengerVO>>() {}.getType());
		System.out.print(xs.toXML(passengers));
		System.out.print("");
	}
	
	public static void main(String[]args) throws Exception{
		//Tue+Feb+20+2018+00:00:00+GMT+0800
		SimpleDateFormat sdf = new SimpleDateFormat("EEE+MMM+dd+yyyy+HH:mm:ss", java.util.Locale.US);
		System.out.println(sdf.format(DateUtils.getDate("2018-03-20 00:00:00"))+"+GMT+0800");
		//System.out.println(new Date());
	}
	
}
