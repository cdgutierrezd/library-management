package com.cdgutierrezd.library.service.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cdgutierrezd.library.dto.request.UserRequestDTO;
import com.cdgutierrezd.library.dto.response.UserResponseDTO;

public interface UserServiceInterface {

    Page<UserResponseDTO> findAll(Pageable pageable);

    UserResponseDTO findById(Long id);

    UserResponseDTO findByEmail(String email);

    UserResponseDTO save(UserRequestDTO dto);

    UserResponseDTO update(Long id, UserRequestDTO dto);

    void deleteById(Long id);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);
}
