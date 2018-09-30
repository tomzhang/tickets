package com.tickets.tickets.service;

import com.tickets.tickets.domain.TrainInfoVO;

public interface TicketService {
    public TrainInfoVO query(String fromStation,String fromStation_code,
                             String toStation,String toStation_code,
                             String train_date);


}
