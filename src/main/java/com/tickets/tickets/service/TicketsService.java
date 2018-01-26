package com.tickets.tickets.service;

import com.tickets.tickets.domain.TrainInfoVO;

public interface TicketsService {
	
	
	public void login12306();
	
	//public Object 
	
	public TrainInfoVO query();
	
	public void buyTicket();
	
}
