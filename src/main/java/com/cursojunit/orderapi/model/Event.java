package com.cursojunit.orderapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.List;

public class Event {

    private Integer eventId;
    private String eventType;
    private Double amount;
    private Date creationDate;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Order order;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<PaymentMethod> paymentMethods;

    /*
    * Getters and Setters
     */
    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<PaymentMethod> getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(List<PaymentMethod> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }
}
