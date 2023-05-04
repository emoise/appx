package com.principal33.appx.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ResponseUserDto(
    @NotNull @Schema(example = "1") Integer id,
    @NotBlank @Schema(example = "John") String firstName,
    @NotBlank @Schema(example = "Doe") String lastName,
    @NotBlank @Email @Schema(example = "john.doe@email.com") String email,
    @Schema(example = "ab123")String kid) {}
