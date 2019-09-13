package com.cursojunit.orderapi.repository;

import com.cursojunit.orderapi.entities.public_.tables.records.OrderRecord;
import com.cursojunit.orderapi.model.Order;
import com.cursojunit.orderapi.utils.DateTimeUtils;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

import static com.cursojunit.orderapi.entities.public_.tables.Order.ORDER;

@Repository
public class OrderRepository {

    @Autowired
    private DSLContext dslContext;

    public Integer saveOrder(Order order){
        OrderRecord orderRecord = dslContext.insertInto(ORDER)
                .columns(ORDER.CUSTOMERID,
                        ORDER.AMOUNT,
                        ORDER.SKU,
                        ORDER.QUANTITY,
                        ORDER.VALUE,
                        ORDER.CREATIONDATE)
                .values(order.getCustomerId(),
                        BigDecimal.valueOf(order.getAmount()),
                        order.getSku(),
                        order.getQuantity(),
                        BigDecimal.valueOf(order.getValue()),
                        DateTimeUtils.convertDateToTimestap(order.getCreationDate()))
                .returning(ORDER.ORDERID).fetchOne();
        return orderRecord.getOrderid();
    }

    public Order selectOrder(Integer orderId){
        OrderRecord orderRecord = dslContext.selectFrom(ORDER)
                .where(ORDER.ORDERID.eq(orderId))
                .fetchOne();

        Order order = new Order();
        order.setOrderId(orderRecord.getOrderid());
        order.setAmount(orderRecord.getAmount().doubleValue());
        order.setCustomerId(orderRecord.getCustomerid());
        order.setSku(orderRecord.getSku());
        order.setValue(orderRecord.getValue().doubleValue());
        order.setQuantity(orderRecord.getQuantity());
        order.setCreationDate(orderRecord.getCreationdate());
        return order;
    }
}
