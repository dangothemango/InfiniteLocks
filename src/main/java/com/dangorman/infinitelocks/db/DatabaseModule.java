package com.dangorman.infinitelocks.db;

import groovy.sql.Sql;
import groovy.util.logging.Slf4j;

import java.net.URI;
import java.net.URISyntaxException;

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
            connectionString = "jdbc:postgresql://" + uri.getHost() + ':' + uri.getPort() + uri.getPath();
                  //  + "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";

            System.out.println("CONNECTION STRING:" + herokuUrl);
            dbConnection = Sql.newInstance(connectionString, username, password,"org.postgresql.Driver");
            System.out.println(DatabaseModule.getDbConnection() == null ? "itsnull": "its not");
            return dbConnection;
        } catch (Exception e) {
            System.out.println(e.getClass().getName());
            System.out.println(e.getMessage());
            System.exit(1);
        }
        return null;
    }

    public static Sql getDbConnection(){
        return dbConnection;
    }
}
