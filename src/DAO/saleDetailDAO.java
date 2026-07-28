package DAO;

import database.DBConnection;
import model.saleDetail;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class saleDetailDAO {


    public static List<saleDetail> getAllSaleDetails(){

        List<saleDetail> list = new ArrayList<>();

        String sql =
                """
                SELECT 
                sd.detail_id,
                s.sale_id,
                b.title,
                sd.quantity_sold,
                sd.unit_price,
                s.sale_date,
                st.full_name
        
                FROM sale_detail sd
        
                JOIN sale s
                ON sd.sale_id = s.sale_id
        
                JOIN book b
                ON sd.book_id = b.book_id
        
                JOIN staff st
                ON s.staff_id = st.staff_id
                """;

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){


            while(rs.next()){

                list.add(new saleDetail(
                        rs.getInt("detail_id"),
                        rs.getInt("sale_id"),
                        rs.getString("title"),
                        rs.getInt("quantity_sold"),
                        rs.getDouble("unit_price"),
                        rs.getTimestamp("sale_date"),
                        rs.getString("full_name")
                ));
            }


        }catch(Exception e){
            e.printStackTrace();
        }


        return list;
    }
}