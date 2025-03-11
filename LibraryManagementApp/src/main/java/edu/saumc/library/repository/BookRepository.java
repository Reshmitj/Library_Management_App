package edu.saumc.library.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.saumc.library.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // Find books by title (case-insensitive)
    List<Book> findByTitleContainingIgnoreCase(String title);

    // Find books by author (case-insensitive)
    List<Book> findByAuthorContainingIgnoreCase(String author);

    // Find books by category (case-insensitive)
    List<Book> findByCategoryContainingIgnoreCase(String category);

    // Find a book by both title and author
    Optional<Book> findByTitleAndAuthor(String title, String author);

    // Find books that are available (not borrowed)
    List<Book> findByAvailableTrue(); // This will find all books where available = true

    // Custom method to find a book by its ID and set it to available (used for returning the book)
    Optional<Book> findByIdAndAvailable(Long id, boolean available);
}
