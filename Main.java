import javax.swing.*;
import java.awt.*;
import java.util.*;

// ! Main class creates and manages the BookTrackr GUI application
public class Main {

    // ? Launches the GUI Window I think
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createAndShowGui);
    }

    // ! Creates and displays the GUI window with Everything
    private static void createAndShowGui() {
        JFrame frame = new JFrame("BookTrackr"); // ? Creates the frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ? Closes the program on exit
        frame.setSize(640, 420);

        JPanel root = new JPanel(new BorderLayout(8, 8));

        // ! Panel for user input (title, author, year, add button)
        JPanel input = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0;
        c.gridy = 0;
        input.add(new JLabel("Title:"), c);
        JTextField titleField = new JTextField(24);
        c.gridx = 1;
        input.add(titleField, c);

        c.gridx = 0;
        c.gridy = 1;
        input.add(new JLabel("Author:"), c);
        JTextField authorField = new JTextField(24);
        c.gridx = 1;
        input.add(authorField, c);

        c.gridx = 0;
        c.gridy = 2;
        input.add(new JLabel("Year:"), c);
        JTextField yearField = new JTextField(6);
        c.gridx = 1;
        input.add(yearField, c);

        JButton addBtn = new JButton("Add Book");
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2;
        input.add(addBtn, c);

        // ! Model and view for displaying the list of books
        DefaultListModel<Book> listModel = new DefaultListModel<>();
        JList<Book> bookList = new JList<>(listModel);
        bookList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(bookList);

        // Panel for sorting and removing books
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        JComboBox<String> sortBox = new JComboBox<>(new String[] { "Title", "Author", "Year" });
        JButton sortBtn = new JButton("Sort");
        JButton removeBtn = new JButton("Remove Selected");
        controls.add(new JLabel("Sort by:"));
        controls.add(sortBox);
        controls.add(sortBtn);
        controls.add(removeBtn);

        // Add Book button - validates input and adds new book to list
        addBtn.addActionListener(e -> {
            String t = titleField.getText().trim();
            String a = authorField.getText().trim();
            String yText = yearField.getText().trim();
            if (t.isEmpty() || a.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter both Title and Author.", "Missing data",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            int y = 0;
            if (!yText.isEmpty()) {
                try {
                    y = Integer.parseInt(yText);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Year must be a number.");
                    return;
                }
            }
            Book b = new Book(t, a, y);
            listModel.addElement(b);
            titleField.setText("");
            authorField.setText("");
            yearField.setText("");
        });

        // Remove Selected button - removes the selected book from the list
        removeBtn.addActionListener(e -> {
            int idx = bookList.getSelectedIndex();
            if (idx != -1)
                listModel.remove(idx);
        });

        // Sort button - sorts books by the selected category (Title, Author, or Year)
        sortBtn.addActionListener(e -> {
            String key = (String) sortBox.getSelectedItem();
            java.util.List<Book> books = Collections.list(listModel.elements());
            Comparator<Book> comp = Book.BY_TITLE;
            if ("Author".equals(key))
                comp = Book.BY_AUTHOR;
            else if ("Year".equals(key))
                comp = Book.BY_YEAR;
            books.sort(comp);
            listModel.clear();
            for (Book b : books)
                listModel.addElement(b);
        });

        // Assemble the layout: input panel on top, book list in middle, controls on
        // bottom
        root.add(input, BorderLayout.NORTH);
        root.add(scroll, BorderLayout.CENTER);
        root.add(controls, BorderLayout.SOUTH);

        frame.getContentPane().add(root);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}