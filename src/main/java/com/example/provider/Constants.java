package com.example.provider;

public final class Constants {
    public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    public static final String JDBC_URL = "jdbc:mysql://host.docker.internal/custom_provider?characterEncoding=UTF-8";
    public static final String DB_USERNAME = "root";
    public static final String DB_PASSWORD = "1";
    public static final String VALIDATION_QUERY = "select 1";
}
