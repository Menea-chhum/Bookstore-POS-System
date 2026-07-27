package DAO;

import model.book;
import database.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class bookDAO {

    // Create - Add a new book
    public boolean addBook(book book) {
        String sql = "INSERT INTO book (title, author, price, quantity, category_id, supplier_id) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setDouble(3, book.getPrice());
            pstmt.setInt(4, book.getStockQuantity());
            pstmt.setInt(5, book.getCategoryId());
            pstmt.setInt(6, book.getSupplierId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error adding book: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Read - Get all books
    public static List<book> getAllBooks() {
        List<book> books = new ArrayList<>();
//        String sql = "SELECT * FROM book ORDER BY title";
        String sql =
                "SELECT b.*, c.category_name, s.supplier_name " +
                        "FROM book b " +
                        "JOIN category c ON b.category_id = c.category_id " +
                        "JOIN supplier s ON b.supplier_id = s.supplier_id " +
                        "ORDER BY b.title";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
//                book b = new book(
//                        rs.getInt("book_id"),
//                        rs.getString("title"),
//                        rs.getString("author"),
//                        rs.getDouble("price"),
//                        rs.getInt("quantity"),
//                        rs.getInt("category_id"),
//                        rs.getInt("supplier_id")
//                );
//                books.add(b);
                book b = new book(
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getDouble("price"),
                        rs.getInt("quantity"),
                        rs.getInt("category_id"),
                        rs.getInt("supplier_id")
                );

                b.setCategoryName(rs.getString("category_name"));
                b.setSupplierName(rs.getString("supplier_name"));

                books.add(b);
            }
        } catch (SQLException e) {
            System.err.println("Error getting all books: " + e.getMessage());
            e.printStackTrace();
        }
        return books;
    }

    // Read - Get book by ID
    public book getBookById(int bookId) {
        String sql = "SELECT * FROM book WHERE book_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, bookId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new book(
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getDouble("price"),
                        rs.getInt("quantity"),
                        rs.getInt("category_id"),
                        rs.getInt("supplier_id")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error getting book by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Read - Search books by title or author
    public List<book> searchBooks(String keyword) {
        List<book> books = new ArrayList<>();
        String sql = "SELECT * FROM book WHERE title LIKE ? OR author LIKE ? ORDER BY title";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                book b = new book(
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getDouble("price"),
                        rs.getInt("quantity"),
                        rs.getInt("category_id"),
                        rs.getInt("supplier_id")
                );
                books.add(b);
            }
        } catch (SQLException e) {
            System.err.println("Error searching books: " + e.getMessage());
            e.printStackTrace();
        }
        return books;
    }

    // Read - Get books by category
    public List<book> getBooksByCategory(int categoryId) {
        List<book> books = new ArrayList<>();
        String sql = "SELECT * FROM book WHERE category_id = ? ORDER BY title";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, categoryId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                book b = new book(
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getDouble("price"),
                        rs.getInt("quantity"),
                        rs.getInt("category_id"),
                        rs.getInt("supplier_id")
                );
                books.add(b);
            }
        } catch (SQLException e) {
            System.err.println("Error getting books by category: " + e.getMessage());
            e.printStackTrace();
        }
        return books;
    }

    // Read - Get books by supplier
    public List<book> getBooksBySupplier(int supplierId) {
        List<book> books = new ArrayList<>();
        String sql = "SELECT * FROM book WHERE supplier_id = ? ORDER BY title";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, supplierId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                book b = new book(
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getDouble("price"),
                        rs.getInt("quantity"),
                        rs.getInt("category_id"),
                        rs.getInt("supplier_id")
                );
                books.add(b);
            }
        } catch (SQLException e) {
            System.err.println("Error getting books by supplier: " + e.getMessage());
            e.printStackTrace();
        }
        return books;
    }

    // Update - Update book details
    public boolean updateBook(book book) {
        String sql = "UPDATE book SET title = ?, author = ?, price = ?, quantity = ?, category_id = ?, supplier_id = ? WHERE book_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setDouble(3, book.getPrice());
            pstmt.setInt(4, book.getStockQuantity());
            pstmt.setInt(5, book.getCategoryId());
            pstmt.setInt(6, book.getSupplierId());
            pstmt.setInt(7, book.getBookId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error updating book: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Update - Update stock quantity only
    public boolean updateStock(int bookId, int newQuantity) {
        String sql = "UPDATE book SET quantity = ? WHERE book_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, newQuantity);
            pstmt.setInt(2, bookId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error updating stock: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Delete - Delete a book
    public boolean deleteBook(int bookId) {
        String sql = "DELETE FROM book WHERE book_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, bookId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting book: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Check if book exists
    public boolean bookExists(int bookId) {
        String sql = "SELECT COUNT(*) FROM book WHERE book_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, bookId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking book existence: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Get total number of books
    public static int getTotalBooks() {
        String sql = "SELECT COUNT(*) FROM book";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting total books: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
    // get low stock
    public static List<book> getLowStockBooks() {
        List<book> books = new ArrayList<>();
        String sql = "SELECT * FROM book WHERE quantity < 5 ";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                books.add(new book(
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getDouble("price"),
                        rs.getInt("quantity"),
                        rs.getInt("category_id"),
                        rs.getInt("supplier_id")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }
    // get total stock
    public static int getTotalStock() {
        String sql = "SELECT SUM(quantity) FROM book";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
}