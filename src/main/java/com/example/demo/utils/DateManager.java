package com.example.demo.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateManager {

    public static String getReversedSystemDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");// ("yyyy.MM.dd");
        Date date = new Date();
        return dateFormat.format(date);
    }
    public static String getDateBeforeAnotherDate(int days, Date helpDate) {
        Calendar gregorian = new GregorianCalendar();
        gregorian.setTime(helpDate);
        gregorian.add(Calendar.DAY_OF_YEAR, -1 * days);
        Date date2 = gregorian.getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(date2);
    }
}
