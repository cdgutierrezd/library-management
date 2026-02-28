package com.cdgutierrezd.library.mapper;

import java.util.List;

import com.cdgutierrezd.library.dto.request.UserRequestDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.cdgutierrezd.library.dto.response.UserResponseDTO;
import com.cdgutierrezd.library.entity.User;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class UserMapper {

    private final ModelMapper modelMapper;

    public UserResponseDTO toResponseDto(User user) {
        if (user == null) return null;
        return modelMapper.map(user, UserResponseDTO.class);
    }

    public User toEntity(UserRequestDTO dto) {
        if (dto == null) return null;
        return modelMapper.map(dto, User.class);
    }

    public List<UserResponseDTO> toResponseDtoList(List<User> users) {
        if (users == null) return List.of();
        return users
                .stream()
                .map(this::toResponseDto)
                .toList();
    }
}
