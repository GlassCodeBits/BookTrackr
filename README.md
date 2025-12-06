# 📂 Project Submission Guidelines

These guidelines detail the mandatory components and scoring breakdown for the project submission.

---

## 📝 1. Define Requirements (10% of the Project Grade)

List a few **core features or functional requirements** your project should meet.

* **Goal:** Clearly state what your system will do.
* **Example:** For a library management system, some requirements might include:
    * Adding new books to the catalog.
    * Issuing books to registered users.
    * Tracking and calculating book due dates.
    * Searching for books by title or author.

---

## 📐 2. Create a Class Diagram (10% of the Project Grade)

Design a **UML class diagram** that outlines the fundamental structure of your project.

* **Key Classes:** Identify the main entities in your project (e.g., `Book`, `User`, `Library`).
* **Relationships:** Show the connections between these classes (e.g., **Inheritance**, **Associations**, **Composition**).
* **Methods and Attributes:** Include the key methods (operations) and attributes (data fields) for each class.



[Image of a UML Class Diagram Example]


---

## 💻 3. Develop the Project (70% of the Project Grade)

Write the **Java source code** for your project.

* **Coding Practices:** Ensure the code is clean, readable, and follows good programming standards.
* **OOP Concepts:** Demonstrate the effective use of **Object-Oriented Programming (OOP)** concepts (e.g., encapsulation, inheritance, polymorphism).

---

## ✅ 4. Write Test Cases (10% of the Project Grade)

Provide a set of **test cases** that thoroughly demonstrate the functionality of your project.

* **Documentation Format:** Use the table format below to document your tests.
* **Required Information:** Each test case must clearly show the **Input data**, the **Expected Output**, and the **Actual Output** after running the program.

| Test Case ID | Test Case Description | Input | Expected Output | Actual Output | Status (Pass/Fail) |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **TC1** | Add valid student and grades | Student Name: John, Grades: [85, 90, 78] | Student John added with grades [85, 90, 78] | Student John added with grades [85, 90, 78] | Pass |
| **TC2** | Calculate average grade | Student Name: John | Average grade: 84.33 | Average grade: 84.33 | Pass |
| **TC3** | Determine letter grade (A) | Average Grade: 92 | Letter Grade: A | Letter Grade: A | Pass |
| **TC4** | Add invalid grade (negative value) | Student Name: Sarah, Grades: [85, -5, 78] | Error: Invalid grade | Error: Invalid grade | Pass |
| **TC5** | Calculate average for empty list | Student Name: Mark, Grades: [] | Error: No grades available to calculate | Error: No grades available | Pass |

---

## 📌 Important Notes

* **Submission Package:** Submit the following items within your ZIP file:
    1.  Project Idea
    2.  Requirements Document
    3.  Class Diagram
    4.  Source Code Files (Java)
    5.  Test Cases Document
* **Team Contribution (If applicable):** Highlight the role and responsibilities of each team member. Clearly specify who was responsible for which parts of the project (e.g., class design, coding, testing, documentation).
* **Code Quality:** Ensure that the code is **well-commented** and adheres to **good coding standards**.

Good luck!
---
*— Abdul*
