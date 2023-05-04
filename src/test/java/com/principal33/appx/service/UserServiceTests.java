package com.principal33.appx.service;

import com.principal33.appx.dto.UserDto;
import com.principal33.appx.external.EnergyService;
import com.principal33.appx.model.User;
import com.principal33.appx.repostitory.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {

  @Mock
  private UserRepository userRepository;

  @Mock
  private EnergyService energyService;

  @Mock
  private EmailService emailService;
  @InjectMocks
  private UserService userService;

  @Test
  void createUser_whenValidInput_repositoryCalled() {
    UserDto userDto = new UserDto("John", "Doe", "john.doe@mail.com");
    userService.createUser(userDto);
    verify(userRepository, times(1)).save(any(User.class));
    verify(energyService, times(1)).getKid("john.doe@mail.com");
    verify(emailService, times(1)).sendSimpleMessage("john.doe@mail.com", "Registration to appx", "You are in");
  }
}
