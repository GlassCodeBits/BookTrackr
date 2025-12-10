import javax.swing.*;
import java.awt.*;
import java.util.Optional;

/**
 * A dialog for adding a new book or editing an existing one.
 * It encapsulates all UI components, layout, and validation for the book form.
 */
public class BookDialog extends JDialog {

    // --- Fields ---
    private static final String[] GENRES = { " ", "Action", "Adventure", "Animation", "Biography", "Comedy", "Crime",
            "Documentary", "Drama", "Family", "Fantasy", "History", "Horror", "Musical", "Mystery", "Romance",
            "Sci-Fi", "Sport", "Thriller", "War", "Western" };
    private static final String[] STATUSES = { "to-be-read", "reading", "finished", "did-not-finish" };

    // UI Components
    private JTextField titleField, authorField, yearField, ratingField;
    private JComboBox<String> genreBox, statusBox;
    private JTextArea reviewArea;

    // Result tracking
    private boolean saved = false;
    private Book newBook = null;

    /**
     * Constructor for adding a new book.
     * 
     * @param owner The parent frame.
     */
    public BookDialog(Frame owner) {
        super(owner, "Add a New Book", true);
        buildLayout();
        setupAddActions();
        pack(); // Adjusts dialog size to fit its content
        Dimension size = getSize();
        setSize(new Dimension(size.width + 20, size.height + 20)); // Add padding
        setLocationRelativeTo(owner);
    }

    /**
     * Constructor for editing an existing book.
     * 
     * @param owner      The parent frame.
     * @param bookToEdit The book whose data will populate the form.
     */
    public BookDialog(Frame owner, Book bookToEdit) {
        super(owner, "Edit " + bookToEdit.getName(), true);
        buildLayout();
        populateFields(bookToEdit);
        setupEditActions(bookToEdit);
        pack();
        Dimension size = getSize();
        setSize(new Dimension(size.width + 20, size.height + 20)); // Add padding
        setLocationRelativeTo(owner);
    }

    /**
     * Builds the common UI layout for the dialog.
     */
    private void buildLayout() {
        setLayout(new BorderLayout(8, 8));

        // --- Input Panel ---
        JPanel input = new JPanel(new GridBagLayout());
        input.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);
        c.anchor = GridBagConstraints.WEST;

        // Title
        c.gridx = 0;
        c.gridy = 0;
        input.add(new JLabel("Title:"), c);
        titleField = new JTextField(24);
        c.gridx = 1;
        input.add(titleField, c);

        // Author
        c.gridx = 0;
        c.gridy = 1;
        input.add(new JLabel("Author:"), c);
        authorField = new JTextField(24);
        c.gridx = 1;
        input.add(authorField, c);

        // Year
        c.gridx = 0;
        c.gridy = 2;
        input.add(new JLabel("Year:"), c);
        yearField = new JTextField(6);
        c.gridx = 1;
        input.add(yearField, c);

        // Genre
        c.gridx = 0;
        c.gridy = 3;
        input.add(new JLabel("Genre:"), c);
        genreBox = new JComboBox<>(GENRES);
        c.gridx = 1;
        input.add(genreBox, c);

        // Status
        c.gridx = 0;
        c.gridy = 4;
        input.add(new JLabel("Status:"), c);
        statusBox = new JComboBox<>(STATUSES);
        c.gridx = 1;
        input.add(statusBox, c);
        statusBox.setVisible(false); // Only visible in edit mode

        // Rating
        c.gridx = 0;
        c.gridy = 5;
        input.add(new JLabel("Rating (0-5):"), c);
        ratingField = new JTextField(3);
        c.gridx = 1;
        input.add(ratingField, c);

        // Review
        c.gridx = 0;
        c.gridy = 6;
        input.add(new JLabel("Review:"), c);
        reviewArea = new JTextArea(5, 24);
        reviewArea.setLineWrap(true);
        reviewArea.setWrapStyleWord(true);
        JScrollPane reviewScrollPane = new JScrollPane(reviewArea);
        c.gridx = 1;
        input.add(reviewScrollPane, c);

