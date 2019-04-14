package com.dangorman.infinitelocks.core;

import java.util.Calendar;
import java.util.Date;

public class Utilities {

    public static Date addDate(Date d, int unit, int amount){
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(unit,amount);
        return c.getTime();
    }
}
