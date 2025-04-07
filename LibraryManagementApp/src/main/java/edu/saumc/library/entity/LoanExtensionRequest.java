package edu.saumc.library.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class LoanExtensionRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "borrowed_book_id")
    private BorrowedBook borrowedBook;

    @Column(name = "requested_new_due_date")
    private LocalDateTime requestedNewDueDate;

    @Column(name = "status")
    private String status; // PENDING, APPROVED, REJECTED

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BorrowedBook getBorrowedBook() {
		return borrowedBook;
	}

	public void setBorrowedBook(BorrowedBook borrowedBook) {
		this.borrowedBook = borrowedBook;
	}

	public LocalDateTime getRequestedNewDueDate() {
		return requestedNewDueDate;
	}

	public void setRequestedNewDueDate(LocalDateTime requestedNewDueDate) {
		this.requestedNewDueDate = requestedNewDueDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

    
}

