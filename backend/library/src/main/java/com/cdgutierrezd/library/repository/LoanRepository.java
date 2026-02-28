package com.cdgutierrezd.library.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cdgutierrezd.library.entity.Loan;
import com.cdgutierrezd.library.enums.LoanStatus;

@Repository
public interface LoanRepository extends JpaRepository<Loan,Long> {

    Page<Loan> findByStatus(LoanStatus status, Pageable pageable);

    List<Loan> findByBookId(Long bookId);

    List<Loan> findByUserId(Long userId);

    List<Loan> findByStatusAndUserId(LoanStatus status, Long userId);

}
