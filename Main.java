import javax.swing.*;
import java.awt.*;
import java.util.*;

// ! Main class creates and manages the BookTrackr GUI application
public class Main {

    // TODO: INCLUDE debugBox IN UML DIAGRAM 
    // TODO: incorporate bookmenu.java into this file
    // --- Fields from UML Diagram ---
    private JFrame frame;
    private DefaultListModel<Book> listModel;
    private JList<Book> bookList;
    private JComboBox<String> sortBox;
    private JButton sortBtn;
    private JButton removeBtn;
    private JButton openAddBookDialogBtn;
    private JCheckBox debugBox; 

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main app = new Main();
            app.createAndShowGui();
        });
    }

    private void createAndShowGui() {
        frame = new JFrame("BookTrackr"); // ? Creates the frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ? Closes the program on exit
        frame.setSize(640, 420);

        JPanel root = new JPanel(new BorderLayout(8, 8));

        // ! Button to open the "Add Book" dialog
        openAddBookDialogBtn = new JButton("Add New Book...");
        debugBox = new JCheckBox("Debug");
        JPanel topPanel = new JPanel(new BorderLayout(8, 8));
        topPanel.add(openAddBookDialogBtn, BorderLayout.WEST);
        topPanel.add(debugBox, BorderLayout.EAST);

        // ! Model and view for displaying the list of books
        listModel = new DefaultListModel<>();
        bookList = new JList<>(listModel);
        bookList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // ! Debug checkbox - prints selected book info to console when changed
        bookList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && debugBox.isSelected()) {
                Book selected = bookList.getSelectedValue();
                if (selected != null) {
                    selected.printInfo();
                }
            }
        });
        JScrollPane scroll = new JScrollPane(bookList);

        // ! Panel for sorting and removing books
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        sortBox = new JComboBox<>(new String[] { "Title", "Author", "Year" });
        sortBtn = new JButton("Sort");
        removeBtn = new JButton("Remove Selected");
        controls.add(new JLabel("Sort by:"));
        controls.add(sortBox);
        controls.add(sortBtn);
        controls.add(removeBtn);

        openAddBookDialogBtn.addActionListener(e -> handleAddBook());

        // Remove Selected button - removes the selected book from the list
        removeBtn.addActionListener(e -> handleRemoveBook());

        // Sort button - sorts books by the selected category (Title, Author, or Year)
        sortBtn.addActionListener(e -> handleSortBooks());

        // ! Assemble the layout: input panel on top, book list in middle, controls on
        // ! bottom
        root.add(topPanel, BorderLayout.NORTH);
        root.add(scroll, BorderLayout.CENTER);
        root.add(controls, BorderLayout.SOUTH);

        frame.getContentPane().add(root);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // ! --- Handler Methods (Reference UML Diagram) ---
    private void handleAddBook() {
        // ! Create the dialog
        JDialog addBookDialog = new JDialog(frame, "Add a New Book", true);
        addBookDialog.setLayout(new BorderLayout(8, 8));
        addBookDialog.setSize(500, 400);

        // ! Panel for user input (title, author, year)
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

        c.gridx = 0;
        c.gridy = 3;
        input.add(new JLabel("Genre:"), c);
        String[] genres = { "Horror", "Romance", "Comedy", "Fantasy" };
        JComboBox<String> genreBox = new JComboBox<>(genres);
        c.gridx = 1;
        input.add(genreBox, c);

        c.gridx = 0;
        c.gridy = 4;
        input.add(new JLabel("Rating (0-5):"), c);
        JTextField ratingField = new JTextField(3);
        c.gridx = 1;
        input.add(ratingField, c);

        c.gridx = 0;
        c.gridy = 5;
        input.add(new JLabel("Review:"), c);
        JTextArea reviewArea = new JTextArea(5, 24);
        reviewArea.setLineWrap(true);
        reviewArea.setWrapStyleWord(true);
        JScrollPane reviewScrollPane = new JScrollPane(reviewArea);
        c.gridx = 1;
        input.add(reviewScrollPane, c);

        // ! Panel for the "Add" and "Cancel" buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addBtn = new JButton("Add Book");
        JButton cancelBtn = new JButton("Cancel");
        buttonPanel.add(addBtn);
        buttonPanel.add(cancelBtn);

        // Add Book button - validates input and adds new book to list
        addBtn.addActionListener(ae -> {
            String t = titleField.getText().trim();
            String a = authorField.getText().trim();
            String yText = yearField.getText().trim();
            String genre = (String) genreBox.getSelectedItem();
            String ratingText = ratingField.getText().trim();
            String review = reviewArea.getText().trim();

            if (t.isEmpty() || a.isEmpty()) {
                JOptionPane.showMessageDialog(addBookDialog, "Please enter both Title and Author.", "Missing data",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (review.length() > 500) {
                JOptionPane.showMessageDialog(addBookDialog, "Review must be 500 characters or less.");
                return;
            }

            int y = 0;
            if (!yText.isEmpty()) {
                try {
                    y = Integer.parseInt(yText);
                    if (y > 9999) {
                        JOptionPane.showMessageDialog(addBookDialog, "Year must be four digits or less.");
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(addBookDialog, "Year must be a number.");
                    return;
                }
            }

            int rating = 0;
            if (!ratingText.isEmpty()) {
                try {
                    rating = Integer.parseInt(ratingText);
                    if (rating < 0 || rating > 5) {
                        JOptionPane.showMessageDialog(addBookDialog, "Rating must be between 0 and 5.");
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(addBookDialog, "Rating must be a number.");
                    return;
                }
            }

            Book b = new Book(t, a, genre, "to-be-read", rating, review);
            b.setYear(y);

            listModel.addElement(b);
            addBookDialog.dispose(); // Close the dialog
        });

        // Cancel button - closes the dialog
        cancelBtn.addActionListener(ae -> addBookDialog.dispose());

        addBookDialog.add(input, BorderLayout.CENTER);
        addBookDialog.add(buttonPanel, BorderLayout.SOUTH);
        addBookDialog.setLocationRelativeTo(frame);
        addBookDialog.setVisible(true);
    }

    private void handleRemoveBook() {
        int idx = bookList.getSelectedIndex();
        if (idx != -1)
            listModel.remove(idx);
    }

    private void handleSortBooks() {
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
    }
}