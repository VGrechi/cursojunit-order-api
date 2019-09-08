package com.cursojunit.orderapi.service;

import com.cursojunit.orderapi.entities.public_.tables.records.EventRecord;
import com.cursojunit.orderapi.entities.public_.tables.records.OrderRecord;
import com.cursojunit.orderapi.entities.public_.tables.records.PaymentmethodRecord;
import com.cursojunit.orderapi.exception.ControlBalanceException;
import com.cursojunit.orderapi.exception.OrderException;
import com.cursojunit.orderapi.exception.PaymentException;
import com.cursojunit.orderapi.model.Event;
import com.cursojunit.orderapi.model.Order;
import com.cursojunit.orderapi.model.PaymentMethod;
import com.cursojunit.orderapi.model.dto.OrderDTO;
import com.cursojunit.orderapi.utils.DateTimeUtils;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.cursojunit.orderapi.entities.public_.tables.Event.EVENT;
import static com.cursojunit.orderapi.entities.public_.tables.Order.ORDER;
import static com.cursojunit.orderapi.entities.public_.tables.Paymentmethod.PAYMENTMETHOD;

@Service
public class OrderService {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ControlBalanceService controlBalanceService;

    @Autowired
    private DSLContext dslContext;

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
        Integer orderId = this.saveOrder(order);
        order.setOrderId(orderId);


        // Persist Event
        Event event = new Event();
        event.setEventType("PURCHASE");
        event.setAmount(orderDTO.getAmount());
        event.setCreationDate(new Date());
        Integer eventId = this.saveEvent(orderId, event);
        event.setEventId(eventId);

        List<Event> eventList = new ArrayList<>();
        eventList.add(event);
        order.setEvents(eventList);

        // Persist PaymentMethods
        orderDTO.getPaymentMethods().forEach(paymentMethod -> {
            Integer paymentMethodId = this.savePaymentMethod(eventId, paymentMethod);
            paymentMethod.setPaymentMethodId(paymentMethodId);
        });

        // Process Payment
        paymentService.payOrder(orderDTO, orderId, event);

        //Generate Balance
        controlBalanceService.generateBalance(order, event);

        return order;
    }

    private Integer savePaymentMethod(Integer eventId, PaymentMethod paymentMethod){
        PaymentmethodRecord paymentmethodRecord = dslContext.insertInto(PAYMENTMETHOD)
                .columns(PAYMENTMETHOD.EVENTID,
                        PAYMENTMETHOD.PAYMENTMETHOD_,
                        PAYMENTMETHOD.PAYMENTAMOUNT)
                .values(eventId,
                        paymentMethod.getPaymentMethod(),
                        BigDecimal.valueOf(paymentMethod.getPaymentAmount()))
                .returning(PAYMENTMETHOD.PAYMENTMETHODID).fetchOne();
        return paymentmethodRecord.getPaymentmethodid();
    }

    private Integer saveEvent(Integer orderId, Event event) {
        EventRecord eventRecord = dslContext.insertInto(EVENT)
                .columns(EVENT.ORDERID,
                        EVENT.EVENTTYPE,
                        EVENT.AMOUNT,
                        EVENT.CREATIONDATE)
                .values(orderId,
                        event.getEventType(),
                        BigDecimal.valueOf(event.getAmount()),
                        DateTimeUtils.convertDateToTimestap(event.getCreationDate()))
                .returning(EVENT.EVENTID).fetchOne();
        return eventRecord.getEventid();
    }

    private Integer saveOrder(Order order){
        OrderRecord orderRecord = dslContext.insertInto(ORDER)
                .columns(ORDER.CUSTOMERID,
                        ORDER.AMOUNT,
                        ORDER.SKU,
                        ORDER.QUANTITY,
                        ORDER.VALUE,
                        ORDER.CREATIONDATE)
                .values(order.getCustomerId(),
                        BigDecimal.valueOf(order.getAmount()),
                        order.getSku(),
                        order.getQuantity(),
                        BigDecimal.valueOf(order.getValue()),
                        DateTimeUtils.convertDateToTimestap(order.getCreationDate()))
                .returning(ORDER.ORDERID).fetchOne();
        return orderRecord.getOrderid();
    }

    public Order getOrder(Integer orderId) {
        Order order = this.selectOrder(orderId);

        List<Event> eventList = this.selectEventByOrderId(orderId);

        eventList.forEach(event -> {
                List<PaymentMethod> paymentMethods = this.selectPaymentMethodByEventId(event.getEventId());
                event.setPaymentMethods(paymentMethods);
            });

        order.setEvents(eventList);
        return order;
    }

    private Order selectOrder(Integer orderId){
        OrderRecord orderRecord = dslContext.selectFrom(ORDER)
                .where(ORDER.ORDERID.eq(orderId))
                .fetchOne();

        Order order = new Order();
        order.setOrderId(orderRecord.getOrderid());
        order.setAmount(orderRecord.getAmount().doubleValue());
        order.setCustomerId(orderRecord.getCustomerid());
        order.setSku(orderRecord.getSku());
        order.setValue(orderRecord.getValue().doubleValue());
        order.setQuantity(orderRecord.getQuantity());
        order.setCreationDate(orderRecord.getCreationdate());
        return order;
    }

    private List<Event> selectEventByOrderId(Integer orderId){
        return dslContext.selectFrom(EVENT)
                .where(EVENT.ORDERID.eq(orderId))
                .fetch().map(mapper -> {
                    Event event = new Event();
                    event.setEventId(mapper.get(EVENT.EVENTID));
                    event.setEventType(mapper.get(EVENT.EVENTTYPE));
                    event.setAmount(mapper.get(EVENT.AMOUNT).doubleValue());
                    event.setCreationDate(mapper.get(EVENT.CREATIONDATE));
                    return event;
                });
    }

    private List<PaymentMethod> selectPaymentMethodByEventId(Integer eventId){
        return dslContext.selectFrom(PAYMENTMETHOD)
                .where(PAYMENTMETHOD.EVENTID.eq(eventId))
                .fetch().map(mapper -> {
                    PaymentMethod paymentMethod = new PaymentMethod();
                    paymentMethod.setPaymentMethodId(mapper.get(PAYMENTMETHOD.PAYMENTMETHODID));
                    paymentMethod.setPaymentMethod(mapper.get(PAYMENTMETHOD.PAYMENTMETHOD_));
                    paymentMethod.setPaymentAmount(mapper.getPaymentamount().doubleValue());
                    return paymentMethod;
                });
    }
}
