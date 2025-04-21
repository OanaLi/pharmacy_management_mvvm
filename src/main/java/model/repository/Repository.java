package model.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Repository {

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "YOUR_DATABASE_URL_HERE";
    private static final String USER = "YOUR_USERNAME_HERE";
    private static final String PASSWORD = "YOUR_PASSWORD_HERE";

    private static Repository instance;
    private Connection connection;

    private Repository() {
        try{
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        } catch (ClassNotFoundException e){ //exceptie specifica
            e.printStackTrace();
        } catch (SQLException e) { //exceptie generala
            e.printStackTrace();
        }
    }

    public static Repository getInstance() {
        if (instance == null) {
            synchronized (Repository.class) {
                if (instance == null) {
                    instance = new Repository();
                }
            }
        }
        return instance;
    }

    public Connection getConnection(){
        return connection;
    }
}
