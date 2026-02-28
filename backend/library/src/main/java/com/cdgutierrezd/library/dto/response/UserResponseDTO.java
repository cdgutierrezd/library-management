package com.cdgutierrezd.library.dto.response;

import lombok.Data;

@Data
public class UserResponseDTO {

    private Long id;
    private String name;
    private String email;
    private String phone;
}
