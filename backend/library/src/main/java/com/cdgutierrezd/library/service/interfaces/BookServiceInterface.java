package com.cdgutierrezd.library.service.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cdgutierrezd.library.dto.request.BookRequestDTO;
import com.cdgutierrezd.library.dto.response.BookResponseDTO;

public interface BookServiceInterface {
    Page<BookResponseDTO> findAll(Pageable pageable);

    BookResponseDTO findById(Long id);

    Page<BookResponseDTO> searchByTitle(String title, Pageable pageable);

    Page<BookResponseDTO> searchByAuthor(String author, Pageable pageable);

    BookResponseDTO save(BookRequestDTO dto);

    BookResponseDTO update(Long id, BookRequestDTO dto);

    void deleteById(Long id);
}
