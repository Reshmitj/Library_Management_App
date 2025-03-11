package edu.saumc.library.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.saumc.library.entity.Book;
import edu.saumc.library.entity.User;
import edu.saumc.library.service.BookService;
import edu.saumc.library.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final BookService bookService;

    public UserController(UserService userService,BookService bookService) {
        this.userService = userService;
		this.bookService = bookService;
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String name,
                               @RequestParam String email,
                               @RequestParam String password) {
        try {
            userService.registerUser(name, email, password);
            return "redirect:/login"; 
        } catch (Exception e) {
            return "redirect:/register?error=Email already exists!"; 
        }
    }
    
    @GetMapping("/add-user")
    public String showAddUserForm(Model model) {
        model.addAttribute("user", new User());
        return "add-user";
    }
    
 // Handle book search functionality
    @PostMapping("/search-books")
    public String searchBooks(@RequestParam String query, Model model) {
        List<Book> books = bookService.searchBooks(query);
        if (books.isEmpty()) {
            model.addAttribute("errorMessage", "No books found");
        } else {
            model.addAttribute("books", books);
        }
        return "search-books";
    }

    @PostMapping("/add-user")
    public String addUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        userService.saveUser(user); 
        redirectAttributes.addFlashAttribute("successMessage", "User added successfully!");
        return "redirect:/admin/manage-users";
    }
}
