package com.cdgutierrezd.library.mapper;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.cdgutierrezd.library.dto.response.LoanResponseDTO;
import com.cdgutierrezd.library.entity.Loan;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class LoanMapper {

    private final ModelMapper modelMapper;

    public LoanResponseDTO toResponseDto(Loan loan) {
        if (loan == null) return null;
        return modelMapper.map(loan, LoanResponseDTO.class);
    }

    public List<LoanResponseDTO> toResponseDtoList(List<Loan> loans) {
        if (loans == null) return List.of();
        return loans
                .stream()
                .map(this::toResponseDto)
                .toList();
    }
}
