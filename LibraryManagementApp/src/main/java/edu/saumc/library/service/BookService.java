package edu.saumc.library.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import edu.saumc.library.entity.Book;
import edu.saumc.library.entity.BorrowedBook;
import edu.saumc.library.repository.BookRepository;
import edu.saumc.library.repository.BorrowedBookRepository;

@Service
public class BookService {

	 private final BookRepository bookRepository;
	    private final BorrowedBookRepository borrowedBookRepository;

	    public BookService(BookRepository bookRepository, BorrowedBookRepository borrowedBookRepository) {
	        this.bookRepository = bookRepository;
	        this.borrowedBookRepository = borrowedBookRepository;
	    }
 

    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    public List<Book> searchBooks(String query) {
        return bookRepository.findByTitleContainingIgnoreCase(query);
    }

    public void addBook(Book book) {
        // Check if a book with the same title and author already exists
        Optional<Book> existingBook = bookRepository.findByTitleAndAuthor(book.getTitle(), book.getAuthor());
        if (existingBook.isPresent()) {
            throw new RuntimeException("Book with this title and author already exists!");
        }

        bookRepository.save(book);
    }
    
    

    public void updateBook(Long id, String title, String author, String category, boolean available) {
        Optional<Book> bookOpt = bookRepository.findById(id);
        if (bookOpt.isPresent()) {
            Book book = bookOpt.get();
            book.setTitle(title);
            book.setAuthor(author);
            book.setCategory(category);
            book.setAvailable(available);
            bookRepository.save(book);
        }
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
    
    public boolean borrowBook(Long bookId) {
        Book book = bookRepository.findById(bookId).orElse(null);
        if (book != null && book.isAvailable()) {
            // Mark the book as borrowed (set available to false)
            book.setAvailable(false);
            bookRepository.save(book);

            // Save the borrowed book entry
            BorrowedBook borrowedBook = new BorrowedBook();
            borrowedBook.setBook(book);
            borrowedBook.setBorrowedDate(LocalDateTime.now());
            borrowedBookRepository.save(borrowedBook);
            return true;
        }
        return false;
    }

    public boolean returnBook(Long bookId) {
        Optional<BorrowedBook> borrowedBookOpt = borrowedBookRepository.findByBookId(bookId); // This returns an Optional<BorrowedBook>
        
        if (borrowedBookOpt.isPresent()) {  // Check if the Optional contains a value
            BorrowedBook borrowedBook = borrowedBookOpt.get();  // Extract the value (BorrowedBook) from the Optional

            // Set the book available again
            Book book = borrowedBook.getBook();
            book.setAvailable(true);
            bookRepository.save(book);

            // Delete the borrowed book entry from the database
            borrowedBookRepository.delete(borrowedBook);
            return true;  // Successfully returned the book
        }

        return false;  // Book was not found (i.e., not borrowed)
    }


    public List<Book> getAvailableBooks() {
        return bookRepository.findByAvailableTrue();
    }

    public List<BorrowedBook> getBorrowedBooks() {
        return borrowedBookRepository.findAll();
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
}
