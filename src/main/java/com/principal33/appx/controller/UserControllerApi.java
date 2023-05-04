package com.principal33.appx.controller;

import com.principal33.appx.dto.ResponseUserDto;
import com.principal33.appx.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@Tag(name = "user")
public interface UserControllerApi {
  @Operation(operationId = "create-user", summary = "Create a new user")
  @ApiResponse(
      responseCode = "200",
      description = "Calculation finished successfully. Total cost is returned")
  @ApiResponse(
      responseCode = "400",
      description = "Validation exception",
      content =
          @Content(
              schema = @Schema(implementation = Map.class),
              mediaType = "application/problem+json"))
  ResponseEntity<ResponseUserDto> create(UserDto userDto);
}
