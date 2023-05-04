package com.principal33.appx.controller;

import com.principal33.appx.dto.ResponseUserDto;
import com.principal33.appx.dto.UserDto;
import com.principal33.appx.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class UserController implements UserControllerApi{

  private final UserService userService;

  @Override
  @PostMapping(value = "/users")
  public ResponseEntity<ResponseUserDto> create(@Valid @RequestBody UserDto userDto) {
    ResponseUserDto responseUserDto = userService.createUser(userDto);
    return new ResponseEntity<>(responseUserDto, HttpStatus.CREATED);
  }
}
