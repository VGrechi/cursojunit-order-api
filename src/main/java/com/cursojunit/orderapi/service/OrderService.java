package com.cursojunit.orderapi.service;

import com.cursojunit.orderapi.exception.ControlBalanceException;
import com.cursojunit.orderapi.exception.OrderException;
import com.cursojunit.orderapi.exception.PaymentException;
import com.cursojunit.orderapi.model.Event;
import com.cursojunit.orderapi.model.Order;
import com.cursojunit.orderapi.model.PaymentMethod;
import com.cursojunit.orderapi.model.dto.OrderDTO;
import com.cursojunit.orderapi.repository.EventRepository;
import com.cursojunit.orderapi.repository.OrderRepository;
import com.cursojunit.orderapi.repository.PaymentMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Component
public class OrderService {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ControlBalanceService controlBalanceService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    public Order createOrder(OrderDTO orderDTO) throws OrderException, PaymentException, ControlBalanceException {

        double total = orderDTO.getValue() * orderDTO.getQuantity();
        if(total != orderDTO.getAmount()){
            throw new OrderException("Product Value or Quantity is invalid.");
        }

        double sum = orderDTO.getPaymentMethods().stream().mapToDouble(PaymentMethod::getPaymentAmount).sum();
        if(sum != orderDTO.getAmount()){
            throw new OrderException("Payment Amount is invalid.");
        }

        // Persist Order
        Order order = new Order();
        order.setCustomerId(orderDTO.getCustomerId());
        order.setAmount(orderDTO.getAmount());
        order.setSku(orderDTO.getSku());
        order.setValue(orderDTO.getValue());
        order.setQuantity(orderDTO.getQuantity());
        order.setCreationDate(new Date());
        Integer orderId = orderRepository.saveOrder(order);
        order.setOrderId(orderId);


        // Persist Event
        Event event = new Event();
        event.setEventType("PURCHASE");
        event.setAmount(orderDTO.getAmount());
        event.setCreationDate(new Date());
        Integer eventId = eventRepository.saveEvent(orderId, event);
        event.setEventId(eventId);

        List<Event> eventList = new ArrayList<>();
        eventList.add(event);
        order.setEvents(eventList);

        // Persist PaymentMethods
        orderDTO.getPaymentMethods().forEach(paymentMethod -> {
            paymentMethodRepository.savePaymentMethod(eventId, paymentMethod);
        });

        // Process Payment
        paymentService.payOrder(orderDTO, orderId, event);

        //Generate Balance
        controlBalanceService.generateBalance(order, event);

        return order;
    }


    public Order getOrder(Integer orderId) {
        Order order = orderRepository.selectOrder(orderId);

        List<Event> eventList = eventRepository.selectEventByOrderId(orderId);

        eventList.forEach(event -> {
                List<PaymentMethod> paymentMethods = paymentMethodRepository.selectPaymentMethodByEventId(event.getEventId());
                event.setPaymentMethods(paymentMethods);
            });

        order.setEvents(eventList);
        return order;
    }


    public Order rechargeOrder(Integer orderId, OrderDTO order) {

        return new Order();
    }
}
