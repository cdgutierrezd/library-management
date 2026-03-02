package com.cdgutierrezd.library.service;

import com.cdgutierrezd.library.dto.response.BookResponseDTO;
import com.cdgutierrezd.library.entity.Book;
import com.cdgutierrezd.library.exception.ResourceNotFoundException;
import com.cdgutierrezd.library.mapper.BookMapper;
import com.cdgutierrezd.library.repository.BookRepository;
import com.cdgutierrezd.library.service.impl.BookServiceImpl;
import com.cdgutierrezd.library.service.interfaces.LoanServiceInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private LoanServiceInterface loanService;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    void findById_existingId_returnsBookReponseDto(){
        //The book that would be stored in the database is created.
        Long bookId = 1L;

        Book bookDataBase = new Book();
        bookDataBase.setId(bookId);
        bookDataBase.setTitle("1984");
        bookDataBase.setAuthor("George Orwell");

        BookResponseDTO responseDTO = new BookResponseDTO();
        responseDTO.setId(bookId);
        responseDTO.setTitle("1984");
        responseDTO.setAuthor("George Orwell");

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(bookDataBase));
        when(bookMapper.toResponseDto(bookDataBase)).thenReturn(responseDTO);

        // When
        BookResponseDTO result = bookService.findById(bookId);

        // Then
        assertNotNull(result);
        assertEquals("1984", result.getTitle());
        assertEquals("George Orwell", result.getAuthor());
        verify(bookRepository).findById(bookId);
        verify(bookMapper).toResponseDto(bookDataBase);

    }

    @Test
    void findById_nonExistingId_throwsResourceNotFoundException() {

        Long bookId = 99L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> bookService.findById(bookId));

        verify(bookRepository).findById(bookId);
    }

    @Test
    void finAll_returnsMapperPage(){
        Pageable pageable = PageRequest.of(0, 10);

        Book book = new Book();
        book.setTitle("1984");

        Page<Book> bookPage = new PageImpl<>(List.of(book));

        BookResponseDTO dto = new BookResponseDTO();
        dto.setTitle("1984");

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toResponseDto(book)).thenReturn(dto);

        Page<BookResponseDTO> result = bookService.findAll(pageable);

        assertEquals(1, result.getTotalElements());
        verify(bookRepository).findAll(pageable);
    }
}
