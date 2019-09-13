package com.cursojunit.orderapi.provider;

import com.cursojunit.orderapi.exception.ControlBalanceException;
import com.cursojunit.orderapi.model.dto.BalanceDTO;
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

@Component
public class ControlBalanceAPIProvider {

    public BalanceDTO createBalance(BalanceDTO balanceDTO) throws ControlBalanceException {
        try{
            String requestBody = JsonUtils.convertToJson(balanceDTO);

            URL url = new URL("http://localhost:8082/balances/");
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
                return (BalanceDTO) JsonUtils.convertToObject(responseJson, BalanceDTO.class);
            } else {
                throw new ControlBalanceException("Connection Error to Control Balance API");
            }
        } catch (Exception e){
            throw new ControlBalanceException("Connection Error to Control Balance API");
        }
    }
}
