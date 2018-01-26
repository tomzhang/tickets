package com.tickets.tickets.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tickets.tickets.service.TicketsService;

@Controller
@RequestMapping("/ticket")
public class TicketController {
	
	@Autowired
	private TicketsService ticketsService;
	
	@RequestMapping("/index")
	public String index() {
		 return "index";  
	}
	
	
	@RequestMapping("/login12306")
	public String login12306() {
		ticketsService.login12306();
		 return "index";  
	}
	
	@RequestMapping("/query")
	public String query() {
		ticketsService.query();
		 return "index";  
	}
	
	@RequestMapping("/buy")
	public String buy() {
		ticketsService.buyTicket();
		 return "index";  
	}
	
}
