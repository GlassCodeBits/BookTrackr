import javax.swing.*;
import java.awt.*;
// no direct java.util imports needed here

// ! Main class creates and manages the BookTrackr GUI application
public class Main {

    // --- UI Components ---
    private JFrame frame; // The main window of the application.
    private DefaultListModel<Book> listModel; // The model that holds the list of books.
    private JList<Book> bookList; // The visual list that displays the books.
    private JComboBox<String> sortBox; // Dropdown for selecting sorting criteria.
    private JButton sortBtn; // Button to apply the selected sorting.
    private JButton removeBtn; // Button to remove the selected book.
    private JButton openAddBookDialogBtn; // Button to open the "Add Book" dialog.
    private JCheckBox debugBox; // Checkbox to enable/disable debug features.
    private JButton quickAddBtn; // Button to add a book with semi-random data (debug only).
    private JSplitPane splitPane; // Splits the main view between the book list and details.
    private JPanel rightPanel; // The panel on the right that shows book details.
    private JLabel coverPlaceholder; // A placeholder for the book cover image.
    // --- Detail Panel Components ---
    private JLabel authorLabel, yearLabel, genreLabel, statusLabel, ratingLabel;
    private JTextArea reviewArea;
    // --- Data Management ---
    private BookMenu bookMenu;

    // (Moved GENRES into Book.java as Book.GENRES)

    /**
     * The main entry point of the application.
     * It schedules the creation of the GUI on the Event Dispatch Thread.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main app = new Main();
            app.createAndShowGui();
        });
    }

    /**
     * Initializes and displays the main GUI components of the application.
     * This method sets up the main frame, panels, buttons, and listeners.
     */
    private void createAndShowGui() {
        // --- Frame Setup ---
        frame = new JFrame("BookTrackr");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 650);

        // --- Data Model ---
        bookMenu = new BookMenu();

        // --- Root Panel ---
        JPanel root = new JPanel(new BorderLayout(8, 8));

        // --- UI Component Creation ---
        JPanel topPanel = createTopPanel();
        JScrollPane scroll = createBookListPanel();
        rightPanel = createDetailsPanel();
        rightPanel.setVisible(false); // Initially hidden.

