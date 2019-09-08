package com.cursojunit.orderapi.service;

import com.cursojunit.orderapi.exception.PaymentException;
import com.cursojunit.orderapi.model.Event;
import com.cursojunit.orderapi.model.dto.AllowedPaymentMethodDTO;
import com.cursojunit.orderapi.model.dto.OrderDTO;
import com.cursojunit.orderapi.model.dto.PaymentDTO;
import com.cursojunit.orderapi.utils.JsonUtils;
import org.apache.commons.io.IOUtils;
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

    public void payOrder(OrderDTO orderDTO, Integer orderId, Event event) throws PaymentException {
        List<AllowedPaymentMethodDTO> paymentMethods = this.getPaymentMethods();
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
        this.processPayment(paymentDTO);
    }

    private List<AllowedPaymentMethodDTO> getPaymentMethods() throws PaymentException {
        try{
            URL url = new URL("http://localhost:8081/paymentmethods/allowed");
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestMethod(HttpMethod.GET.toString());

            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String responseJson = IOUtils.toString(httpConnection.getInputStream(), StandardCharsets.UTF_8);
                return (List<AllowedPaymentMethodDTO>) JsonUtils.convertToList(responseJson, AllowedPaymentMethodDTO.class);
            } else {
                throw new PaymentException("Connection Error to Payment API");
            }
        }catch (Exception e){
            throw new PaymentException("Connection Error to Payment API");
        }
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

    private PaymentDTO processPayment(PaymentDTO paymentDTO) throws PaymentException {
        try{
            String requestBody = JsonUtils.convertToJson(paymentDTO);

            URL url = new URL("http://localhost:8081/payments/");
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestMethod(HttpMethod.POST.toString());
            httpConnection.setRequestProperty(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            httpConnection.setDoOutput(true);

            OutputStream outputStream = httpConnection.getOutputStream();
            outputStream.write(requestBody.getBytes());
            outputStream.flush();
            outputStream.close();

            httpConnection.connect();

            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String responseJson = IOUtils.toString(httpConnection.getInputStream(), StandardCharsets.UTF_8);
                return (PaymentDTO) JsonUtils.convertToObject(responseJson, PaymentDTO.class);
            } else {
                throw new PaymentException("Connection Error to Payment API");
            }
        }catch (Exception e){
            throw new PaymentException("Connection Error to Payment API");
        }
    }
}
