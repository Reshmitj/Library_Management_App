package edu.saumc.library.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.saumc.library.entity.LoanExtensionRequest;

@Repository
public interface LoanExtensionRequestRepository extends JpaRepository<LoanExtensionRequest, Long> {
    List<LoanExtensionRequest> findByStatus(String status);
}

