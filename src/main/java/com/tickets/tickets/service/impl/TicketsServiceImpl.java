package com.tickets.tickets.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.thoughtworks.xstream.XStream;
import com.tickets.tickets.service.TicketsService;

import net.dongliu.requests.Requests;
import net.dongliu.requests.Session;

public class TicketsServiceImpl implements TicketsService {
	

	Session session = Requests.session();
	
	@Override
	public void login12306() {
		String url1 ="https://kyfw.12306.cn/otn/passport?redirect=/otn/";
		String resp1 = session.get(url1).verify(false).headers(getHeaders()).send().readToText();
		XStream xs = new XStream();
		//System.out.println(resp1);
		System.out.println(xs.toXML(session));
	}
	
	
	/**
	 * 获取验证码
	 * @throws Exception 
	 */
	public void getCaptcha(){
		String url ="https://kyfw.12306.cn/passport/captcha/captcha-image?login_site=E&module=login&rand=sjrand&0.7581446374982701";
		session.get(url).verify(false).headers(getHeaders()).send().writeToFile("D:/img/c1.jpg");
		Runtime run = Runtime.getRuntime();
		try {
			run.exec("cmd.exe /c D:/img/c1.jpg");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public Map getHeaders() {
		Map<String, Object> headers = new HashMap<>();
		headers.put("User-Agent", "	Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Win64; x64; Trident/5.0)");
		headers.put("Host", "kyfw.12306.cn");
		return headers;
	}
	
	public static void main(String[]args) {
		TicketsServiceImpl i = new TicketsServiceImpl();
		i.getCaptcha();
	}
	

}
