# Library Management System

This is a Spring Boot application that allows users to register and log in as library members. Admin access is also available for managing books, users, and borrowed books.

## Getting Started

Follow these steps to set up and run the application:

### 1. Prerequisites

* Ensure you have **Java 17** installed on your system.
    * You can download and install Java from [this link](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html).
* **To Check if Java is Installed and the Version:**
    * Open your Command Prompt (Windows) or Terminal (Mac/Linux).
    * Run the following command to check if Java is installed:

    ```bash
    java -version
    ```

### 2. Download and Set Up the Project

#### Download the ZIP

1.  Go to the GitHub Repository.
2.  Click on "Code" and select "Download ZIP".
3.  Extract the downloaded ZIP file.

#### Open in an IDE

1.  Open Spring Tool Suite (STS) or any preferred Java IDE.
2.  Import the project as a Maven project.

#### Run Maven Commands (Before Running the Application)

**From Terminal:**

```bash
mvn clean
mvn install
```

### From IDE (Spring Tool Suite - STS):

* Open the Spring Boot Perspective in STS.
* Locate the main class of the Spring Boot application.
* Right-click and select `Run As > Spring Boot App`.
* The application will start on `http://localhost:8080/`.

### 4. Using the Application

* Navigate to `http://localhost:8080/` in your browser.
* Register as a User.
* Log in using your registered credentials.
* **Admin Login Credentials:**
    * Username: `admin@library.com`
    * Password: `adminpass15`

### 5. Features

* **User Module:**
    * Allows users to register, log in, search for books, borrow books, and return borrowed books.
* **Admin Module:**
    * Allows administrators to manage books, users, and track borrowed books.
* **Book Management:**
    * Admin can add, update, and delete books from the library system.
* **Borrowing and Returning Books:**
    * Users can borrow and return books.
    * Admin can view borrowed books.

### 6. Troubleshooting

* If you face any issues, ensure that all dependencies are correctly resolved using Maven.
* Check that your database is set up with sample data (you can use the SQL scripts provided).
* Verify that the application is running on `http://localhost:8080/` in your browser.
