package com.cursojunit.orderapi.unit;

import com.cursojunit.orderapi.TestUtils;
import com.cursojunit.orderapi.exception.ControlBalanceException;
import com.cursojunit.orderapi.exception.OrderException;
import com.cursojunit.orderapi.exception.PaymentException;
import com.cursojunit.orderapi.model.Order;
import com.cursojunit.orderapi.model.dto.OrderDTO;
import com.cursojunit.orderapi.service.ControlBalanceService;
import com.cursojunit.orderapi.service.OrderService;
import com.cursojunit.orderapi.service.PaymentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private PaymentService paymentService;

    @Mock
    private ControlBalanceService controlBalanceService;

    @Test
    public void test01() throws PaymentException, OrderException, ControlBalanceException {
        // Given
        String path = TestUtils.WORK_PATH + "/service/orderservice/orderdto.json";
        OrderDTO orderDTO = TestUtils.convertJsonToPOJO(path, OrderDTO.class);

        // When
        Order order = orderService.createOrder(orderDTO);

        // Then
        Assertions.assertNotNull(order);
    }
}
