package com.dangorman.infinitelocks.core;

import com.dangorman.infinitelocks.db.DatabaseModule;
import groovy.sql.GroovyRowResult;
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
        if (username != null && sessionId != null){
            try {
                GroovyRowResult session = DatabaseModule.rows(
                        String.format("select * from sessions where sessionid = '%s' and username = '%s'", sessionId, username)
                ).get(0);
                Date expiryDate = (Date)session.get("expirydate");

                if (expiryDate.after(new Date())){
                    System.out.println("Session validated");
                    return null;
                }
                System.out.println("User session expired");
            } catch (Exception e) {
                System.err.println("Unable verify user session: " + e.getMessage());
            }
        }
        JtwigTemplate template = JtwigTemplate.classpathTemplate("assets/Login.html");
        return template.render(new JtwigModel());
    }
}
