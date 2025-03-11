package edu.saumc.library.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.saumc.library.entity.Book;
import edu.saumc.library.entity.BorrowedBook;
import edu.saumc.library.entity.User;
import edu.saumc.library.service.BookService;
import edu.saumc.library.service.BorrowService;
import edu.saumc.library.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final BookService bookService;
    private final BorrowService borrowedBookService;
    private final UserService userService;

    public AdminController(BookService bookService, BorrowService borrowedBookService, UserService userService) {
        this.bookService = bookService;
        this.borrowedBookService = borrowedBookService;
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "admin-dashboard";
    }

    @GetMapping("/manage-books")
    public String manageBooks(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "manage-books"; 
    }

    @GetMapping("/add-book")
    public String addBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "add-book";
    }

    @PostMapping("/add-book")
    public String addBook(@RequestParam String title,
                          @RequestParam String author,
                          @RequestParam String category,
                          @RequestParam(defaultValue = "true") boolean available,
                          Model model) {
        if (title.trim().isEmpty() || author.trim().isEmpty() || category.trim().isEmpty()) {
            model.addAttribute("error", "All fields are required!");
            return "manage-books";
        }

        try {
            Book book = new Book(title, author, category);
            book.setAvailable(available);
            bookService.addBook(book);
            model.addAttribute("success", "Book added successfully!");
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }

        model.addAttribute("books", bookService.getAllBooks());
        return "manage-books";
    }

    @GetMapping("/update-book/{id}")
    public String updateBookForm(@PathVariable Long id, Model model) {
        Book book = bookService.getBookById(id);
        if (book == null) {
            return "redirect:/admin/manage-books";
        }
        model.addAttribute("book", book);
        return "update-book";
    }

    @PostMapping("/update-book")
    public String updateBook(@RequestParam Long id,
                             @RequestParam String title,
                             @RequestParam String author,
                             @RequestParam String category,
                             @RequestParam(defaultValue = "true") boolean available,
                             Model model) {
        bookService.updateBook(id, title, author, category, available);
        model.addAttribute("success", "Book updated successfully!");
        return "redirect:/admin/manage-books";
    }

    @PostMapping("/delete-book/{id}")
    public String deleteBook(@PathVariable Long id, Model model) {
        bookService.deleteBook(id);
        model.addAttribute("success", "Book deleted successfully!");
        return "redirect:/admin/manage-books";
    }


    @GetMapping("/view-borrowed-books")
    public String viewBorrowedBooks(Model model) {
        List<BorrowedBook> borrowedBooks = borrowedBookService.getAllBorrowedBooks();
        model.addAttribute("borrowedBooks", borrowedBooks);
        return "view-borrowed-books";  
    }

    @GetMapping("/manage-users")
    public String manageUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "manage-users";
    }
    
    @GetMapping("/edit-user/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);
        if (user == null) {
            return "redirect:/admin/manage-users";
        }
        model.addAttribute("user", user);
        return "edit-user";
    }

    @PostMapping("/update-user")
    public String updateUser(@RequestParam Long id,
                             @RequestParam String name,
                             @RequestParam String email,
                             @RequestParam String password) {
        userService.updateUser(id, name, email, password);
        return "redirect:/admin/manage-users";
    }

}
