package form;

import DAO.bookDAO;
import model.book;

import javax.swing.*;

public class deleteBook {
    private JPanel mainPanel;
    private JButton deleteButton;
    private JButton cancelButton;

    private book currentBook;
    private bookDAO dao = new bookDAO();

    public JPanel getMainPanel()
    {
        return mainPanel;
    }

    public deleteBook(book b) {

        currentBook = b;

        deleteButton.addActionListener(e -> deleteBook());

        cancelButton.addActionListener(e -> closeWindow());

    }
    private void deleteBook() {

        if (dao.deleteBook(currentBook.getBookId())) {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Book deleted successfully."
            );

            closeWindow();

        } else {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Delete failed."
            );

        }

    }
    private void closeWindow() {

        SwingUtilities.getWindowAncestor(mainPanel).dispose();

    }
}
