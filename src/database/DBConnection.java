package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/bookstore_inventory_management";
    private static final String USER = "root";
    private static final String PASSWORD = "tA9$Xf2@Qw8!Zd3P";

    public static Connection getConnection() {

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            return DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (ClassNotFoundException | SQLException e) {

            System.out.println("Database Connection Failed");
            e.printStackTrace();

            return null;
        }
    }
}