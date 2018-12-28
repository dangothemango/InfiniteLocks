package com.dangorman.infinitelocks.db;

import groovy.sql.Sql;
import groovy.util.logging.Slf4j;
import jline.internal.Log;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

@Slf4j
public final class DatabaseModule {

    private static String herokuUrl;
    private static String username;
    private static String password;
    private static String connectionString;
    private static Sql dbConnection;

    public static Sql setDbUrl(String url) {
        herokuUrl = url;
        try {
            URI uri = new URI(herokuUrl);
            username = uri.getUserInfo().split(":")[0];
            password = uri.getUserInfo().split(":")[1];
            connectionString = "jdbc:postgresql://" + uri.getHost() + ':' + uri.getPort() + uri.getPath()
                    + "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
            dbConnection = Sql.newInstance(connectionString, username, password);
            dbConnection.execute("Select 1");
            return dbConnection;
        } catch (URISyntaxException e) {
            Log.error(e.getMessage());
        } catch (SQLException e) {
            Log.error(e.getMessage());
        }
        return null;
    }

    public static Sql getDbConnection(){
        return dbConnection;
    }
}
