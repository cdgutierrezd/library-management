package com.cdgutierrezd.library.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BookRequestDTO {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Author is required")
    private String author;

    @Min(value = 1900, message = "Publication year must be at least 1900")
    private Integer publicationYear;

    @Min(value = 1, message = "Total stock must be at least 1")
    private Integer totalStock;
}
