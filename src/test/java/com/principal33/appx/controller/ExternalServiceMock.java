package com.principal33.appx.controller;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.net.URLEncoder;
import java.nio.charset.Charset;

import static com.principal33.appx.controller.UserControllerIT.TEST_EMAIL_ADDRESS;

public class ExternalServiceMock {

  public static void setupKidsResponse(WireMockServer mockService) {
    mockService.stubFor(
        WireMock.get(WireMock.urlEqualTo("/kids?email=" + URLEncoder.encode(TEST_EMAIL_ADDRESS, Charset.defaultCharset())))
            .willReturn(
                WireMock.aResponse()
                    .withStatus(HttpStatus.OK.value())
                    .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                    .withBody("abc")));
  }
}
