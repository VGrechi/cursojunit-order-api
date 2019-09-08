package com.cursojunit.orderapi.utils;

import com.cursojunit.orderapi.model.*;
import com.cursojunit.orderapi.model.dto.OrderDTO;

import java.util.ArrayList;
import java.util.List;

public class ValidationUtils {

    private static List<String> requestErrors = new ArrayList<>();

    public static boolean isOrderValid(OrderDTO orderDTO){
        requestErrors.clear();
        if(orderDTO.getAmount() == null || orderDTO.getAmount() <= 0) requestErrors.add("Amount is missing.");
        if(orderDTO.getCustomerId() == null || orderDTO.getCustomerId() <= 0) requestErrors.add("CustomerId is missing.");
        if(orderDTO.getSku() == null || orderDTO.getSku().trim().isEmpty()) requestErrors.add("Sku is missing.");
        if(orderDTO.getQuantity() == null || orderDTO.getQuantity() <= 0) requestErrors.add("Quantity is missing.");
        if(orderDTO.getValue() == null || orderDTO.getValue() <= 0) requestErrors.add("Value is missing.");

        if(orderDTO.getPaymentMethods() == null || orderDTO.getPaymentMethods().size() == 0) requestErrors.add("Payment Method List is missing.");
        orderDTO.getPaymentMethods().stream().forEach(paymentMethod -> isPaymentMethodValid(paymentMethod));

        return requestErrors.size() == 0;
    }

    private static void isPaymentMethodValid(PaymentMethod paymentMethod){
        if(paymentMethod.getPaymentMethod() == null || paymentMethod.getPaymentMethod().trim().isEmpty()) requestErrors.add("Payment Method is missing.");
        if(paymentMethod.getPaymentAmount() == null || paymentMethod.getPaymentAmount() == 0) requestErrors.add("Payment Amount is missing.");
        if(paymentMethod.getPaymentMethod().equals("CREDIT CARD")) {
            if(paymentMethod.getCreditCard() == null) requestErrors.add("Credit Card is missing.");
            else isCreditCardValid(paymentMethod.getCreditCard());
        }
    }

    private static void isCreditCardValid(CreditCard creditCard){
        if(creditCard.getCardHolderName() == null || creditCard.getCardHolderName().trim().isEmpty()) requestErrors.add("Card Holder Name is missing.");
        if(creditCard.getBrand() == null || creditCard.getBrand().trim().isEmpty()) requestErrors.add("Brand is missing.");
        if(creditCard.getCardNumber() == null || creditCard.getCardNumber().trim().isEmpty()) requestErrors.add("Card Number is missing.");
        if(creditCard.getExpirationDate() == null || creditCard.getExpirationDate().trim().isEmpty()) requestErrors.add("Expiration Date is missing.");
        if(creditCard.getCvv() == null || creditCard.getCvv() <= 0) requestErrors.add("CVV is missing.");

        if(creditCard.getExpirationDate().replace("/", "").length() != 6) requestErrors.add("Expiration Date is invalid.");
        if(creditCard.getCvv().toString().length() != 3) requestErrors.add("CVV is invalid.");
    }
}
