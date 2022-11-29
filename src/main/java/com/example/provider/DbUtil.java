package com.example.provider;

import org.keycloak.component.ComponentModel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtil {

    public static Connection getConnection(ComponentModel config) throws SQLException {
        String driverClass = config.get(Constants.JDBC_DRIVER);
        try {
            Class.forName(driverClass);
        } catch (ClassNotFoundException nfe) {
            throw new RuntimeException("Invalid JDBC driver: " + driverClass + ". Please check if your driver if properly installed");
        }
        return DriverManager.getConnection(config.get(Constants.JDBC_URL),
                config.get(Constants.DB_USERNAME),
                config.get(Constants.DB_PASSWORD));
    }
}
