import java.util.Comparator;
import java.util.UUID;

// Book class stores info about a book including title, author, and user-provided data
public class Book implements Comparable<Book> {
    // --- Constants ---
    public static final String[] GENRES = { " ", "Action", "Adventure", "Animation", "Biography", "Comedy", "Crime",
            "Documentary", "Drama", "Family", "Fantasy", "History", "Horror", "Musical", "Mystery", "Romance",
            "Sci-Fi", "Sport", "Thriller", "War", "Western" };

    public static final String[] STATUSES = { " ", "to-be-read", "reading", "finished", "did-not-finish" };

    // Core book info
    private final UUID id;
    private String name;
    private String author;
    private int year; // ! optional; 0 will be unspecified
    private String genre;
    private String readingStatus; // status options include "to-be-read," "reading," "finished," and
                                  // "did-not-finish."
    private int rating; // Optional rating (0-5, where 0 = not rated)
    private String review; // Optional user review

    // Constructor for basic book info without optional fields
    public Book(String name, String author, String genre, String readingStatus) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.author = author;
        this.year = 0;
        this.genre = genre;
        this.readingStatus = readingStatus;
        this.rating = 0; // ! a rating of 0 means the user chose not to rate the book
        this.review = "";
    }

    // Constructor with optional year, rating, and review
    public Book(String name, String author, String genre, String readingStatus, int rating, String review) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.author = author;
        this.year = 0;
        this.genre = genre;
        this.readingStatus = readingStatus;
        if (rating < 0 || rating > 5) {
            System.out.println("Invalid rating. Rating is now set to 0 until further changes are made.");
            this.rating = 0;
        } else {
            this.rating = rating;
        }
        this.review = review;
    }

    // Constructor for UI with name, author, and year only
    public Book(String name, String author, int year) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.author = author;
        this.year = year;
        this.genre = "";
        this.readingStatus = "to-be-read";
        this.rating = 0;
        this.review = "";
    }

    // Getter methods for book information
    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public String getReadingStatus() {
        return readingStatus;
    }

    public int getRating() {
        return rating;
    }

    public String getReview() {
        return review;
    }

    public int getYear() {
        return year;
    }

    public UUID getId() {
        return id;
    }

    // Setter methods for updating book information
    public void setName(String name) {
        this.name = name;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setReadingStatus(String readingStatus) {
        this.readingStatus = readingStatus;
    }

    public void setRating(int rating) {
        if (rating < 0 || rating > 5) {
            System.out.println("Invalid rating. Please enter a rating value between 0 and 5.");
            return;
        }
        this.rating = rating;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public void setYear(int year) {
        this.year = year;
    }

    // Returns a formatted string of detailed book information
    public String getFormattedInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("Title: ").append(name).append("\n");
        sb.append("Author: ").append(author).append("\n");
        if (year > 0)
            sb.append("Year: ").append(year).append("\n");
        if (genre != null && !genre.trim().isEmpty())
            sb.append("Genre: ").append(genre).append("\n");
        sb.append("Status: ").append(readingStatus).append("\n");
        if (rating > 0)
            sb.append("Rating: ").append(rating).append("/5\n");
        if (!review.isEmpty())
            sb.append("Review: ").append(review).append("\n");
        return sb.toString();
    }

    // Prints detailed book information to the console
    public void printInfo() {
        System.out.println(getFormattedInfo());
    }

    // Returns a formatted string for display in the UI (e.g., "Title by Author
    // (2023)")
    @Override
    public String toString() {
        return name + " by " + author + (year > 0 ? " (" + year + ")" : "");
    }

    // Default comparison by book title (ignoring case)
    @Override
    public int compareTo(Book o) {
        return this.name.compareToIgnoreCase(o.name);
    }

    // Static comparators for sorting books by different criteria
    public static final Comparator<Book> BY_TITLE = (a, b) -> a.name.compareToIgnoreCase(b.name);
    public static final Comparator<Book> BY_AUTHOR = (a, b) -> a.author.compareToIgnoreCase(b.author);
    public static final Comparator<Book> BY_YEAR = (a, b) -> Integer.compare(a.year, b.year);
}
