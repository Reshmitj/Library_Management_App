package edu.saumc.library.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.saumc.library.entity.BorrowedBook;

@Repository
public interface BorrowedBookRepository extends JpaRepository<BorrowedBook, Long> {
    Optional<BorrowedBook> findByBookId(Long bookId);
    List<BorrowedBook> findByUserEmail(String email);
    List<BorrowedBook> findByExtensionRequestedTrue();
}
