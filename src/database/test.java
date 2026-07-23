package database;

import java.sql.Connection;

public class test {

    public static void main(String[] args) {

        Connection con = DBConnection.getConnection();

        if(con != null){

            System.out.println("Connected Successfully!");

        }else{

            System.out.println("Connection Failed.");

        }

    }

}