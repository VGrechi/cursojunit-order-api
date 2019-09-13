package com.cursojunit.orderapi.model;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Order {

    private Integer orderId;
    private Double amount;
    private Integer customerId;
    private String sku;
    private Integer quantity;
    private Double value;
    private Date creationDate;

    private List<Event> events;

    /*
    * Getters and Setters
     */
    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(orderId, order.orderId) &&
                Objects.equals(amount, order.amount) &&
                Objects.equals(customerId, order.customerId) &&
                Objects.equals(sku, order.sku) &&
                Objects.equals(quantity, order.quantity) &&
                Objects.equals(value, order.value) &&
                Objects.equals(creationDate, order.creationDate) &&
                Objects.equals(events, order.events);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, amount, customerId, sku, quantity, value, creationDate, events);
    }
}
