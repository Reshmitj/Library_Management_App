package edu.saumc.library.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.saumc.library.entity.BorrowedBook;

@Repository
public interface BorrowedBookRepository extends JpaRepository<BorrowedBook, Long> {

    // Find BorrowedBook by Book ID (useful for returning a book)
    Optional<BorrowedBook> findByBookId(Long bookId);

    // Find BorrowedBooks by User's Email (useful for listing borrowed books for a user)
    List<BorrowedBook> findByUserEmail(String email);

}
