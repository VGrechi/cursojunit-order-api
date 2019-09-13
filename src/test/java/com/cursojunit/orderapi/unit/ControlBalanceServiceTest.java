package com.cursojunit.orderapi.unit;

import com.cursojunit.orderapi.exception.ControlBalanceException;
import com.cursojunit.orderapi.model.Event;
import com.cursojunit.orderapi.model.Order;
import com.cursojunit.orderapi.model.dto.BalanceDTO;
import com.cursojunit.orderapi.provider.ControlBalanceAPIProvider;
import com.cursojunit.orderapi.service.ControlBalanceService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ControlBalanceServiceTest {

    @InjectMocks
    private ControlBalanceService controlBalanceService;

    @Mock
    private ControlBalanceAPIProvider controlBalanceAPIProvider;

    @Test
    public void testGenerateBalance() throws ControlBalanceException {
        // Given
        Order order = new Order();
        order.setOrderId(1);
        order.setAmount(10.0);

        Event event = new Event();
        event.setEventId(1);

        when(controlBalanceAPIProvider.createBalance(any(BalanceDTO.class))).thenReturn(null);

        // When
        // Then
        Assertions.assertDoesNotThrow(() -> controlBalanceService.generateBalance(order, event));
    }
}
