package com.cdgutierrezd.library.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cdgutierrezd.library.dto.request.UserRequestDTO;
import com.cdgutierrezd.library.dto.response.LoanResponseDTO;
import com.cdgutierrezd.library.dto.response.UserResponseDTO;
import com.cdgutierrezd.library.entity.User;
import com.cdgutierrezd.library.exception.BusinessException;
import com.cdgutierrezd.library.exception.DuplicateResourceException;
import com.cdgutierrezd.library.exception.ErrorCode;
import com.cdgutierrezd.library.exception.ResourceNotFoundException;
import com.cdgutierrezd.library.mapper.UserMapper;
import com.cdgutierrezd.library.repository.UserRepository;
import com.cdgutierrezd.library.service.interfaces.LoanServiceInterface;
import com.cdgutierrezd.library.service.interfaces.UserServiceInterface;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserServiceInterface {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final LoanServiceInterface loanService;

    @Override
    public Page<UserResponseDTO> findAll(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(userMapper::toResponseDto);
    }

    @Override
    public UserResponseDTO findById(Long id) {
        User user = userRepository.
                findById(id).
                orElseThrow(()
                        -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND, "User not found with id: " + id));
        return userMapper.toResponseDto(user);
    }

    @Override
    public UserResponseDTO findByEmail(String email) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(()
                        -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND, "User not found with email: " + email));
        return userMapper.toResponseDto(user);
    }

    @Override
    public UserResponseDTO save(UserRequestDTO dto) {
        if (existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException(ErrorCode.USER_EMAIL_DUPLICATE, "Email already exists: " + dto.getEmail());
        }
        User user = userMapper.toEntity(dto);
        User savedUser = userRepository.save(user);
        return userMapper.toResponseDto(savedUser);
    }

    @Override
    public UserResponseDTO update(Long id, UserRequestDTO dto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND, "User not found with id: " + id));
        if (existsByEmailAndIdNot(dto.getEmail(), id)) {
            throw new DuplicateResourceException(ErrorCode.USER_EMAIL_DUPLICATE, "Email already exists: " + dto.getEmail());
        }
        existingUser.setName(dto.getName());
        existingUser.setEmail(dto.getEmail());
        existingUser.setPhone(dto.getPhone());
        User updatedUser = userRepository.save(existingUser);
        return userMapper.toResponseDto(updatedUser);
    }

    @Override
    public void deleteById(Long id) {
        List<LoanResponseDTO> loans = loanService.findByUserId(id);
        if (!loans.isEmpty()) {
            throw new BusinessException(ErrorCode.USER_HAS_ACTIVE_LOANS);
        }
        userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND, "User not found with id: " + id));
        userRepository.deleteById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    @Override
    public boolean existsByEmailAndIdNot(String email, Long id) {
        return userRepository.existsByEmailAndIdNot(email, id);
    }
}
