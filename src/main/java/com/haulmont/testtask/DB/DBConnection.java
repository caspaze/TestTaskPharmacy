package com.haulmont.testtask.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Класс, осуществляющий подключение к базе данных через JDBC драйвер
 */
public class DBConnection {
    private static final String URL = "jdbc:hsqldb:file:pharmacy";
    private static final String USERNAME = "SA";
    private static final String PASSWORD = "";
    private Connection connection;
    public DBConnection(){
        try{
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
            connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public Connection getConnection(){
        return connection;
    }
}
