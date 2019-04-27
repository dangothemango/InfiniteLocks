package com.dangorman.infinitelocks.core;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.util.Calendar;
import java.util.Date;

public class Utilities {

    public static Date addDate(Date d, int unit, int amount){
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(unit,amount);
        return c.getTime();
    }

    public static String checkLoginStatus(String username, String sessionId){
        if (username == null || sessionId == null){
            return null;
        }

        JtwigTemplate template = JtwigTemplate.classpathTemplate("assets/Login.html");
        return template.render(new JtwigModel());
    }
}
