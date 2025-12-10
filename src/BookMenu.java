import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class BookMenu {
    private Map<UUID, Book> books;

    public BookMenu() {
        this.books = new HashMap<>();
    }

    /**
     * Finds a book in the list by its UUID.
     * 
     * @param id The UUID of the book to find.
     * @return The Book object if found, otherwise null.
     */
    private Book findBookById(UUID id) {
        return books.get(id);
    }

    public void addBook(Book b) {
        books.put(b.getId(), b);
    }

    public void removeBook(Book b) {
        books.remove(b.getId());
    }

    public ArrayList<Book> listAllBooks() {
        return new ArrayList<>(books.values()); // Return a copy to prevent external modification
    }

    /**
     * Filters the list of books by their reading status.
     * 
     * @param readingStatus The status to filter by (e.g., "reading", "finished").
     * @return A new list containing only the books with the specified status.
     */
    public ArrayList<Book> listBooksByStatus(String readingStatus) {
        return books.values().stream()
                .filter(book -> book.getReadingStatus().equalsIgnoreCase(readingStatus))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Filters the list of books by their rating.
     * 
     * @param rating The rating to filter by.
     * @return A new list containing only the books with the specified rating.
     */
    public ArrayList<Book> listBooksByRating(int rating) {
        return books.values().stream()
                .filter(book -> book.getRating() == rating)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Updates the reading status of a specific book.
     * 
     * @param id            The id of the book to update.
     * @param readingStatus The new reading status.
     */
    public void updateBookReadingStatus(UUID id, String readingStatus) {
        Book book = findBookById(id);
        if (book != null) {
            book.setReadingStatus(readingStatus);
        }
    }

    /**
     * Updates the rating of a specific book.
     * 
     * @param id     The id of the book to update.
     * @param rating The new rating.
     */
    public void updateBookRating(UUID id, int rating) {
        Book book = findBookById(id);
        if (book != null) {
            book.setRating(rating);
        }
    }

    /**
     * Updates the review of a specific book.
     * 
     * @param id     The id of the book to update.
     * @param review The new review text.
     */
    public void updateBookReview(UUID id, String review) {
        Book book = findBookById(id);
        if (book != null) {
            book.setReview(review);
        }
    }

    /**
     * Returns a new list of all books sorted by the given key.
     *
     * @param sortBy One of "Title", "Author", or "Year" (case-sensitive as used in
     *               the UI).
     * @return A sorted ArrayList of books.
     */
    public ArrayList<Book> getSortedBooks(String sortBy) {
        Comparator<Book> comp = switch (sortBy) {
            case "Author" -> Book.BY_AUTHOR;
            case "Year" -> Book.BY_YEAR;
            default -> Book.BY_TITLE;
        };
        ArrayList<Book> list = listAllBooks();
        list.sort(comp);
        return list;
    }
}
