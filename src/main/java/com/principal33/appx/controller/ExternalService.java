package com.principal33.appx.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ExternalService {
  @GetMapping(value = "/kids")
  public ResponseEntity<String> getKid(@RequestParam String email) {
    return new ResponseEntity<>("abc", HttpStatus.OK);
  }
}
