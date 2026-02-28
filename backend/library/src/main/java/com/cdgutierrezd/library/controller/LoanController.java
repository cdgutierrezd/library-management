package com.cdgutierrezd.library.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cdgutierrezd.library.dto.request.LoanRequestDTO;
import com.cdgutierrezd.library.dto.response.LoanResponseDTO;
import com.cdgutierrezd.library.enums.LoanStatus;
import com.cdgutierrezd.library.service.interfaces.LoanServiceInterface;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/loans")
@AllArgsConstructor
public class LoanController {

    private final LoanServiceInterface loanService;

    @GetMapping
    public ResponseEntity<Page<LoanResponseDTO>> getAllLoans(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        return ResponseEntity.ok(loanService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanResponseDTO> getLoanById(@PathVariable Long id) {
        return ResponseEntity.ok(loanService.findById(id));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<LoanResponseDTO>> getLoansByStatus(
            @PathVariable LoanStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "loanDate") String sortBy
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        return ResponseEntity.ok(loanService.findByStatus(status, pageable));
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<LoanResponseDTO>> getLoansByBook(@PathVariable Long bookId) {
        return ResponseEntity.ok(loanService.findByBookId(bookId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LoanResponseDTO>> getLoansByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(loanService.findByUserId(userId));
    }

    @GetMapping("/user/{userId}/active")
    public ResponseEntity<List<LoanResponseDTO>> getActiveLoansByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(loanService.findActiveByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<LoanResponseDTO> createLoan(@Valid @RequestBody LoanRequestDTO dto) {
        LoanResponseDTO created = loanService.createLoan(dto.getBookId(), dto.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}/return")
    public ResponseEntity<LoanResponseDTO> returnBook(@PathVariable Long id) {
        return ResponseEntity.ok(loanService.returnBook(id));
    }
}
