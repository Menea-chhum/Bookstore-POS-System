import javax.swing.*;
import java.awt.event.*;

public class editBook extends JDialog {
    private JPanel contentPane;
    private JButton buttonCancel;
    private JButton buttonSaveChange;
    private JComboBox categoryCombo;
    private JComboBox supplierCombo;
    private JTextField bookTitleField;
    private JTextField authorField;
    private JTextField priceField;
    private JTextField stockField;
    private JLabel errMsg;

    public editBook() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonCancel);

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        buttonSaveChange.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onSaveChange();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onCancel() {
        // add your code here
        dispose();
    }

    private void onSaveChange() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        editBook dialog = new editBook();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
