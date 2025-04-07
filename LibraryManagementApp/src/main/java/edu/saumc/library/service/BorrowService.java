package edu.saumc.library.service;

import java.time.LocalDateTime;
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

    public BorrowService(BorrowedBookRepository borrowedBookRepository,
                         BookRepository bookRepository,
                         UserRepository userRepository) {
        this.borrowedBookRepository = borrowedBookRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    public boolean borrowBook(Long bookId, String userEmail) {
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        if (bookOpt.isPresent() && bookOpt.get().isAvailable()) {
            Book book = bookOpt.get();
            User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found"));

            BorrowedBook borrowedBook = new BorrowedBook(user, book);
            borrowedBookRepository.save(borrowedBook);

            book.setAvailable(false);
            bookRepository.save(book);

            return true;
        }
        return false;
    }

    public boolean returnBook(Long bookId) {
        Optional<BorrowedBook> borrowedBookOpt = borrowedBookRepository.findByBookId(bookId);
        if (borrowedBookOpt.isPresent()) {
            BorrowedBook borrowedBook = borrowedBookOpt.get();
            borrowedBookRepository.delete(borrowedBook);

            Book book = borrowedBook.getBook();
            book.setAvailable(true);
            bookRepository.save(book);

            return true;
        }
        return false;
    }

    public List<BorrowedBook> getAllBorrowedBooks() {
        return borrowedBookRepository.findAll();
    }

    public boolean requestLoanExtension(Long bookId) {
        Optional<BorrowedBook> borrowedBookOpt = borrowedBookRepository.findByBookId(bookId);
        if (borrowedBookOpt.isPresent()) {
            BorrowedBook borrowedBook = borrowedBookOpt.get();
            if (!borrowedBook.isExtensionRequested()) {
                borrowedBook.setExtensionRequested(true);
                borrowedBookRepository.save(borrowedBook);
                return true;
            }
        }
        return false;
    }

    public List<BorrowedBook> getPendingExtensionRequests() {
        return borrowedBookRepository.findByExtensionRequestedTrue();
    }

    public boolean approveExtension(Long borrowId, LocalDateTime newDueDate) {
        Optional<BorrowedBook> opt = borrowedBookRepository.findById(borrowId);
        if (opt.isPresent()) {
            BorrowedBook borrowedBook = opt.get();
            borrowedBook.setDueDate(newDueDate);
            borrowedBook.setExtensionRequested(false);
            borrowedBookRepository.save(borrowedBook);
            return true;
        }
        return false;
    }
}
