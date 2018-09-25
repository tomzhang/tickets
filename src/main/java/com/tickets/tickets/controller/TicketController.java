package com.tickets.tickets.controller;

import com.tickets.tickets.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tickets.tickets.service.TicketsService;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/ticket")
@Slf4j
public class TicketController {
	
//	@Autowired
//	private TicketsService ticketsService;
	@Autowired
	private LoginService loginService;
	
	@RequestMapping("/index")
	public String index() {
		loginService.toLogin();
		 return "index";  
	}

	@RequestMapping("/getCaptcha")
	public void getCaptcha( HttpServletResponse response) throws Exception{
		String filePath = loginService.getCaptcha();
		File file = new File(filePath);
		FileInputStream inputStream = new FileInputStream(file);
		byte[] data = new byte[(int)file.length()];
		int length = inputStream.read(data);
		inputStream.close();
		response.setContentType("image/png");
		OutputStream stream = response.getOutputStream();
		stream.write(data);
		stream.flush();
		stream.close();
	}

	@RequestMapping("/chckCaptcha")
	public Map chckCaptcha(String captcha) throws Exception {
		log.info(captcha);
		Map resulstMap = new HashMap();
		boolean flag = loginService.checkCaptcha(captcha);
		if (flag){
			resulstMap.put("code",1);
		}else{
			resulstMap.put("code",0);
		}
		return resulstMap;
	}

	@RequestMapping("/login12306")
	public String login12306() {
	//	ticketsService.login12306();
		 return "index";  
	}
	
	@RequestMapping("/query")
	public String query() {
	//	ticketsService.query();
		 return "index";  
	}
	
	@RequestMapping("/buy")
	public String buy() {
	//	ticketsService.buyTicket();
		 return "index";  
	}
	
}
