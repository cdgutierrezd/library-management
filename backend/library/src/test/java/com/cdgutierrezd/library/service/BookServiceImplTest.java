package com.cdgutierrezd.library.service;

import com.cdgutierrezd.library.dto.request.BookRequestDTO;
import com.cdgutierrezd.library.dto.response.BookResponseDTO;
import com.cdgutierrezd.library.dto.response.LoanResponseDTO;
import com.cdgutierrezd.library.entity.Book;
import com.cdgutierrezd.library.exception.BusinessException;
import com.cdgutierrezd.library.exception.ErrorCode;
import com.cdgutierrezd.library.exception.ResourceNotFoundException;
import com.cdgutierrezd.library.mapper.BookMapper;
import com.cdgutierrezd.library.repository.BookRepository;
import com.cdgutierrezd.library.service.impl.BookServiceImpl;
import com.cdgutierrezd.library.service.interfaces.LoanServiceInterface;
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
import static org.mockito.Mockito.*;

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
    void findAll_withMultipleBooks_returnsMappedPage() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);

        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("1984");

        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Animal Farm");

        Page<Book> bookPage = new PageImpl<>(List.of(book1, book2));

        BookResponseDTO dto1 = new BookResponseDTO();
        dto1.setId(1L);
        dto1.setTitle("1984");

        BookResponseDTO dto2 = new BookResponseDTO();
        dto2.setId(2L);
        dto2.setTitle("Animal Farm");

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toResponseDto(book1)).thenReturn(dto1);
        when(bookMapper.toResponseDto(book2)).thenReturn(dto2);

        // When
        Page<BookResponseDTO> result = bookService.findAll(pageable);

        // Then
        assertEquals(2, result.getTotalElements());
        assertEquals("1984", result.getContent().get(0).getTitle());
        assertEquals("Animal Farm", result.getContent().get(1).getTitle());
        verify(bookRepository).findAll(pageable);
        verify(bookMapper).toResponseDto(book1);
        verify(bookMapper).toResponseDto(book2);
    }

    @Test
    void save_validRequestDto_returnsSavedBookResponseDto() {

        // Given
        BookRequestDTO requestDto = new BookRequestDTO();
        requestDto.setTitle("1984");
        requestDto.setAuthor("George Orwell");

        Book bookWithoutId = new Book();
        bookWithoutId.setTitle("1984");
        bookWithoutId.setAuthor("George Orwell");

        Book savedBook = new Book();
        savedBook.setId(1L);
        savedBook.setTitle("1984");
        savedBook.setAuthor("George Orwell");

        BookResponseDTO responseDto = new BookResponseDTO();
        responseDto.setId(1L);
        responseDto.setTitle("1984");
        responseDto.setAuthor("George Orwell");

        // When
        when(bookMapper.toEntity(requestDto)).thenReturn(bookWithoutId);
        when(bookRepository.save(bookWithoutId)).thenReturn(savedBook);
        when(bookMapper.toResponseDto(savedBook)).thenReturn(responseDto);

        BookResponseDTO result = bookService.save(requestDto);

        // Then
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(1L, result.getId());
        assertEquals("1984", result.getTitle());
        assertEquals("George Orwell", result.getAuthor());
        verify(bookMapper).toEntity(requestDto);
        verify(bookRepository).save(bookWithoutId);
        verify(bookMapper).toResponseDto(savedBook);
    }

    @Test
    void update_validRequestDto_returnsUpdateBookResponseDto(){
        Long bookId = 1L;

        BookRequestDTO bookRequestDTO = new BookRequestDTO();
        bookRequestDTO.setTitle("1984");
        bookRequestDTO.setAuthor("George Orwell");
        bookRequestDTO.setPublicationYear(1949);
        bookRequestDTO.setTotalStock(100);

        Book oldBook = new Book();
        oldBook.setId(bookId);
        oldBook.setTitle("Old Tittle");
        oldBook.setAuthor("Old Author");
        oldBook.setPublicationYear(1111);
        oldBook.setTotalStock(0);

        BookResponseDTO bookResponseDTO = new BookResponseDTO();
        bookResponseDTO.setId(bookId);
        bookResponseDTO.setTitle("1984");
        bookResponseDTO.setAuthor("George Orwell");
        bookResponseDTO.setPublicationYear(1949);
        bookResponseDTO.setTotalStock(100);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(oldBook));
        when(bookRepository.save(oldBook)).thenReturn(oldBook);
        when(bookMapper.toResponseDto(oldBook)).thenReturn(bookResponseDTO);

        BookResponseDTO result = bookService.update(bookId, bookRequestDTO);

        assertNotNull(result);
        assertEquals(bookId, result.getId());
        assertEquals("1984", result.getTitle());
        assertEquals("George Orwell", result.getAuthor());
        assertEquals(1949, result.getPublicationYear());
        assertEquals(100, result.getTotalStock());

        assertEquals(bookId, result.getId());
        assertEquals("1984", oldBook.getTitle());
        assertEquals("George Orwell", oldBook.getAuthor());
        assertEquals(1949, oldBook.getPublicationYear());
        assertEquals(100, oldBook.getTotalStock());

        verify(bookRepository).findById(bookId);
        verify(bookRepository).save(oldBook);
        verify(bookMapper).toResponseDto(oldBook);
    }

    @Test
    void update_nonExistingBook_throwsResourceNotFoundException(){
        Long bookId = 99L;
        BookRequestDTO bookRequestDTO = new BookRequestDTO();

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> bookService.update(bookId,bookRequestDTO));

        verify(bookRepository).findById(bookId);
    }

    @Test
    void deleteById_existingBookNoLoans_deletesSuccessfully(){
        Long bookId = 1L;

        Book book = new Book();
        book.setId(bookId);
        book.setTitle("1984");
        book.setAuthor("George Orwell");

        // No hay préstamos activos
        when(loanService.findByBookId(bookId)).thenReturn(List.of());
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        doNothing().when(bookRepository).deleteById(bookId);

        // When
        bookService.deleteById(bookId);

        // Then
        verify(loanService).findByBookId(bookId);
        verify(bookRepository).findById(bookId);
        verify(bookRepository).deleteById(bookId);

    }

    @Test
    void deleteById_existingBookWithActiveLoans_throwsBusinessException() {
        Long bookId = 1L;

        Book book = new Book();
        book.setId(bookId);
        book.setTitle("1984");

        LoanResponseDTO activeLoan = new LoanResponseDTO();
        activeLoan.setId(1L);

        when(loanService.findByBookId(bookId)).thenReturn(List.of(activeLoan));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> bookService.deleteById(bookId));

        assertEquals(ErrorCode.BOOK_HAS_ACTIVE_LOANS, exception.getErrorCode());
        verify(loanService).findByBookId(bookId);
        verifyNoInteractions(bookRepository);
    }

    @Test
    void deleteById_nonExistingBook_throwsResourceNotFoundException() {
        Long bookId = 99L;

        when(loanService.findByBookId(bookId)).thenReturn(List.of());
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> bookService.deleteById(bookId));

        verify(loanService).findByBookId(bookId);
        verify(bookRepository).findById(bookId);
    }

    @Test
    void searchByTitle_existingTitle_returnsMatchingBooks() {
        String title = "1984";
        Pageable pageable = PageRequest.of(0, 10);

        Book book = new Book();
        book.setId(1L);
        book.setTitle("1984");

        Page<Book> bookPage = new PageImpl<>(List.of(book));

        BookResponseDTO responseDto = new BookResponseDTO();
        responseDto.setId(1L);
        responseDto.setTitle("1984");

        when(bookRepository.findByTitleContainingIgnoreCase(title, pageable)).thenReturn(bookPage);
        when(bookMapper.toResponseDto(book)).thenReturn(responseDto);

        Page<BookResponseDTO> result = bookService.searchByTitle(title, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("1984", result.getContent().get(0).getTitle());
        verify(bookRepository).findByTitleContainingIgnoreCase(title, pageable);
    }

    @Test
    void searchByAuthor_existingAuthor_returnsMatchingBooks() {
        String author = "Orwell";
        Pageable pageable = PageRequest.of(0, 10);

        Book book = new Book();
        book.setId(1L);
        book.setAuthor("George Orwell");

        Page<Book> bookPage = new PageImpl<>(List.of(book));

        BookResponseDTO responseDto = new BookResponseDTO();
        responseDto.setId(1L);
        responseDto.setAuthor("George Orwell");

        when(bookRepository.findByAuthorContainingIgnoreCase(author, pageable)).thenReturn(bookPage);
        when(bookMapper.toResponseDto(book)).thenReturn(responseDto);

        Page<BookResponseDTO> result = bookService.searchByAuthor(author, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("George Orwell", result.getContent().get(0).getAuthor());
        verify(bookRepository).findByAuthorContainingIgnoreCase(author, pageable);
    }

    @Test
    void searchByTitle_nonExistingTitle_returnsEmptyPage() {
        String title = "NonExisting";
        Pageable pageable = PageRequest.of(0, 10);

        Page<Book> emptyPage = Page.empty();

        when(bookRepository.findByTitleContainingIgnoreCase(title, pageable)).thenReturn(emptyPage);

        Page<BookResponseDTO> result = bookService.searchByTitle(title, pageable);

        assertEquals(0, result.getTotalElements());
        assertTrue(result.isEmpty());
        verify(bookRepository).findByTitleContainingIgnoreCase(title, pageable);
        verifyNoInteractions(bookMapper);
    }
}
