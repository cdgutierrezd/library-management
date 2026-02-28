package com.cdgutierrezd.library.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // Book errors (BOOK_XXX)
    BOOK_NOT_FOUND("BOOK_001", "Book not found"),
    BOOK_HAS_ACTIVE_LOANS("BOOK_002", "Cannot delete book with active loans"),
    BOOK_NO_STOCK("BOOK_003", "No available copies for this book"),
    
    // User errors (USER_XXX)
    USER_NOT_FOUND("USER_001", "User not found"),
    USER_EMAIL_DUPLICATE("USER_002", "Email already exists"),
    USER_HAS_ACTIVE_LOANS("USER_003", "Cannot delete user with active loans"),
    
    // Loan errors (LOAN_XXX)
    LOAN_NOT_FOUND("LOAN_001", "Loan not found"),
    LOAN_NOT_ACTIVE("LOAN_002", "Loan is not active"),
    LOAN_ALREADY_RETURNED("LOAN_003", "Loan has already been returned"),
    
    // Validation errors (VAL_XXX)
    VALIDATION_ERROR("VAL_001", "Validation failed"),
    
    // Generic errors (GEN_XXX)
    INTERNAL_ERROR("GEN_001", "An unexpected error occurred");

    private final String code;
    private final String defaultMessage;

    ErrorCode(String code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }
}
