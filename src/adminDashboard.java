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
    private JTable booksTable;
    private JPanel categoriesPanel;
    private JPanel categoryTopPanel;
    private JButton addCategoryButton;
    private JTextField categorySearchField;
    private JPanel categoryGridPanel;
    private JPanel selfHelpCard;
    private JLabel selfHelpNameLabel;
    private JLabel selfHelpDescLabel;
    private JLabel selfHelpCountLabel;
    private JTextField searchSuppliersTextField;
    private JTable table1;
    private JButton addSupplierButton;
    private JTable table2;
    private JPanel TechnologyCard;
    private JButton a️Button;
    private JButton a️Button3;
    private JButton a️Button1;
    private JButton a️Button8;
    private JButton a️Button6;
    private JButton a️Button9;
    private JPanel FictionCard;
    private JPanel horrorCard;
    private JButton a️Button4;
    private JButton a️Button5;
    private JPanel businessCard;
    private JButton addCategoryButton1;
    private JPanel addCategoryCard;
    private JButton a️Button2;
    private JButton a️Button7;

    private JPanel technologyCard;


    private void createUIComponents() {
        // Set fixed size for all category cards
        Dimension cardSize = new Dimension(300, 200);

        selfHelpCard.setPreferredSize(cardSize);
        selfHelpCard.setMaximumSize(cardSize);

        technologyCard.setPreferredSize(cardSize);
        technologyCard.setMaximumSize(cardSize);

        businessCard.setPreferredSize(cardSize);
        businessCard.setMaximumSize(cardSize);

        FictionCard.setPreferredSize(cardSize);
        FictionCard.setMaximumSize(cardSize);

        horrorCard.setPreferredSize(cardSize);
        horrorCard.setMaximumSize(cardSize);
    }
}
