package com.cursojunit.orderapi.utils;

import java.sql.Timestamp;
import java.util.Date;

public class DateTimeUtils {

    public static Timestamp convertDateToTimestap(Date date){
        return new Timestamp(date.getTime());
    }

}
