package com.dangorman.infinitelocks.core;

import com.dangorman.infinitelocks.db.DatabaseModule;
import groovy.sql.GroovyRowResult;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.sql.SQLException;
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
                GroovyRowResult session = DatabaseModule
                        .rows("select * from sessions where sessionid = ? and username = ?", sessionId, username)
                        .get(0);
                Date expiryDate = (Date)session.get("expirydate");

                if (expiryDate.after(new Date())){
                    System.out.println("Session validated");
                    if (extendSession(username,sessionId)) {
                        System.out.println("Session extended");
                    }
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

    public static boolean extendSession(String username, String sessionId) throws SQLException {
        Date expiry = new Date();
        expiry = Utilities.addDate(expiry, Calendar.DATE, 1);
        return DatabaseModule.execute(
                String.format("insert into sessions values (?,?,'%1$tD') " +
                        "on conflict do update set expirydate = '%1$td'", expiry),
                sessionId,
                username);
    }

    public static void deleteExpiredSessions(String username){
        try{
            DatabaseModule.execute("delete from sessions where username = ? and expirydate < CURRENT_TIMESTAMP",username);
        } catch (Exception e) {
            System.err.println("Unable to remove expired sessions: " + e.getMessage());
        }
    }
}
