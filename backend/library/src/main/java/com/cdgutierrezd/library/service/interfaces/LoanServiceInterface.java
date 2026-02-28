package com.cdgutierrezd.library.service.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cdgutierrezd.library.dto.response.LoanResponseDTO;
import com.cdgutierrezd.library.enums.LoanStatus;

public interface LoanServiceInterface {

    Page<LoanResponseDTO> findAll(Pageable pageable);

    LoanResponseDTO findById(Long id);

    Page<LoanResponseDTO> findByStatus(LoanStatus status, Pageable pageable);

    List<LoanResponseDTO> findByBookId(Long bookId);

    List<LoanResponseDTO> findByUserId(Long userId);

    List<LoanResponseDTO> findActiveByUserId(Long userId);

    LoanResponseDTO createLoan(Long bookId, Long userId);

    LoanResponseDTO returnBook(Long loanId);
}
