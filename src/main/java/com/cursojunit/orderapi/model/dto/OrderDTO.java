package com.cursojunit.orderapi.model.dto;

import com.cursojunit.orderapi.model.PaymentMethod;

import java.util.List;

public class OrderDTO {

    private Integer orderId;
    private Double amount;
    private Integer customerId;
    private String sku;
    private Integer quantity;
    private Double value;
    private List<PaymentMethod> paymentMethods;


    /*
    * Getters and Setters
     */

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

    public List<PaymentMethod> getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(List<PaymentMethod> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public Integer getOrderId() {
        return orderId;
    }
}
