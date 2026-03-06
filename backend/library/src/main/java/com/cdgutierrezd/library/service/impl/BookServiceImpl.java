package com.cdgutierrezd.library.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cdgutierrezd.library.dto.request.BookRequestDTO;
import com.cdgutierrezd.library.dto.response.BookResponseDTO;
import com.cdgutierrezd.library.dto.response.LoanResponseDTO;
import com.cdgutierrezd.library.entity.Book;
import com.cdgutierrezd.library.exception.BusinessException;
import com.cdgutierrezd.library.exception.ErrorCode;
import com.cdgutierrezd.library.exception.ResourceNotFoundException;
import com.cdgutierrezd.library.mapper.BookMapper;
import com.cdgutierrezd.library.repository.BookRepository;
import com.cdgutierrezd.library.service.interfaces.BookServiceInterface;
import com.cdgutierrezd.library.service.interfaces.LoanServiceInterface;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookServiceInterface {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final LoanServiceInterface loanService;

    @Override
    public Page<BookResponseDTO> findAll(Pageable pageable) {
        Page<Book> books = bookRepository.findAll(pageable);
        return books.map(bookMapper::toResponseDto);
    }

    @Override
    public BookResponseDTO findById(Long id) {
        Book book = bookRepository.
                findById(id).
                orElseThrow(() -> new ResourceNotFoundException(ErrorCode.BOOK_NOT_FOUND, "Book not found with id: " + id));
        return bookMapper.toResponseDto(book);
    }

    @Override
    public Page<BookResponseDTO> searchByTitle(String title, Pageable pageable) {
        Page<Book> books = bookRepository.findByTitleContainingIgnoreCase(title, pageable);
        return books.map(bookMapper::toResponseDto);
    }

    @Override
    public Page<BookResponseDTO> searchByAuthor(String author, Pageable pageable) {
        Page<Book> books = bookRepository.findByAuthorContainingIgnoreCase(author, pageable);
        return books.map(bookMapper::toResponseDto);
    }

    @Override
    public BookResponseDTO save(BookRequestDTO dto) {
        Book book = bookMapper.toEntity(dto);
        Book savedBook = bookRepository.save(book);
        return bookMapper.toResponseDto(savedBook);
    }

    @Override
    public BookResponseDTO update(Long id, BookRequestDTO dto) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(()
                        -> new ResourceNotFoundException(ErrorCode.BOOK_NOT_FOUND, "Book not found with id: " + id));

        existingBook.setTitle(dto.getTitle());
        existingBook.setAuthor(dto.getAuthor());
        existingBook.setPublicationYear(dto.getPublicationYear());
        existingBook.setTotalStock(dto.getTotalStock());

        Book updatedBook = bookRepository.save(existingBook);
        return bookMapper.toResponseDto(updatedBook);
    }

    @Override
    public void deleteById(Long id) {
        List<LoanResponseDTO> loans = loanService.findByBookId(id);
        if (!loans.isEmpty()) {
            throw new BusinessException(ErrorCode.BOOK_HAS_ACTIVE_LOANS);
        }
        bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.BOOK_NOT_FOUND, "Book not found with id: " + id));
        bookRepository.deleteById(id);
    }
}
