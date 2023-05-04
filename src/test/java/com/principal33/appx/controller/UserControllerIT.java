package com.principal33.appx.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.principal33.appx.config.WireMockConfig;
import com.principal33.appx.dto.UserDto;
import com.principal33.appx.model.User;
import com.principal33.appx.repostitory.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.principal33.appx.service.UserService.REGISTRATION_MAIL_BODY;
import static com.principal33.appx.service.UserService.REGISTRATION_MAIL_SUBJECT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {WireMockConfig.class})
class UserControllerIT {

  private static final String USER_FIRST_NAME = "John";
  private static final String USER_LAST_NAME = "Doe";
  static final String TEST_EMAIL_ADDRESS = "john.doe@mail.com";

  @RegisterExtension
  private static GreenMailExtension greenMail =
      new GreenMailExtension(ServerSetupTest.SMTP)
          .withConfiguration(GreenMailConfiguration.aConfig().withUser("user", "admin"))
          .withPerMethodLifecycle(false);

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Autowired private UserRepository userRepository;

  @Autowired private WireMockServer mockExternalService;

  private UserDto userDto;

  @BeforeEach
  void init() throws IOException {
    ExternalServiceMock.setupKidsResponse(mockExternalService);
  }

  @Test
  void createUser_withValidInput_storesUserInDatabaseAndSendsMail() throws Exception {
    userDto = new UserDto(USER_FIRST_NAME, USER_LAST_NAME, TEST_EMAIL_ADDRESS);
    postCreateUser().andExpect(status().isCreated());
    List<User> users = userRepository.findAll();
    assertThat(users.size(), equalTo(1));
    User actualUser = users.get(0);
    verifyActualUserMatchesUserDto(actualUser);
    verifyExternalServiceCalled();
    verifyMailReceived();
  }

  private void verifyActualUserMatchesUserDto(User actualUser) {
    assertThat(actualUser.getFirstName(), equalTo(USER_FIRST_NAME));
    assertThat(actualUser.getLastName(), equalTo(USER_LAST_NAME));
    assertThat(actualUser.getEmail(), equalTo(TEST_EMAIL_ADDRESS));
  }

  private void verifyMailReceived() throws MessagingException {
    MimeMessage receivedMessage = greenMail.getReceivedMessages()[0];
    assertThat(receivedMessage.getAllRecipients()[0].toString(), equalTo(TEST_EMAIL_ADDRESS));
    assertThat(receivedMessage.getFrom()[0].toString(), equalTo("appx@email.com"));
    assertThat(receivedMessage.getSubject(), equalTo(REGISTRATION_MAIL_SUBJECT));
    assertThat(GreenMailUtil.getBody(receivedMessage), equalTo(REGISTRATION_MAIL_BODY));
  }

  private void verifyExternalServiceCalled() {
    mockExternalService.verify(
        1,
        getRequestedFor(
            WireMock.urlEqualTo(
                "/kids?email=" + URLEncoder.encode(TEST_EMAIL_ADDRESS, Charset.defaultCharset()))));
  }

  private ResultActions postCreateUser() throws Exception {
    return mockMvc.perform(
        post("/v1/users")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(userDto)));
  }

  @Test
  void createUser_withMissingFirstName_throw400() throws Exception {
    userDto = new UserDto(null, USER_LAST_NAME, TEST_EMAIL_ADDRESS);
    postCreateUser().andExpect(status().is4xxClientError());
  }

  @Test
  void createUser_withMissingLastName_throw400() throws Exception {
    userDto = new UserDto(USER_FIRST_NAME, null, TEST_EMAIL_ADDRESS);
    postCreateUser().andExpect(status().is4xxClientError());
  }

  @Test
  void createUser_withMissingEmail_throw400() throws Exception {
    userDto = new UserDto(USER_FIRST_NAME, USER_LAST_NAME, null);
    postCreateUser().andExpect(status().is4xxClientError());
  }

  @Test
  void createUser_withInvalidEmail_throw400() throws Exception {
    userDto = new UserDto(USER_FIRST_NAME, USER_LAST_NAME, "invalid email address");
    postCreateUser().andExpect(status().is4xxClientError());
  }
}
