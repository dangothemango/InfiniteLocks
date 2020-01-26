package com.dangorman.infinitelocks.core;

import com.dangorman.infinitelocks.db.DatabaseModule;
import groovy.sql.GroovyRowResult;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import javax.ws.rs.core.NewCookie;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

public class Utilities {

    private static final String GET_SESSIONS_SQL = "select * from sessions where sessionid = ? and username = ?";
    private static final String EXPIRE_SESSIONS_SQL = "delete from sessions where username = ? and expirydate < CURRENT_TIMESTAMP";

    public static Date addDate(Date d, int unit, int amount){
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(unit,amount);
        return c.getTime();
    }

    public static boolean isSessionActive(String username, String sessionId){
        if (username != null && sessionId != null){
            try {
                GroovyRowResult session = DatabaseModule
                        .rows(GET_SESSIONS_SQL, sessionId, username)
                        .get(0);
                Date expiryDate = (Date)session.get("expirydate");

                if (expiryDate.after(new Date())){
                    System.out.println("Session validated");
                    extendSession(username,sessionId);
                    System.out.println("Session extended");
                    return true;
                }
                System.out.println("User session expired");
            } catch (Exception e) {
                System.err.println("Unable verify user session: " + e.getMessage());
            }
        }
        return false;
    }

    public static boolean extendSession(String username, String sessionId) throws SQLException {
        Date expiry = new Date();
        expiry = Utilities.addDate(expiry, Calendar.DATE, 2);
        return DatabaseModule.execute(
                String.format("insert into sessions values (?,?,'%1$tD') " +
                        "on conflict (sessionid) do update set expirydate = '%1$tD'", expiry),
                sessionId,
                username);
    }

    public static void deleteExpiredSessions(String username){
        try{
            DatabaseModule.execute(EXPIRE_SESSIONS_SQL,username);
        } catch (Exception e) {
            System.err.println("Unable to remove expired sessions: " + e.getMessage());
        }
    }

    public static NewCookie createSecureCookie(String identifier, String value) {
        return new NewCookie(identifier,
                value,
                null,
                null,
                null,
                -1,
                true,
                true);
    }

    public static String getRedirect(String dest){
        JtwigTemplate template = JtwigTemplate.classpathTemplate("assets/redirect.html");

        return template.render(new JtwigModel().with("url",dest));
    }
}
