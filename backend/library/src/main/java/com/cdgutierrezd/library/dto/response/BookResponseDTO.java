package com.cdgutierrezd.library.dto.response;

import lombok.Data;

@Data
public class BookResponseDTO {

    private Long id;
    private String title;
    private String author;
    private Integer publicationYear;
    private Integer totalStock;
    private Integer availableStock;
}
