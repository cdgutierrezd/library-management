package com.cdgutierrezd.library.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cdgutierrezd.library.dto.response.LoanResponseDTO;
import com.cdgutierrezd.library.entity.Book;
import com.cdgutierrezd.library.entity.Loan;
import com.cdgutierrezd.library.entity.User;
import com.cdgutierrezd.library.enums.LoanStatus;
import com.cdgutierrezd.library.exception.BusinessException;
import com.cdgutierrezd.library.exception.ErrorCode;
import com.cdgutierrezd.library.exception.ResourceNotFoundException;
import com.cdgutierrezd.library.mapper.LoanMapper;
import com.cdgutierrezd.library.repository.BookRepository;
import com.cdgutierrezd.library.repository.LoanRepository;
import com.cdgutierrezd.library.repository.UserRepository;
import com.cdgutierrezd.library.service.interfaces.LoanServiceInterface;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LoanServiceImpl implements LoanServiceInterface {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final LoanMapper loanMapper;

    @Override
    public Page<LoanResponseDTO> findAll(Pageable pageable) {
        Page<Loan> loans = loanRepository.findAll(pageable);
        return loans.map(loanMapper::toResponseDto);
    }

    @Override
    public LoanResponseDTO findById(Long id) {
        Loan loan = loanRepository.
                findById(id).
                orElseThrow(() -> new ResourceNotFoundException(ErrorCode.LOAN_NOT_FOUND, "Loan not found with id: " + id));
        return loanMapper.toResponseDto(loan);
    }

    @Override
    public Page<LoanResponseDTO> findByStatus(LoanStatus status, Pageable pageable) {
        Page<Loan> loans = loanRepository.findByStatus(status, pageable);
        return loans.map(loanMapper::toResponseDto);
    }

    @Override
    public List<LoanResponseDTO> findByBookId(Long bookId) {
        List<Loan> loans = loanRepository.findByBookId(bookId);
        return loanMapper.toResponseDtoList(loans);
    }

    @Override
    public List<LoanResponseDTO> findByUserId(Long userId) {
        List<Loan> loans = loanRepository.findByUserId(userId);
        return loanMapper.toResponseDtoList(loans);
    }

    @Override
    public List<LoanResponseDTO> findActiveByUserId(Long userId) {
        List<Loan> loans = loanRepository.findByStatusAndUserId(LoanStatus.ACTIVE, userId);
        return loanMapper.toResponseDtoList(loans);
    }

    @Override
    @Transactional
    public LoanResponseDTO createLoan(Long bookId, Long userId) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.BOOK_NOT_FOUND, "Book not found with id: " + bookId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND, "User not found with id: " + userId));

        // Check if book has available stock
        if (book.getAvailableStock() <= 0) {
            throw new BusinessException(ErrorCode.BOOK_NO_STOCK, "No available copies for book: " + book.getTitle());
        }

        // Decrease available stock
        book.setAvailableStock(book.getAvailableStock() - 1);
        bookRepository.save(book);

        Loan loan = new Loan();
        loan.setBook(book);
        loan.setUser(user);
        // loanDate, dueDate, status set automatically by @PrePersist

        Loan savedLoan = loanRepository.save(loan);
        return loanMapper.toResponseDto(savedLoan);
    }


    @Override
    @Transactional
    public LoanResponseDTO returnBook(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.LOAN_NOT_FOUND, "Loan not found with id: " + loanId));

        if (loan.getStatus() != LoanStatus.ACTIVE) {
            throw new BusinessException(ErrorCode.LOAN_NOT_ACTIVE, "Loan is not active. Current status: " + loan.getStatus());
        }

        loan.setStatus(LoanStatus.RETURNED);
        loan.setReturnDate(LocalDate.now());

        // Increase available stock
        Book book = loan.getBook();
        book.setAvailableStock(book.getAvailableStock() + 1);
        bookRepository.save(book);

        Loan returnedLoan = loanRepository.save(loan);
        return loanMapper.toResponseDto(returnedLoan);
    }

}
