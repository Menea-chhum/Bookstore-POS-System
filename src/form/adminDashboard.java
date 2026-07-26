package form;

import javax.swing.*;
import java.awt.*;

public class adminDashboard {
    private JPanel mainPanel;
    private JLabel storeLogo;
    private JLabel avatarLabel;
    private JButton logoutBtn;
    private JLabel staffNameLabel;
    private JLabel staffrolePanel;
    private JTabbedPane dashboardTab;
    private JPanel booksPanel;
    private JButton addBookBtn;
    private JLabel totalTitleLabel;
    private JLabel totalStockLabel;
    private JLabel totalCategoryLabel;
    private JLabel LowStockLable;
    private JTextField bookSearchField;
    private JComboBox categoryFilterCombo;
    private JPanel categoriesPanel;
    private JPanel categoryTopPanel;
    private JButton addCategoryButton;
    private JTextField categorySearchField;
    private JPanel categoryGridPanel;
    private JPanel selfHelpCard;
    private JTextField searchSuppliersTextField;
    private JTable table1;
    private JButton addSupplierButton;
    private JTable table2;
    private JTable table3;
    private JPanel FictionCard;
    private JPanel horrorCard;
    private JPanel businessCard;

    private JPanel technologyCard;


//    private void createUIComponents() {
//        // Set fixed size for all category cards
//        Dimension cardSize = new Dimension(300, 200);
//
//        selfHelpCard.setPreferredSize(cardSize);
//        selfHelpCard.setMaximumSize(cardSize);
//
//        technologyCard.setPreferredSize(cardSize);
//        technologyCard.setMaximumSize(cardSize);
//
//        businessCard.setPreferredSize(cardSize);
//        businessCard.setMaximumSize(cardSize);
//
//        FictionCard.setPreferredSize(cardSize);
//        FictionCard.setMaximumSize(cardSize);
//
//        horrorCard.setPreferredSize(cardSize);
//        horrorCard.setMaximumSize(cardSize);
//    }

    public JPanel getMainPanel(){
        return mainPanel;
    }
}
