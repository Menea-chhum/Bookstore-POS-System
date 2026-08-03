package DAO;

import model.supplier;
import database.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class supplierDAO {

    // Create - Add a new supplier
    public boolean addSupplier(supplier supplier) {
        String sql = "INSERT INTO supplier (supplier_name, contact_number, email, address) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, supplier.getSupplierName());
            pstmt.setString(2, supplier.getContactNumber());
            pstmt.setString(3, supplier.getEmail());
            pstmt.setString(4, supplier.getAddress());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error adding supplier: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Read - Get all suppliers
    public static List<supplier> getAllSuppliers() {
        List<supplier> suppliers = new ArrayList<>();
        String sql = "SELECT * FROM supplier ORDER BY supplier_name";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                supplier s = new supplier(
                        rs.getInt("supplier_id"),
                        rs.getString("supplier_name"),
                        rs.getString("contact_number"),
                        rs.getString("email"),
                        rs.getString("address")
                );
                suppliers.add(s);
            }
        } catch (SQLException e) {
            System.err.println("Error getting all suppliers: " + e.getMessage());
            e.printStackTrace();
        }
        return suppliers;
    }

    // Read - Get supplier by ID
    public supplier getSupplierById(int supplierId) {
        String sql = "SELECT * FROM supplier WHERE supplier_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, supplierId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new supplier(
                        rs.getInt("supplier_id"),
                        rs.getString("supplier_name"),
                        rs.getString("contact_number"),
                        rs.getString("email"),
                        rs.getString("address")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error getting supplier by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Read - Search suppliers by name
    public List<supplier> searchSuppliers(String keyword) {
        List<supplier> suppliers = new ArrayList<>();
        String sql = "SELECT * FROM supplier WHERE supplier_name LIKE ? ORDER BY supplier_name";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                supplier s = new supplier(
                        rs.getInt("supplier_id"),
                        rs.getString("supplier_name"),
                        rs.getString("contact_number"),
                        rs.getString("email"),
                        rs.getString("address")
                );
                suppliers.add(s);
            }
        } catch (SQLException e) {
            System.err.println("Error searching suppliers: " + e.getMessage());
            e.printStackTrace();
        }
        return suppliers;
    }
    // Add this method to supplierDAO class
    public supplier getSupplierByName(String supplierName) {
        String sql = "SELECT * FROM supplier WHERE supplier_name = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, supplierName);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new supplier(
                        rs.getInt("supplier_id"),
                        rs.getString("supplier_name"),
                        rs.getString("contact_number"),
                        rs.getString("email"),
                        rs.getString("address")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error getting supplier by name: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Update - Update supplier
    public boolean updateSupplier(supplier supplier) {
        String sql = "UPDATE supplier SET supplier_name = ?, contact_number = ?, email = ?, address = ? WHERE supplier_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, supplier.getSupplierName());
            pstmt.setString(2, supplier.getContactNumber());
            pstmt.setString(3, supplier.getEmail());
            pstmt.setString(4, supplier.getAddress());
            pstmt.setInt(5, supplier.getSupplierId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error updating supplier: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Delete - Delete supplier
    public boolean deleteSupplier(int supplierId) {
        String sql = "DELETE FROM supplier WHERE supplier_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, supplierId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting supplier: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Check if supplier supplies any books
    public boolean hasBooks(int supplierId) {
        String sql = "SELECT COUNT(*) FROM book WHERE supplier_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, supplierId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking supplier books: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Get total number of suppliers
    public static int getTotalSuppliers() {
        String sql = "SELECT COUNT(*) FROM supplier";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting total suppliers: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
    public int getBookCountBySupplier(int supplierId) {
        String sql = "SELECT COUNT(*) FROM book WHERE supplier_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, supplierId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

}