package com.principal33.appx.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserDto(
    @NotBlank @Schema(example = "John") String firstName,
    @NotBlank @Schema(example = "Doe") String lastName,
    @NotBlank @Email @Schema(example = "john.doe@email.com") String email) {}
