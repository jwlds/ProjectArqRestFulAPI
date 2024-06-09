package com.puccampinas.projectarqrestfulapi.dtos.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpDTO {
    @NotBlank
    @Size(min = 3, max = 30)
    private String email;
    @NotBlank
    @Size(min = 6, max = 60)
    private String password;
    @NotBlank
    private String fullName;
}