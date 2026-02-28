package com.cdgutierrezd.library.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.cdgutierrezd.library.enums.LoanStatus;

import lombok.Data;

@Data
public class LoanResponseDTO {

    private Long id;
    private BookResponseDTO book;
    private UserResponseDTO user;
    private LocalDateTime loanDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private LoanStatus status;
}
