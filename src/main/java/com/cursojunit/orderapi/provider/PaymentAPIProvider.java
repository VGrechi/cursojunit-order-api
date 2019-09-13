package com.cursojunit.orderapi.provider;

import com.cursojunit.orderapi.exception.PaymentException;
import com.cursojunit.orderapi.model.dto.AllowedPaymentMethodDTO;
import com.cursojunit.orderapi.model.dto.PaymentDTO;
import com.cursojunit.orderapi.utils.JsonUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class PaymentAPIProvider {

    public List<AllowedPaymentMethodDTO> getPaymentMethods() throws PaymentException {
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

    public PaymentDTO processPayment(PaymentDTO paymentDTO) throws PaymentException {
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
