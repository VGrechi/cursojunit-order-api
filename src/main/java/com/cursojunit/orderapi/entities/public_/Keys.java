/*
 * This file is generated by jOOQ.
*/
package com.cursojunit.orderapi.entities.public_;


import com.cursojunit.orderapi.entities.public_.tables.Event;
import com.cursojunit.orderapi.entities.public_.tables.Order;
import com.cursojunit.orderapi.entities.public_.tables.Paymentmethod;
import com.cursojunit.orderapi.entities.public_.tables.records.EventRecord;
import com.cursojunit.orderapi.entities.public_.tables.records.OrderRecord;
import com.cursojunit.orderapi.entities.public_.tables.records.PaymentmethodRecord;

import javax.annotation.Generated;

import org.jooq.Identity;
import org.jooq.impl.AbstractKeys;


/**
 * A class modelling foreign key relationships between tables of the <code>PUBLIC</code> 
 * schema
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.9.2"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // IDENTITY definitions
    // -------------------------------------------------------------------------

    public static final Identity<EventRecord, Integer> IDENTITY_EVENT = Identities0.IDENTITY_EVENT;
    public static final Identity<OrderRecord, Integer> IDENTITY_ORDER = Identities0.IDENTITY_ORDER;
    public static final Identity<PaymentmethodRecord, Integer> IDENTITY_PAYMENTMETHOD = Identities0.IDENTITY_PAYMENTMETHOD;

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------


    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------


    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class Identities0 extends AbstractKeys {
        public static Identity<EventRecord, Integer> IDENTITY_EVENT = createIdentity(Event.EVENT, Event.EVENT.EVENTID);
        public static Identity<OrderRecord, Integer> IDENTITY_ORDER = createIdentity(Order.ORDER, Order.ORDER.ORDERID);
        public static Identity<PaymentmethodRecord, Integer> IDENTITY_PAYMENTMETHOD = createIdentity(Paymentmethod.PAYMENTMETHOD, Paymentmethod.PAYMENTMETHOD.PAYMENTMETHODID);
    }
}