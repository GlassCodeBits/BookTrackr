### Current todo:
+ Something we probably forgot probably idk lol
---
# BookTrackr App - Core Features/Requirements
---
## Adding/Removing Books

The program must allow the user to add and remove books to and from their library. Adding books is crucial for a user to build a library and track their hobby, and removing books is necessary as users may want to remove books from their "to-be-read" list.

* **When adding a book, the user should be able to input:**
    * Name
    * Author
    * Year
    * Genre
    * Reading status (one of: "to-be-read," "reading," "finished," "did-not-finish")
    * *Optional:* Review and rating (0-5)
* **When removing a book:** The user should be able to click a **"Remove selected"** button that will remove the book and all of its attributes from the user's library.

## Update Existing Books

The program must allow the user to update details of books they had created previously through an **"Edit"** button. Users will frequently update a book's reading status, and will eventually update a book's review/rating after finishing it, making an update feature crucial for the building of an efficient book tracker.

## Prevent Invalid User Input

The program must ensure that the user inputs the correct information prompted by the program for the tracker to function correctly when being sorted or accurately saving input from the user.

* **Validations must include:**
    * Ensuring the rating is a number between 0 and 5. (A rating of 0 will mean the user chose not to rate the book).
    * Ensuring required fields (name, author, year, genre, reading status) are not left unanswered.
    * Ensuring a book's reading status always equals one of the four allowed options:
        * "To-be-read"
        * "Reading"
        * "Finished"
        * "did-not-finish"

## Store Books in a Collection

The program must create a collection of books whose size can change frequently. This is most easily achieved with an **ArrayList**. This will be necessary for adding/removing books and printing the library in an ordered manner.

## Sort Library by Title, Author, or Year

The program must allow the user to sort their library by a book's title, author, or year to help users find books efficiently and organize their library personally. A **"Sort by"** feature should be present in the program that allows the user to choose among these three options, which will be sorted accordingly.

* Title or author will be sorted **alphabetically (A → Z)**.
* Year published will be sorted **ascendingly (earliest to latest)**.
