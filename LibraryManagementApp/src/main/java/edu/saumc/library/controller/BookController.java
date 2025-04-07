package edu.saumc.library.controller;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        List<Book> books = (query == null || query.trim().isEmpty())
                ? bookService.getAllBooks()
                : bookService.searchBooks(query);
        model.addAttribute("books", books);
        return "fragments/searchResults :: results";
    }

    @GetMapping("/borrow-books")
    public String borrowBooks(Model model) {
        List<Book> books = bookService.getAvailableBooks();
        model.addAttribute("books", books);
        return "borrow-books";
    }

    @PostMapping("/borrow-books")
    public String borrowBook(@RequestParam("bookId") Long bookId, Model model) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean success = borrowService.borrowBook(bookId, userEmail);

        model.addAttribute(success ? "successMessage" : "errorMessage",
                success ? "Book borrowed successfully!" : "Failed to borrow the book.");

        model.addAttribute("books", bookService.getAvailableBooks());
        return "borrow-books";
    }

    @GetMapping("/return-books")
    public String returnBooks(Model model) {
        List<BorrowedBook> borrowedBooks = borrowService.getAllBorrowedBooks();
        model.addAttribute("borrowedBooks", borrowedBooks);
        return "return-books";
    }

    @PostMapping("/return-books/return")
    public String returnBook(@RequestParam("bookId") Long bookId, Model model) {
        boolean success = borrowService.returnBook(bookId);
        model.addAttribute(success ? "successMessage" : "errorMessage",
                success ? "Book returned successfully!" : "Failed to return the book.");
        model.addAttribute("borrowedBooks", borrowService.getAllBorrowedBooks());
        return "return-books";
    }

    @PostMapping("/extend-loan")
    public String requestLoanExtension(@RequestParam("bookId") Long bookId, Model model) {
        boolean success = borrowService.requestLoanExtension(bookId);
        model.addAttribute(success ? "successMessage" : "errorMessage",
                success ? "Loan extension request submitted." : "Extension already requested or failed.");
        model.addAttribute("borrowedBooks", borrowService.getAllBorrowedBooks());
        return "return-books";
    }
}
