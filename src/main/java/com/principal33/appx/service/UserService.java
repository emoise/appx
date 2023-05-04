package com.principal33.appx.service;

import com.principal33.appx.dto.ResponseUserDto;
import com.principal33.appx.dto.UserDto;
import com.principal33.appx.external.EnergyService;
import com.principal33.appx.model.User;
import com.principal33.appx.repostitory.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {

    public static final String REGISTRATION_MAIL_SUBJECT = "Registration to appx";
    public static final String REGISTRATION_MAIL_BODY = "You are in";
    private final UserRepository userRepository;

    private final EnergyService energyService;

    private final EmailService emailService;

    public ResponseUserDto createUser(UserDto userDto) {
        User user = getUser(userDto);
        userRepository.save(user);
        emailService.sendSimpleMessage(user.getEmail(), REGISTRATION_MAIL_SUBJECT, REGISTRATION_MAIL_BODY);
        log.info("user having id {} successfully created and email send at {}", user.getId(), user.getEmail());
        return getResponseUserDto(user);
    }

    private ResponseUserDto getResponseUserDto(User user) {
        return new ResponseUserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getKid());
    }

    private User getUser(UserDto userDto) {
        String kid = energyService.getKid(userDto.email());
        return User.builder().firstName(userDto.firstName()).lastName(userDto.lastName()).email(userDto.email()).kid(kid).build();
    }

}
