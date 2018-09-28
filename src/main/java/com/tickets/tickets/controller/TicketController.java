package com.tickets.tickets.controller;

import com.tickets.tickets.domain.TrainInfoVO;
import com.tickets.tickets.service.LoginService;
import com.tickets.tickets.service.TicketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tickets.tickets.service.TicketsService;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/ticket")
@Slf4j
public class TicketController {

	@Autowired
	private LoginService loginService;
	@Autowired
	private TicketService ticketService;
	
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

	@RequestMapping("/checkCaptcha")
	@ResponseBody
	public Map checkCaptcha(String captcha) throws Exception {
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

	@RequestMapping("/doLogin")
	@ResponseBody
	public Map doLogin(String username, String password, HttpSession session) throws Exception {
		Map map  = loginService.doLogin(username,password);
		session.setAttribute("username",username);
		session.setAttribute("password",password);
		session.setAttribute("usernames",map.get("usernames"));
		return map;
	}

	@RequestMapping("/toTicket")
	public String toTicket(){
		return "ticket";
	}

	@RequestMapping("/queryTrains")
	@ResponseBody
	public TrainInfoVO queryTrains(String fromStation,String fromStation_code,
								   String toStation,String toStation_code,
								   String train_date){
		log.info( fromStation+" "+fromStation_code+" "+toStation+" "+toStation_code+" "+train_date);
		return ticketService.query( fromStation, fromStation_code,toStation, toStation_code, train_date);
	}

}
