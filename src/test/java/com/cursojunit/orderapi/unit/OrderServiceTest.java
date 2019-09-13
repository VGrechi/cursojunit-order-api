package com.cursojunit.orderapi.unit;

import com.cursojunit.orderapi.TestUtils;
import com.cursojunit.orderapi.exception.ControlBalanceException;
import com.cursojunit.orderapi.exception.OrderException;
import com.cursojunit.orderapi.exception.PaymentException;
import com.cursojunit.orderapi.model.Event;
import com.cursojunit.orderapi.model.Order;
import com.cursojunit.orderapi.model.PaymentMethod;
import com.cursojunit.orderapi.model.dto.OrderDTO;
import com.cursojunit.orderapi.repository.EventRepository;
import com.cursojunit.orderapi.repository.OrderRepository;
import com.cursojunit.orderapi.repository.PaymentMethodRepository;
import com.cursojunit.orderapi.service.ControlBalanceService;
import com.cursojunit.orderapi.service.OrderService;
import com.cursojunit.orderapi.service.PaymentService;
import org.jooq.DSLContext;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration("/src/test/resources/application-test.properties")
public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private PaymentService paymentService;

    @Mock
    private ControlBalanceService controlBalanceService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private PaymentMethodRepository paymentMethodRepository;

    @Test
    public void testCreateOrderSuccessfully() throws PaymentException, OrderException, ControlBalanceException {
        // Given
        String path = TestUtils.WORK_PATH + "/service/orderservice/orderdto.json";
        OrderDTO orderDTO = TestUtils.convertJsonToPOJO(path, OrderDTO.class);

        when(orderRepository.saveOrder(any(Order.class))).thenReturn(16);
        when(eventRepository.saveEvent(anyInt(), any(Event.class))).thenReturn(19);
        when(paymentMethodRepository.savePaymentMethod(anyInt(), any(PaymentMethod.class))).thenReturn(20);
        doNothing().when(paymentService).payOrder(any(OrderDTO.class), anyInt(), any(Event.class));
        doNothing().when(controlBalanceService).generateBalance(any(Order.class), any(Event.class));

        // When
        Order order = orderService.createOrder(orderDTO);

        // Then
        assertNotNull(order);
        assertNotNull(order.getEvents());
        assertFalse(order.getEvents().isEmpty());

        order.getEvents().forEach(event -> assertNotNull(event.getEventId()));
    }

    @Test
    public void testCreateOrderWithInvalidAmount() {
        // Given
        String path = TestUtils.WORK_PATH + "/service/orderservice/orderdto-invalidamount.json";
        OrderDTO orderDTO = TestUtils.convertJsonToPOJO(path, OrderDTO.class);

        // When
        // Then
        assertThrows(OrderException.class, () -> orderService.createOrder(orderDTO));
    }

    @Test
    public void testCreateOrderWithInvalidProduct() {
        // Given
        String path = TestUtils.WORK_PATH + "/service/orderservice/orderdto-invalidproduct.json";
        OrderDTO orderDTO = TestUtils.convertJsonToPOJO(path, OrderDTO.class);

        // When
        // Then
        assertThrows(OrderException.class, () -> orderService.createOrder(orderDTO));
    }

    @Test
    public void testRechargeOrderSuccessfully(){
        // Given
        String path = TestUtils.WORK_PATH + "/service/orderservice/orderdto-recharge.json";
        OrderDTO orderDTO = TestUtils.convertJsonToPOJO(path, OrderDTO.class);

        // When
        Order rechargeOrder = orderService.rechargeOrder(orderDTO.getOrderId(), orderDTO);

        // Then
        Assertions.assertNotNull(rechargeOrder);
        Assertions.assertNotNull(rechargeOrder.getEvents());
        Assertions.assertFalse(rechargeOrder.getEvents().isEmpty());

        rechargeOrder.getEvents().forEach(event -> {
            Assertions.assertNotNull(event);
            Assertions.assertEquals(20, event.getAmount());
        });

    }
}
