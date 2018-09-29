package com.tickets.tickets.service;

import com.tickets.tickets.domain.PassengerVO;

import java.util.List;

public interface TicketPassengerService {

    public List<PassengerVO> getPassengerDTOs();
}
