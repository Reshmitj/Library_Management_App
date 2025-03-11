package edu.saumc.library.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import edu.saumc.library.entity.Book;
import edu.saumc.library.entity.BorrowedBook;
import edu.saumc.library.entity.User;
import edu.saumc.library.repository.BookRepository;
import edu.saumc.library.repository.BorrowedBookRepository;
import edu.saumc.library.repository.UserRepository;

@Service
public class BorrowService {
    private final BorrowedBookRepository borrowedBookRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public BorrowService(BorrowedBookRepository borrowedBookRepository, BookRepository bookRepository, UserRepository userRepository) {
        this.borrowedBookRepository = borrowedBookRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    // Borrow a book
    public boolean borrowBook(Long bookId, String userEmail) {
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        if (bookOpt.isPresent() && bookOpt.get().isAvailable()) {
            Book book = bookOpt.get();

            // Fetch the user by their email
            User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found"));

            // Create a new BorrowedBook instance and set the user and book
            BorrowedBook borrowedBook = new BorrowedBook(user, book);
            
            // Save the borrowed book
            borrowedBookRepository.save(borrowedBook);

            // Mark the book as unavailable and save it
            book.setAvailable(false);
            bookRepository.save(book);

            return true;
        }
        return false;
    }


    // Return a book
    public boolean returnBook(Long bookId) {
        Optional<BorrowedBook> borrowedBookOpt = borrowedBookRepository.findByBookId(bookId);

        if (borrowedBookOpt.isPresent()) {
            BorrowedBook borrowedBook = borrowedBookOpt.get();
            borrowedBookRepository.delete(borrowedBook); // Delete the borrowed record

            // Make the book available again
            Book book = borrowedBook.getBook();
            book.setAvailable(true);
            bookRepository.save(book); // Update book availability

            return true;
        }
        return false; // Return false if the book was not found in borrowed books
    }

    // Get all borrowed books
    public List<BorrowedBook> getAllBorrowedBooks() {
        return borrowedBookRepository.findAll();
    }
}
