package form;

import model.staff;

import javax.swing.*;
import java.awt.*;

public class cashierDashboard {
    private JPanel mainPanel;
    private JButton newSaleButton;
    private JButton logoutButton;
    private JLabel welcomeLabel;
    private staff currentStaff;
    private JPanel MainPanel;
    private JPanel StaffCard;
    public cashierDashboard() {
        this(null);
    }

    public cashierDashboard(staff staff) {
        this.currentStaff = staff;
        createUI();

        if (welcomeLabel != null && staff != null) {
            welcomeLabel.setText("Welcome, " + staff.getFull_name());
        }

        setupListeners();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private void createUI() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240));

        // Header
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Center
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(240, 240, 240));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Cashier Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(new Color(30, 30, 50));
        centerPanel.add(titleLabel, gbc);

        gbc.gridy = 1;
        JLabel subtitleLabel = new JLabel("Point of Sale System");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        subtitleLabel.setForeground(Color.GRAY);
        centerPanel.add(subtitleLabel, gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(30, 10, 10, 10);
        newSaleButton = new JButton("🛒 Start New Sale");
        newSaleButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        newSaleButton.setBackground(new Color(0, 120, 215));
        newSaleButton.setForeground(Color.WHITE);
        newSaleButton.setFocusPainted(false);
        newSaleButton.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        newSaleButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        newSaleButton.setPreferredSize(new Dimension(250, 60));
        centerPanel.add(newSaleButton, gbc);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(30, 30, 50));
        header.setPreferredSize(new Dimension(0, 70));
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftPanel.setOpaque(false);

        JLabel logoLabel = new JLabel("📚");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        logoLabel.setForeground(Color.WHITE);

        JLabel storeName = new JLabel("Inkwell Books");
        storeName.setFont(new Font("Segoe UI", Font.BOLD, 22));
        storeName.setForeground(Color.WHITE);

        leftPanel.add(logoLabel);
        leftPanel.add(storeName);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setOpaque(false);

        welcomeLabel = new JLabel("Cashier");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        welcomeLabel.setForeground(Color.WHITE);

        logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logoutButton.setBackground(new Color(200, 50, 50));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        rightPanel.add(welcomeLabel);
        rightPanel.add(logoutButton);

        header.add(leftPanel, BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);

        return header;
    }

    private void setupListeners() {
        if (newSaleButton != null) {
            newSaleButton.addActionListener(e -> openNewSale());
        }

        if (logoutButton != null) {
            logoutButton.addActionListener(e -> logout());
        }
    }

    private void openNewSale() {
        try {
            System.out.println("=== OPENING NEW SALE ===");
            System.out.println("Current staff: " + (currentStaff != null ? currentStaff.getFull_name() : "NULL"));

            NewSale newSaleForm = new NewSale(currentStaff);
            JFrame frame = new JFrame("New Sale - Bookstore POS");
            frame.setContentPane(newSaleForm.getMainPanel());
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(1200, 700);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(mainPanel,
                    "Error opening New Sale form: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(mainPanel,
                "Are you sure you want to logout?",
                "Logout Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            Window parentWindow = SwingUtilities.getWindowAncestor(mainPanel);
            if (parentWindow != null) {
                parentWindow.dispose();
            }

            SwingUtilities.invokeLater(() -> {
                try {
                    JFrame loginFrame = new JFrame("Bookstore POS - Login");
                    logIn loginForm = new logIn();
                    loginFrame.setContentPane(loginForm.getMainPanel());
                    loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    loginFrame.pack();
                    loginFrame.setLocationRelativeTo(null);
                    loginFrame.setVisible(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null,
                            "Error opening login screen: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            });
        }
    }
}