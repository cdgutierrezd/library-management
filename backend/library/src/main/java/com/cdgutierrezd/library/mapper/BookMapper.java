package com.cdgutierrezd.library.mapper;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.cdgutierrezd.library.dto.request.BookRequestDTO;
import com.cdgutierrezd.library.dto.response.BookResponseDTO;
import com.cdgutierrezd.library.entity.Book;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class BookMapper {

    private final ModelMapper modelMapper;

    public BookResponseDTO toResponseDto(Book book) {
        if (book == null) return null;
        return modelMapper.map(book, BookResponseDTO.class);
    }

    public Book toEntity(BookRequestDTO dto) {
        if (dto == null) return null;
        return modelMapper.map(dto, Book.class);
    }

    public List<BookResponseDTO> toResponseDtoList(List<Book> books) {
        if (books == null) return List.of();
        return books
                .stream()
                .map(this::toResponseDto)
                .toList();
    }
}
