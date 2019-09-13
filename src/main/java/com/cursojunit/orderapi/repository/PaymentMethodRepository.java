package com.cursojunit.orderapi.repository;

import com.cursojunit.orderapi.entities.public_.tables.records.PaymentmethodRecord;
import com.cursojunit.orderapi.model.PaymentMethod;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

import static com.cursojunit.orderapi.entities.public_.tables.Paymentmethod.PAYMENTMETHOD;

@Repository
public class PaymentMethodRepository {

    @Autowired
    private DSLContext dslContext;

    public Integer savePaymentMethod(Integer eventId, PaymentMethod paymentMethod){
        PaymentmethodRecord paymentmethodRecord = dslContext.insertInto(PAYMENTMETHOD)
                .columns(PAYMENTMETHOD.EVENTID,
                        PAYMENTMETHOD.PAYMENTMETHOD_,
                        PAYMENTMETHOD.PAYMENTAMOUNT)
                .values(eventId,
                        paymentMethod.getPaymentMethod(),
                        BigDecimal.valueOf(paymentMethod.getPaymentAmount()))
                .returning(PAYMENTMETHOD.PAYMENTMETHODID).fetchOne();
        return paymentmethodRecord.getPaymentmethodid();
    }

    public List<PaymentMethod> selectPaymentMethodByEventId(Integer eventId){
        return dslContext.selectFrom(PAYMENTMETHOD)
                .where(PAYMENTMETHOD.EVENTID.eq(eventId))
                .fetch().map(mapper -> {
                    PaymentMethod paymentMethod = new PaymentMethod();
                    paymentMethod.setPaymentMethodId(mapper.get(PAYMENTMETHOD.PAYMENTMETHODID));
                    paymentMethod.setPaymentMethod(mapper.get(PAYMENTMETHOD.PAYMENTMETHOD_));
                    paymentMethod.setPaymentAmount(mapper.getPaymentamount().doubleValue());
                    return paymentMethod;
                });
    }
}
