/*
 * This file is generated by jOOQ.
*/
package com.cursojunit.orderapi.entities.information_schema.tables.records;


import com.cursojunit.orderapi.entities.information_schema.tables.Users;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record4;
import org.jooq.Row4;
import org.jooq.impl.TableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.9.2"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class UsersRecord extends TableRecordImpl<UsersRecord> implements Record4<String, String, String, Integer> {

    private static final long serialVersionUID = -958572481;

    /**
     * Setter for <code>INFORMATION_SCHEMA.USERS.NAME</code>.
     */
    public void setName(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>INFORMATION_SCHEMA.USERS.NAME</code>.
     */
    public String getName() {
        return (String) get(0);
    }

    /**
     * Setter for <code>INFORMATION_SCHEMA.USERS.ADMIN</code>.
     */
    public void setAdmin(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>INFORMATION_SCHEMA.USERS.ADMIN</code>.
     */
    public String getAdmin() {
        return (String) get(1);
    }

    /**
     * Setter for <code>INFORMATION_SCHEMA.USERS.REMARKS</code>.
     */
    public void setRemarks(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>INFORMATION_SCHEMA.USERS.REMARKS</code>.
     */
    public String getRemarks() {
        return (String) get(2);
    }

    /**
     * Setter for <code>INFORMATION_SCHEMA.USERS.ID</code>.
     */
    public void setId(Integer value) {
        set(3, value);
    }

    /**
     * Getter for <code>INFORMATION_SCHEMA.USERS.ID</code>.
     */
    public Integer getId() {
        return (Integer) get(3);
    }

    // -------------------------------------------------------------------------
    // Record4 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row4<String, String, String, Integer> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row4<String, String, String, Integer> valuesRow() {
        return (Row4) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field1() {
        return Users.USERS.NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field2() {
        return Users.USERS.ADMIN;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field3() {
        return Users.USERS.REMARKS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field4() {
        return Users.USERS.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value1() {
        return getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value2() {
        return getAdmin();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value3() {
        return getRemarks();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value4() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UsersRecord value1(String value) {
        setName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UsersRecord value2(String value) {
        setAdmin(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UsersRecord value3(String value) {
        setRemarks(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UsersRecord value4(Integer value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UsersRecord values(String value1, String value2, String value3, Integer value4) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached UsersRecord
     */
    public UsersRecord() {
        super(Users.USERS);
    }

    /**
     * Create a detached, initialised UsersRecord
     */
    public UsersRecord(String name, String admin, String remarks, Integer id) {
        super(Users.USERS);

        set(0, name);
        set(1, admin);
        set(2, remarks);
        set(3, id);
    }
}
