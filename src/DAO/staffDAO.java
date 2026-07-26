package DAO;

import model.staff;
import database.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class staffDAO {

    // Create - Add a new staff member
    public boolean addStaff(staff staff) {
        String sql = "INSERT INTO staff (username, password, full_name, role) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, staff.getUsername());
            pstmt.setString(2, staff.getPassword()); // In production, hash this!
            pstmt.setString(3, staff.getFull_name());
            pstmt.setString(4, staff.getRole());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error adding staff: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Read - Get all staff members
    public List<staff> getAllStaff() {
        List<staff> staffList = new ArrayList<>();
        String sql = "SELECT * FROM staff ORDER BY full_name";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                staff s = new staff(
                        rs.getInt("staff_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("full_name"),
                        rs.getString("role")
                );
                staffList.add(s);
            }
        } catch (SQLException e) {
            System.err.println("Error getting all staff: " + e.getMessage());
            e.printStackTrace();
        }
        return staffList;
    }

    // Read - Get staff by ID
    public staff getStaffById(int staffId) {
        String sql = "SELECT * FROM staff WHERE staff_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, staffId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new staff(
                        rs.getInt("staff_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("full_name"),
                        rs.getString("role")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error getting staff by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Read - Get staff by username (for login)
    public staff getStaffByUsername(String username) {
        String sql = "SELECT * FROM staff WHERE username = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new staff(
                        rs.getInt("staff_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("full_name"),
                        rs.getString("role")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error getting staff by username: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Validate login credentials
    public boolean validateLogin(String username, String password) {
        String sql = "SELECT * FROM staff WHERE username = ? AND password = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password); // In production, use hashed password comparison

            ResultSet rs = pstmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            System.err.println("Error validating login: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Update - Update staff member
    public boolean updateStaff(staff staff) {
        String sql = "UPDATE staff SET username = ?, password = ?, full_name = ?, role = ? WHERE staff_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, staff.getUsername());
            pstmt.setString(2, staff.getPassword()); // In production, hash this!
            pstmt.setString(3, staff.getFull_name());
            pstmt.setString(4, staff.getRole());
            pstmt.setInt(5, staff.getStaffId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error updating staff: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Delete - Delete staff member
    public boolean deleteStaff(int staffId) {
        String sql = "DELETE FROM staff WHERE staff_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, staffId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting staff: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Check if username already exists
    public boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM staff WHERE username = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking username: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Get staff by role
    public List<staff> getStaffByRole(String role) {
        List<staff> staffList = new ArrayList<>();
        String sql = "SELECT * FROM staff WHERE role = ? ORDER BY full_name";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, role);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                staff s = new staff(
                        rs.getInt("staff_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("full_name"),
                        rs.getString("role")
                );
                staffList.add(s);
            }
        } catch (SQLException e) {
            System.err.println("Error getting staff by role: " + e.getMessage());
            e.printStackTrace();
        }
        return staffList;
    }
}