        // --- Split Pane ---
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scroll, rightPanel);
        splitPane.setDividerLocation(600);

        // --- Bottom Controls Panel ---
        JPanel controls = createControlsPanel();

        // --- Final Assembly ---
        root.add(topPanel, BorderLayout.NORTH);
        root.add(splitPane, BorderLayout.CENTER);
        root.add(controls, BorderLayout.SOUTH);

        // Add the root panel to the frame and make it visible.
        frame.getContentPane().add(root);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Updates the right-hand details panel with the information of the selected
     * book.
     * 
     * @param book The book whose details are to be displayed.
     */
    private void updateBookDetails(Book book) {
        if (book != null) {
            // Update the border title with the book's name
            rightPanel.setBorder(BorderFactory.createTitledBorder(book.getName()));
            // Display the book's title in the cover placeholder.
            coverPlaceholder.setText(
                    "<html><div style='text-align: center;'>" + book.getName() + "<br>Cover Placeholder</div></html>");

            // Populate the detail labels
            authorLabel.setText(book.getAuthor());
            yearLabel.setText(book.getYear() > 0 ? String.valueOf(book.getYear()) : "N/A");
            genreLabel.setText(book.getGenre() != null && !book.getGenre().trim().isEmpty() ? book.getGenre() : "N/A");
            statusLabel.setText(book.getReadingStatus());
            ratingLabel.setText(book.getRating() > 0 ? book.getRating() + "/5" : "Not Rated");
            reviewArea.setText(
                    book.getReview() != null && !book.getReview().trim().isEmpty() ? book.getReview() : "No review.");
            reviewArea.setCaretPosition(0); // Scroll to the top

        } else {
            // Reset the panel if no book is selected.
            rightPanel.setBorder(BorderFactory.createTitledBorder("Details"));
            coverPlaceholder.setText("Book Cover Placeholder");
            authorLabel.setText("");
            yearLabel.setText("");
            genreLabel.setText("");
            statusLabel.setText("");
            ratingLabel.setText("");
            reviewArea.setText("");
        }
    }

    /**
     * Handles opening the dialog to add a new book and adding the book to the
     * menu if created.
     */
    private void handleAddBook() {
        BookDialog dialog = new BookDialog(frame);
        dialog.setVisible(true); // Show the dialog and wait for user input

        if (dialog.isSaved()) {
            dialog.getNewBook().ifPresent(book -> {
                bookMenu.addBook(book);
                refreshBookList();
                bookList.setSelectedValue(book, true); // Select the newly added book
            });
        }
    }

    /**
     * Handles opening the dialog to edit a selected book and applying the changes.
     * 
     * @param bookToEdit The book to be edited.
     */
    private void handleEditBook(Book bookToEdit) {
        BookDialog dialog = new BookDialog(frame, bookToEdit);
        dialog.setVisible(true); // Show the dialog and wait for user input

        if (dialog.isSaved()) {
            // The dialog modifies the book object directly, so we just need to refresh.
            refreshBookList();
            updateBookDetails(bookToEdit); // Update the details panel with the new info
        }
    }

    /**
     * Removes the currently selected book from the list.
     */
    private void handleRemoveBook() {
        Book selected = bookList.getSelectedValue();
        // Ensure that a book is actually selected.
        if (selected != null) {
            int choice = JOptionPane.showConfirmDialog(frame,
                    "Are you sure you want to remove \"" + selected.getName() + "\"?",
                    "Confirm Removal",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (choice == JOptionPane.YES_OPTION) {
                bookMenu.removeBook(selected);
                refreshBookList();
            }
        }
    }

    /**
     * Sorts the books in the list based on the criteria selected in the sortBox.
     */
    private void handleSortBooks() {
        // Delegate sorting to BookMenu which returns a sorted list.
        String key = (String) sortBox.getSelectedItem();
        listModel.clear();
        for (Book b : bookMenu.getSortedBooks(key)) {
            listModel.addElement(b);
        }
    }

    /**
     * Refreshes the book list displayed in the UI from the BookMenu's data.
     */

    private void refreshBookList() {
        // Save the currently selected book so we can re-select it after the refresh.
        Book selected = bookList.getSelectedValue();
        listModel.clear();
        for (Book b : bookMenu.listAllBooks()) {
            listModel.addElement(b);
        }
        // If there was a selection before, try to restore it.
        if (selected != null) {
            bookList.setSelectedValue(selected, true);
        }
    }

    /**
     * A helper method to reduce boilerplate when adding a labeled field to a panel
     * with GridBagLayout. It adds a static label and a value label in a new row.
     *
     * @param panel     The parent panel to add the components to.
     * @param gbc       The GridBagConstraints object used for layout.
     * @param labelText The text for the static label (e.g., "Author:").
     * @param boldFont  The font to use for the static label.
     * @return The newly created JLabel for the value, which can be stored as a
     *         field.
     */
    private JLabel addDetailRow(JPanel panel, GridBagConstraints gbc, String labelText, Font boldFont) {
        // Configure and add the static label (e.g., "Author:")
        gbc.gridx = 0;
        gbc.weightx = 0; // Column for labels should not expand.
        JLabel staticLabel = new JLabel(labelText);
        staticLabel.setFont(boldFont);
        panel.add(staticLabel, gbc);

        // Configure and add the value label (the one that will be updated)
        gbc.gridx = 1;
        gbc.weightx = 1; // Column for data should take up the remaining space.
        JLabel valueLabel = new JLabel();
        panel.add(valueLabel, gbc);

        gbc.gridy++; // Move to the next row for the subsequent call.
        return valueLabel;
    }

    /**
     * Creates and returns the top panel containing action buttons.
     * 
     * @return The configured top panel.
     */
    private JPanel createTopPanel() {
        openAddBookDialogBtn = new JButton("Add New Book...");
        openAddBookDialogBtn.addActionListener(e -> handleAddBook());

        // Panel for debug controls
        JPanel rightPanelLayout = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        quickAddBtn = new JButton("Quick Add");
        quickAddBtn.setVisible(false);
        quickAddBtn.addActionListener(e -> handleQuickAdd());

        debugBox = new JCheckBox("Debug");
        debugBox.addActionListener(e -> quickAddBtn.setVisible(debugBox.isSelected()));

        rightPanelLayout.add(quickAddBtn);
        rightPanelLayout.add(debugBox);

        JPanel topPanel = new JPanel(new BorderLayout(8, 8));
        topPanel.add(openAddBookDialogBtn, BorderLayout.WEST);
        topPanel.add(rightPanelLayout, BorderLayout.EAST);
        return topPanel;
    }

    /**
     * Creates and returns the scrollable panel for the book list.
     * 
     * @return The configured book list panel.
     */
    private JScrollPane createBookListPanel() {
        listModel = new DefaultListModel<>();
        bookList = new JList<>(listModel);
        bookList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Book selected = bookList.getSelectedValue();
                boolean isBookSelected = selected != null;

                rightPanel.setVisible(isBookSelected);

                if (isBookSelected) {
                    if (debugBox.isSelected()) {
                        selected.printInfo();
                    }
                    updateBookDetails(selected);
                    splitPane.setDividerLocation(0.67);
                } else {
                    updateBookDetails(null);
                }
            }
        });
        return new JScrollPane(bookList);
    }

    /**
     * Creates and returns the details panel that displays information about the
     * selected book.
     * 
     * @return The configured details panel.
     */
    private JPanel createDetailsPanel() {
        // --- Cover Panel ---
        coverPlaceholder = new JLabel("Book Cover Placeholder", SwingConstants.CENTER);
        coverPlaceholder.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        coverPlaceholder.setPreferredSize(new Dimension(200, 300));

        JButton editBtn = new JButton("Edit");
        editBtn.addActionListener(e -> {
            Book selected = bookList.getSelectedValue();
            if (selected != null) {
                handleEditBook(selected);
            }
        });

        JPanel coverPanel = new JPanel(new BorderLayout(8, 8));
        coverPanel.add(coverPlaceholder, BorderLayout.CENTER);
        coverPanel.add(editBtn, BorderLayout.SOUTH);

        // --- Details Tab ---
        JPanel detailsTabPanel = new JPanel(new GridBagLayout());
        detailsTabPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 4, 2, 4);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 0;

        Font boldFont = new Font(detailsTabPanel.getFont().getName(), Font.BOLD, detailsTabPanel.getFont().getSize());
        authorLabel = addDetailRow(detailsTabPanel, gbc, "Author:", boldFont);
        yearLabel = addDetailRow(detailsTabPanel, gbc, "Year:", boldFont);
        genreLabel = addDetailRow(detailsTabPanel, gbc, "Genre:", boldFont);
        statusLabel = addDetailRow(detailsTabPanel, gbc, "Status:", boldFont);
        ratingLabel = addDetailRow(detailsTabPanel, gbc, "Rating:", boldFont);

        // --- Review Tab ---
        reviewArea = new JTextArea();
        reviewArea.setEditable(false);
        reviewArea.setLineWrap(true);
        reviewArea.setWrapStyleWord(true);
        JPanel reviewTabPanel = new JPanel(new BorderLayout());
        reviewTabPanel.add(new JScrollPane(reviewArea), BorderLayout.CENTER);

        // --- Tabbed Pane ---
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Details", detailsTabPanel);
        tabbedPane.addTab("Review", reviewTabPanel);

        // --- Final Assembly ---
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.add(coverPanel, BorderLayout.NORTH);
        panel.add(tabbedPane, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createTitledBorder("Details"));
        return panel;
    }

    /**
     * Creates and returns the bottom controls panel.
     * 
     * @return The configured controls panel.
     */
    private JPanel createControlsPanel() {
        sortBox = new JComboBox<>(new String[] { "Title", "Author", "Year" });
        sortBtn = new JButton("Sort");
        sortBtn.addActionListener(e -> handleSortBooks());

        removeBtn = new JButton("Remove Selected");
        removeBtn.addActionListener(e -> handleRemoveBook());

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        controls.add(new JLabel("Sort by:"));
        controls.add(sortBox);
        controls.add(sortBtn);
        controls.add(removeBtn);
        return controls;
    }

    /**
     * Handles the action of the "Quick Add" button to add a semi-random book for
     * debugging.
     */
    private void handleQuickAdd() {
        java.util.Random rand = new java.util.Random();
        int randomNum = rand.nextInt(1000);
        String randomlet = String.valueOf((char) (rand.nextInt(26) + 'A'));
        String title = randomlet + ".Vitle " + randomNum;
        String author = randomlet + randomNum;
        int year = 2000 + rand.nextInt(100);
        String genre = Book.GENRES[1 + rand.nextInt(Book.GENRES.length - 1)];
        int rating = 1 + rand.nextInt(5); // 1-5 rating
        String review = "Generic detailed text that tests multi-line + Title: '" + title + "'.\n"
                + "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod"
                + "sed ut perspiciatis unde omnis iste natus error sit voluptatem. At vero eos et "
                + "excepturi sint occaecati cupiditate non provident, similique sunt in culpa";

        Book b = new Book(title, author, genre, "to-be-read", rating, review);
        b.setYear(year);
        bookMenu.addBook(b);
        refreshBookList();
    }
}