        add(input, BorderLayout.CENTER);
    }

    /**
     * Populates the form fields with data from an existing book.
     */
    private void populateFields(Book book) {
        titleField.setText(book.getName());
        authorField.setText(book.getAuthor());
        yearField.setText(book.getYear() > 0 ? String.valueOf(book.getYear()) : "");
        genreBox.setSelectedItem(book.getGenre());
        statusBox.setSelectedItem(book.getReadingStatus());
        ratingField.setText(String.valueOf(book.getRating()));
        reviewArea.setText(book.getReview());

        // All fields are editable for an "edit" now
        statusBox.setVisible(true); // Status is editable
    }

    /**
     * Sets up the "Add" and "Cancel" buttons for the 'add' mode.
     */
    private void setupAddActions() {
        JButton addBtn = new JButton("Add Book");
        addBtn.addActionListener(e -> {
            if (validateInput()) {
                String t = titleField.getText().trim();
                String a = authorField.getText().trim();
                String yText = yearField.getText().trim();
                int y = yText.isEmpty() ? 0 : Integer.parseInt(yText);
                String genre = (String) genreBox.getSelectedItem();
                String ratingText = ratingField.getText().trim();
                int rating = ratingText.isEmpty() ? 0 : Integer.parseInt(ratingText);
                String review = reviewArea.getText().trim();

                this.newBook = new Book(t, a, genre, "to-be-read", rating, review);
                this.newBook.setYear(y);

                this.saved = true;
                dispose();
            }
        });

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(addBtn);
        buttonPanel.add(cancelBtn);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Sets up the "Save" and "Cancel" buttons for the 'edit' mode.
     */
    private void setupEditActions(Book book) {
        JButton saveBtn = new JButton("Save Changes");
        saveBtn.addActionListener(e -> {
            if (validateInput()) {
                // Get all values from the form
                String newTitle = titleField.getText().trim();
                String newAuthor = authorField.getText().trim();
                String yText = yearField.getText().trim();
                int newYear = yText.isEmpty() ? 0 : Integer.parseInt(yText);
                String newGenre = (String) genreBox.getSelectedItem();
                String newStatus = (String) statusBox.getSelectedItem();
                String ratingText = ratingField.getText().trim();
                int newRating = ratingText.isEmpty() ? 0 : Integer.parseInt(ratingText);
                String newReview = reviewArea.getText().trim();

                // Update the original book object directly with all new data
                book.setName(newTitle);
                book.setAuthor(newAuthor);
                book.setYear(newYear);
                book.setGenre(newGenre);
                book.setReadingStatus(newStatus);
                book.setRating(newRating);
                book.setReview(newReview);

                this.saved = true;
                dispose();
            }
        });

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Validates all user-editable fields.
     * 
     * @return true if all inputs are valid, false otherwise.
     */
    private boolean validateInput() {
        // Title and author are only validated in 'add' mode
        if (titleField.isEditable()) {
            if (titleField.getText().trim().isEmpty() || authorField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both Title and Author.", "Missing Data",
                        JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }

        if (reviewArea.getText().length() > 500) {
            JOptionPane.showMessageDialog(this, "Review must be 500 characters or less.");
            return false;
        }

        String yText = yearField.getText().trim();
        if (!yText.isEmpty()) {
            try {
                int y = Integer.parseInt(yText);
                if (y < 0 || y > 9999) {
                    JOptionPane.showMessageDialog(this, "Year must be a four-digit number.");
                    return false;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Year must be a valid number.");
                return false;
            }
        }

        String ratingText = ratingField.getText().trim();
        if (!ratingText.isEmpty()) {
            try {
                int r = Integer.parseInt(ratingText);
                if (r < 0 || r > 5) {
                    JOptionPane.showMessageDialog(this, "Rating must be between 0 and 5.");
                    return false;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Rating must be a valid number.");
                return false;
            }
        }
        return true;
    }

    /**
     * @return true if the dialog was closed via "Save" or "Add".
     */
    public boolean isSaved() {
        return saved;
    }

    /**
     * @return An Optional containing the new Book if one was created.
     */
    public Optional<Book> getNewBook() {
        return Optional.ofNullable(newBook);
    }
}
