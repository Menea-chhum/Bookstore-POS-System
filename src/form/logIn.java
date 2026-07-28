package form;

import database.DBConnection;
import model.staff;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class logIn {
    private JPanel mainPanel;
    private JPanel LoginCard;
    private JButton logInButton;
    private JTextField usernameInput;
    private JPasswordField passwordInput;
    private JLabel storeLogo;
    private JLabel failMsg;

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public logIn() {
        failMsg.setVisible(false);
        failMsg.setForeground(Color.RED);
        failMsg.setText("Invalid username or password");

        logInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });

        // Add Enter key listener
        usernameInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });

        passwordInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });
    }

    private void performLogin() {
        String username = usernameInput.getText().trim();
        String password = new String(passwordInput.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            failMsg.setText("Please enter username and password");
            failMsg.setVisible(true);
            return;
        }

        Connection con = null;
        try {
            con = DBConnection.getConnection();

            if (con == null) {
                JOptionPane.showMessageDialog(mainPanel,
                        "Database connection failed!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String sql = "SELECT * FROM staff WHERE username = ? AND password = ?";

            try (PreparedStatement ppst = con.prepareStatement(sql)) {
                ppst.setString(1, username);
                ppst.setString(2, password);

                try (ResultSet rs = ppst.executeQuery()) {
                    if (rs.next()) {
                        // Create staff object with ALL fields
                        staff loggedInStaff = new staff(
                                rs.getInt("staff_id"),
                                rs.getString("username"),
                                rs.getString("password"),
                                rs.getString("full_name"),
                                rs.getString("role")
                        );

                        // Debug output
                        System.out.println("Staff logged in: " + loggedInStaff.getFull_name());
                        System.out.println("Staff ID: " + loggedInStaff.getStaffId());
                        System.out.println("Role: " + loggedInStaff.getRole());

                        failMsg.setVisible(false);

                        // Open appropriate dashboard with staff object
                        if (loggedInStaff.getRole().equalsIgnoreCase("Admin")) {
                            adminDashboard adminDash = new adminDashboard(loggedInStaff);
                            openDashboard(adminDash.getMainPanel(), "Admin Dashboard");
                        } else if (loggedInStaff.getRole().equalsIgnoreCase("Cashier")) {
                            cashierDashboard cashierDash = new cashierDashboard(loggedInStaff);
                            openDashboard(cashierDash.getMainPanel(), "Cashier Dashboard");
                        } else {
                            JOptionPane.showMessageDialog(mainPanel,
                                    "Unknown role: " + loggedInStaff.getRole(),
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        failMsg.setText("Invalid username or password");
                        failMsg.setVisible(true);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(mainPanel,
                    "Database Error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (con != null) con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void openDashboard(JPanel panel, String title) {
        JFrame frame = new JFrame(title);
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 700);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Close login window
        Window window = SwingUtilities.getWindowAncestor(mainPanel);
        if (window != null) {
            window.dispose();
        }
    }
}