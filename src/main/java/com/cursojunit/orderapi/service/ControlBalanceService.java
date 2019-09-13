package com.cursojunit.orderapi.service;

import com.cursojunit.orderapi.exception.ControlBalanceException;
import com.cursojunit.orderapi.model.Event;
import com.cursojunit.orderapi.model.Order;
import com.cursojunit.orderapi.model.dto.BalanceDTO;
import com.cursojunit.orderapi.provider.ControlBalanceAPIProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ControlBalanceService {

    @Autowired
    private ControlBalanceAPIProvider controlBalanceAPIProvider;

    public void generateBalance(Order order, Event event) throws ControlBalanceException {
        BalanceDTO balanceDTO = new BalanceDTO();
        balanceDTO.setOrderId(order.getOrderId());
        balanceDTO.setEventId(event.getEventId());
        balanceDTO.setBalanceValue(order.getAmount());
        controlBalanceAPIProvider.createBalance(balanceDTO);
    }


}
