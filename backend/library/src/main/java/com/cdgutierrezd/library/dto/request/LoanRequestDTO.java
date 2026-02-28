package com.cdgutierrezd.library.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoanRequestDTO {

    @NotNull(message = "Book ID is required")
    private Long bookId;

    @NotNull(message = "User ID is required")
    private Long userId;
}
