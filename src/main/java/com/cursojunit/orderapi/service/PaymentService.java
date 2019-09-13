package com.cursojunit.orderapi.service;

import com.cursojunit.orderapi.exception.PaymentException;
import com.cursojunit.orderapi.model.Event;
import com.cursojunit.orderapi.model.dto.AllowedPaymentMethodDTO;
import com.cursojunit.orderapi.model.dto.OrderDTO;
import com.cursojunit.orderapi.model.dto.PaymentDTO;
import com.cursojunit.orderapi.provider.PaymentAPIProvider;
import com.cursojunit.orderapi.utils.JsonUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class PaymentService {

    @Value("${order.api.bankslip.max.value}")
    private Double bankslipMaxValue;

    @Autowired
    private PaymentAPIProvider paymentAPIProvider;

    public void payOrder(OrderDTO orderDTO, Integer orderId, Event event) throws PaymentException {
        List<AllowedPaymentMethodDTO> paymentMethods = paymentAPIProvider.getPaymentMethods();
        boolean isValid = this.validatePaymentMethod(orderDTO, paymentMethods);
        if(!isValid)
            throw new PaymentException("Invalid Payment method");

        boolean validBankslipValue = this.validateBankslipMaxValue(orderDTO);
        if(!validBankslipValue)
            throw new PaymentException(String.format("Bankslip Payment Method shouldn't be greater than %s", bankslipMaxValue));

        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setOrderId(orderId);
        paymentDTO.setEventId(event.getEventId());
        paymentDTO.setTotalAmount(orderDTO.getAmount());
        paymentDTO.setPaymentMethods(orderDTO.getPaymentMethods());
        paymentAPIProvider.processPayment(paymentDTO);
    }



    private boolean validatePaymentMethod(OrderDTO orderDTO, List<AllowedPaymentMethodDTO> allowedPaymentMethodDTOS){
        AtomicBoolean valid = new AtomicBoolean(false);
        orderDTO.getPaymentMethods().forEach(paymentMethod -> {
            boolean match = allowedPaymentMethodDTOS.stream()
                    .anyMatch(apm -> {
                        if(paymentMethod.getPaymentMethod().equals("CREDIT CARD")){
                            return paymentMethod.getPaymentMethod().equals(apm.getName())
                                    && paymentMethod.getCreditCard().getBrand().equals(apm.getBrand());
                        }else{
                            return paymentMethod.getPaymentMethod().equals(apm.getName());
                        }

                    });
            valid.set(match);
        });
        return valid.get();
    }

    private boolean validateBankslipMaxValue(OrderDTO orderDTO) throws PaymentException{
        AtomicBoolean valid = new AtomicBoolean(true);
        orderDTO.getPaymentMethods().stream()
                .filter(paymentMethod -> paymentMethod.getPaymentMethod().equals("BANKSLIP"))
                .findAny()
                .ifPresent(paymentMethod -> {
                    if(paymentMethod.getPaymentAmount() > bankslipMaxValue){
                        valid.set(false);
                    }
                });
        return valid.get();
    }


}
