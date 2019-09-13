package com.cursojunit.orderapi;

import com.cursojunit.orderapi.exception.ControlBalanceException;
import com.cursojunit.orderapi.exception.OrderException;
import com.cursojunit.orderapi.exception.PaymentException;
import com.cursojunit.orderapi.model.Order;
import com.cursojunit.orderapi.model.dto.OrderDTO;
import com.cursojunit.orderapi.service.ControlBalanceService;
import com.cursojunit.orderapi.service.OrderService;
import com.cursojunit.orderapi.service.PaymentService;
import com.cursojunit.orderapi.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> postOrder(@RequestBody OrderDTO orderDTO) throws OrderException, PaymentException, ControlBalanceException {

        boolean orderValid = ValidationUtils.isOrderValid(orderDTO);
        if(!orderValid){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        //

        Order order = orderService.createOrder(orderDTO);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> getOrder(@PathVariable Integer orderId){
        if(orderId == null || orderId <= 0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Order order = orderService.getOrder(orderId);

        if(order != null){
            return new ResponseEntity<>(order, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(value = "/{orderId}/recharge", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> postOrder(@PathVariable Integer orderId, @RequestBody OrderDTO orderDTO) {
        orderService.rechargeOrder(orderId, orderDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
