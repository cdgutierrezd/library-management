package com.cdgutierrezd.library.entity;

import com.cdgutierrezd.library.enums.LoanStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "loans")
@Data
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "loan_date", nullable = false, updatable = false)
    private LocalDateTime loanDate;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    @PrePersist
    protected void onCreate() {
        loanDate = LocalDateTime.now();
        dueDate = LocalDate.now().plusDays(7);
        status = LoanStatus.ACTIVE;
    }

}