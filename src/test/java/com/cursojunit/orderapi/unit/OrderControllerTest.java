package com.cursojunit.orderapi.unit;

import com.cursojunit.orderapi.OrderController;
import com.cursojunit.orderapi.TestUtils;
import com.cursojunit.orderapi.exception.*;
import com.cursojunit.orderapi.model.Order;
import com.cursojunit.orderapi.model.dto.OrderDTO;
import com.cursojunit.orderapi.service.ControlBalanceService;
import com.cursojunit.orderapi.service.OrderService;
import com.cursojunit.orderapi.utils.JsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderService orderService;

    private MockMvc mockMvc;

    @BeforeEach
    public void init(){
        mockMvc = MockMvcBuilders.standaloneSetup(orderController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    public void test01() throws Exception {
        // Given
        OrderDTO orderDTO = this.mockOrderDTO();

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("http://localhost:8080/orders/")
                .content(JsonUtils.convertToJson(orderDTO))
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        // When
        MvcResult result = mockMvc.perform(request).andReturn();

        //Then
        int status = result.getResponse().getStatus();
        Assertions.assertEquals(HttpStatus.CREATED.value(), status);

        String contentAsString = result.getResponse().getContentAsString();
        Order order = (Order) JsonUtils.convertToObject(contentAsString, Order.class);
        Assertions.assertNotNull(order);
    }

    @Test
    public void test02() throws Exception {
        // Given
        OrderDTO orderDTO = this.mockInvalidOrderDTO();

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("http://localhost:8080/orders/")
                .content(JsonUtils.convertToJson(orderDTO))
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        // When
        MvcResult result = mockMvc.perform(request).andReturn();

        //Then
        int status = result.getResponse().getStatus();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), status);

        String contentAsString = result.getResponse().getContentAsString();
        ResponseError responseError = (ResponseError) JsonUtils.convertToObject(contentAsString, ResponseError.class);
        Assertions.assertNotNull(responseError);
    }

    @Test
    public void test03() throws Exception {
        // Given
        OrderDTO orderDTO = this.mockOrderDTO();

        when(orderService.createOrder(any(OrderDTO.class))).thenThrow(OrderException.class);
//        when(orderService.createOrder(any(OrderDTO.class))).thenThrow(PaymentException.class);
//        when(orderService.createOrder(any(OrderDTO.class))).thenThrow(ControlBalanceException.class);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("http://localhost:8080/orders/")
                .content(JsonUtils.convertToJson(orderDTO))
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        // When
        MvcResult result = mockMvc.perform(request).andReturn();

        //Then
        int status = result.getResponse().getStatus();
        Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), status);

        String contentAsString = result.getResponse().getContentAsString();
        ResponseError responseError = (ResponseError) JsonUtils.convertToObject(contentAsString, ResponseError.class);
        Assertions.assertNotNull(responseError);
        Assertions.assertEquals("Product Value or Quantity is invalid.", responseError.getMessage());

    }

    /*
     * Mocks
     */
    private OrderDTO mockOrderDTO(){
        String path = TestUtils.WORK_PATH + "/controller/ordercontroller/orderdto.json";
        return TestUtils.convertJsonToPOJO(path, OrderDTO.class);
    }

    private OrderDTO mockInvalidOrderDTO(){
        String path = TestUtils.WORK_PATH + "/controller/ordercontroller/orderdto-invalid.json";
        return TestUtils.convertJsonToPOJO(path, OrderDTO.class);
    }
}
