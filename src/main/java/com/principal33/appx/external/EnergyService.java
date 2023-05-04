package com.principal33.appx.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "getOrganizationKid")
public interface EnergyService {

  @GetMapping
  String getKid(@RequestParam("email") String email);
}
