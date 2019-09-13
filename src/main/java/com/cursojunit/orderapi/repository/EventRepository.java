package com.cursojunit.orderapi.repository;

import com.cursojunit.orderapi.entities.public_.tables.records.EventRecord;
import com.cursojunit.orderapi.model.Event;
import com.cursojunit.orderapi.utils.DateTimeUtils;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

import static com.cursojunit.orderapi.entities.public_.tables.Event.EVENT;

@Repository
public class EventRepository {

    @Autowired
    private DSLContext dslContext;

    public Integer saveEvent(Integer orderId, Event event) {
        EventRecord eventRecord = dslContext.insertInto(EVENT)
                .columns(EVENT.ORDERID,
                        EVENT.EVENTTYPE,
                        EVENT.AMOUNT,
                        EVENT.CREATIONDATE)
                .values(orderId,
                        event.getEventType(),
                        BigDecimal.valueOf(event.getAmount()),
                        DateTimeUtils.convertDateToTimestap(event.getCreationDate()))
                .returning(EVENT.EVENTID).fetchOne();
        return eventRecord.getEventid();
    }

    public List<Event> selectEventByOrderId(Integer orderId){
        return dslContext.selectFrom(EVENT)
                .where(EVENT.ORDERID.eq(orderId))
                .fetch().map(mapper -> {
                    Event event = new Event();
                    event.setEventId(mapper.get(EVENT.EVENTID));
                    event.setEventType(mapper.get(EVENT.EVENTTYPE));
                    event.setAmount(mapper.get(EVENT.AMOUNT).doubleValue());
                    event.setCreationDate(mapper.get(EVENT.CREATIONDATE));
                    return event;
                });
    }


}
