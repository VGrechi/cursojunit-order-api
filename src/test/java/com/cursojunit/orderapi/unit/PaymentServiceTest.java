package com.cursojunit.orderapi.unit;

import com.cursojunit.orderapi.TestUtils;
import com.cursojunit.orderapi.exception.PaymentException;
import com.cursojunit.orderapi.model.Event;
import com.cursojunit.orderapi.model.dto.AllowedPaymentMethodDTO;
import com.cursojunit.orderapi.model.dto.OrderDTO;
import com.cursojunit.orderapi.model.dto.PaymentDTO;
import com.cursojunit.orderapi.provider.PaymentAPIProvider;
import com.cursojunit.orderapi.service.PaymentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private PaymentAPIProvider paymentAPIProvider;

    private Double bankslipMaxValue;

    @BeforeEach
    public void setUp(){
        bankslipMaxValue = 500.0;
        ReflectionTestUtils.setField(paymentService, "bankslipMaxValue", bankslipMaxValue);
    }

    @Test
    public void testOrderPaidSuccessCreditCardVisa() throws PaymentException {
        // Given
        OrderDTO orderDTO = this.getOrderDTOCreditCardVisaMock();
        Event event = this.getEventMock();

        //ReflectionTestUtils.setField(paymentService, "bankslipMaxValue", bankslipMaxValue);
        List<AllowedPaymentMethodDTO> allowedPaymentMethodMock  = this.getAllowedPaymentMethodsMock();
        when(paymentAPIProvider.getPaymentMethods()).thenReturn(allowedPaymentMethodMock);
        when(paymentAPIProvider.processPayment(any(PaymentDTO.class))).thenReturn(null);

        // When
        // Then
        Assertions.assertDoesNotThrow(() -> paymentService.payOrder(orderDTO, 1, event));
    }

    @Test
    public void testValidBankslipValue() throws PaymentException {
        // Given
        OrderDTO orderDTO = this.getOrderDTOValidBankslipValueMock();
        Event event = this.getEventMock();

        List<AllowedPaymentMethodDTO> allowedPaymentMethodMock  = this.getAllowedPaymentMethodsMock();
        when(paymentAPIProvider.getPaymentMethods()).thenReturn(allowedPaymentMethodMock);
        when(paymentAPIProvider.processPayment(any(PaymentDTO.class))).thenReturn(null);

        // When
        // Then
        Assertions.assertDoesNotThrow(() -> paymentService.payOrder(orderDTO, 1, event));
    }

    @Test
    public void testInvalidPaymentMethod() throws PaymentException {
        // Given
        OrderDTO orderDTO = this.getOrderDTOInvalidPaymentMethodMock();
        Event event = this.getEventMock();

        List<AllowedPaymentMethodDTO> allowedPaymentMethodMock  = this.getAllowedPaymentMethodsMock();
        when(paymentAPIProvider.getPaymentMethods()).thenReturn(allowedPaymentMethodMock);

        // When
        // Then
        Assertions.assertThrows(PaymentException.class,
                () -> paymentService.payOrder(orderDTO, 1, event),
                "Invalid Payment method");
    }

    @Test
    public void testInvalidPaymentMethodBrand() throws PaymentException {
        // Given
        OrderDTO orderDTO = this.getOrderDTOCreditCardEloMock();
        Event event = this.getEventMock();

        List<AllowedPaymentMethodDTO> allowedPaymentMethodMock  = this.getAllowedPaymentMethodsMock();
        when(paymentAPIProvider.getPaymentMethods()).thenReturn(allowedPaymentMethodMock);

        // When
        // Then
        Assertions.assertThrows(PaymentException.class,
                () -> paymentService.payOrder(orderDTO, 1, event),
                "Invalid Payment method");
    }

    @Test
    public void testInvalidBankslipValue() throws PaymentException {
        // Given
        OrderDTO orderDTO = this.getOrderDTOInvalidBankslipValueMock();
        Event event = this.getEventMock();

        List<AllowedPaymentMethodDTO> allowedPaymentMethodMock  = this.getAllowedPaymentMethodsMock();
        when(paymentAPIProvider.getPaymentMethods()).thenReturn(allowedPaymentMethodMock);

        // When
        // Then
        Assertions.assertThrows(PaymentException.class,
                () -> paymentService.payOrder(orderDTO, 1, event),
                String.format("Bankslip Payment Method shouldn't be greater than %s", bankslipMaxValue));
    }

    /*
    * Mocks
     */
    private OrderDTO getOrderDTOCreditCardVisaMock(){
        String path = TestUtils.WORK_PATH + "/service/paymentservice/orderdto-creditcard-visa.json";
        return TestUtils.convertJsonToPOJO(path, OrderDTO.class);
    }

    private OrderDTO getOrderDTOCreditCardEloMock(){
        String path = TestUtils.WORK_PATH + "/service/paymentservice/orderdto-creditcard-elo.json";
        return TestUtils.convertJsonToPOJO(path, OrderDTO.class);
    }

    private Event getEventMock(){
        String path = TestUtils.WORK_PATH + "/service/paymentservice/event.json";
        return TestUtils.convertJsonToPOJO(path, Event.class);
    }

    private OrderDTO getOrderDTOInvalidPaymentMethodMock(){
        String path = TestUtils.WORK_PATH + "/service/paymentservice/orderdto-invalidpaymentmethod.json";
        return TestUtils.convertJsonToPOJO(path, OrderDTO.class);
    }

    private OrderDTO getOrderDTOInvalidBankslipValueMock(){
        String path = TestUtils.WORK_PATH + "/service/paymentservice/orderdto-invalidbankslipvalue.json";
        return TestUtils.convertJsonToPOJO(path, OrderDTO.class);
    }

    private OrderDTO getOrderDTOValidBankslipValueMock(){
        String path = TestUtils.WORK_PATH + "/service/paymentservice/orderdto-validbankslipvalue.json";
        return TestUtils.convertJsonToPOJO(path, OrderDTO.class);
    }

    @SuppressWarnings("unchecked")
    private List<AllowedPaymentMethodDTO> getAllowedPaymentMethodsMock(){
        String path = TestUtils.WORK_PATH + "/service/paymentservice/allowedpaymentmethods.json";
        return TestUtils.convertJsonToList(path, AllowedPaymentMethodDTO.class);
    }
}

