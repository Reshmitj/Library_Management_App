package edu.saumc.library.controller;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.saumc.library.entity.Book;
import edu.saumc.library.entity.BorrowedBook;
import edu.saumc.library.service.BookService;
import edu.saumc.library.service.BorrowService;

@Controller
public class BookController {

    private final BookService bookService;
    private final BorrowService borrowService;

    public BookController(BookService bookService, BorrowService borrowService) {
        this.bookService = bookService;
        this.borrowService = borrowService;
    }

    @GetMapping("/search-books")
    public String searchBooks(@RequestParam(required = false) String query, Model model) {
        List<Book> books;

        // Handle the search query and get results from the service
        if (query == null || query.trim().isEmpty()) {
            books = bookService.getAllBooks(); // Fetch all books if the query is empty
        } else {
            books = bookService.searchBooks(query); // Fetch filtered books based on the query
        }

        model.addAttribute("books", books);
        return "fragments/searchResults :: results"; // Return only the fragment with the results
    }


    @GetMapping("/borrow-books")
    public String borrowBooks(Model model) {
        // Get all available books for borrowing
        List<Book> books = bookService.getAvailableBooks();
        model.addAttribute("books", books);
        return "borrow-books"; // Show the borrow-books.html page
    }

    @PostMapping("/borrow-books")
    public String borrowBook(@RequestParam("bookId") Long bookId, Model model) {
        // Fetch the email of the authenticated user using SecurityContextHolder
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        
        boolean success = borrowService.borrowBook(bookId, userEmail); // Pass email to the service

        // Fetch the updated list of available books
        List<Book> availableBooks = bookService.getAvailableBooks();

        if (success) {
            model.addAttribute("successMessage", "Book borrowed successfully!");
        } else {
            model.addAttribute("errorMessage", "Failed to borrow the book. Please try again.");
        }

        model.addAttribute("books", availableBooks);
        return "borrow-books"; // Stay on the borrow-books page to display results and messages
    }

    @GetMapping("/return-books")
    public String returnBooks(Model model) {
        List<BorrowedBook> borrowedBooks = borrowService.getAllBorrowedBooks();
        
        // Debugging: Check if borrowedBooks is empty or null
        if (borrowedBooks == null || borrowedBooks.isEmpty()) {
            System.out.println("No borrowed books found!");
        }

        model.addAttribute("borrowedBooks", borrowedBooks);
        return "return-books";
    }


    @PostMapping("/return-books/return")
    public String returnBook(@RequestParam("bookId") Long bookId, Model model) {
        boolean success = borrowService.returnBook(bookId);

        if (success) {
            model.addAttribute("successMessage", "Book returned successfully!");
        } else {
            model.addAttribute("errorMessage", "Failed to return the book. Please try again.");
        }

        // Get the updated list of borrowed books
        List<BorrowedBook> borrowedBooks = borrowService.getAllBorrowedBooks();
        model.addAttribute("borrowedBooks", borrowedBooks);

        return "return-books"; // Stay on the return-books page to display the updated list
    }
}
