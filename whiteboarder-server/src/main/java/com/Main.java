package com;

import com.rest.Rest;
import com.db.DatabaseConnector;
import java.io.IOException;
import org.glassfish.grizzly.http.server.HttpServer;

public class Main {

    public static final String LOCAL_MYSQL_DB = "localhost:3306";
    public static final String LOCAL_MYSQL_USER = "root";
    
    public static final String MYSQL_DB = "mysql.cs.iastate.edu:3306";
    public static final String MYSQL_USER = "dbu309ytc1";
    public static final String MYSQL_PASS = "sffwC#x#";

    public static void main(String[] args) throws IOException {
        if (args[0] == "local") {
            DatabaseConnector dbc = new DatabaseConnector(LOCAL_MYSQL_DB, LOCAL_MYSQL_USER, null);
            final HttpServer server = Rest.startServer(true);
            System.in.read();
            server.stop();
            dbc.endConnection();
        } else {
            DatabaseConnector dbc = new DatabaseConnector(MYSQL_DB, MYSQL_USER, MYSQL_PASS);
            final HttpServer server = Rest.startServer(false);

            // TODO: At what point do I kill the server?

            server.stop();
            dbc.endConnection();
        }
    }
}