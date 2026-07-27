import form.logIn;
import form.cashierDashboard;
import form.SaleNew;

import javax.swing.*;



public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Bookstore Login");
        frame.setContentPane(new logIn().getMainPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }


}
