package org.nikas.config.poc.rest;

import org.nikas.config.poc.rest.dto.DeviceProfileDto;
import org.nikas.config.poc.service.OnboardingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class DeviceOnboardingController {

  @Autowired
  private OnboardingService onboardingService;

  @PostMapping(value = "/api/onboarding")
  public String createDeviceProfile(@RequestBody DeviceProfileDto deviceProfileDto) {
    return onboardingService.onboard(deviceProfileDto);
  }

  @GetMapping(value = "/api/onboarding/{deviceId}")
  public DeviceProfileDto get(@PathVariable("deviceId") String deviceId) {
    return onboardingService.get(deviceId).toDto();
  }

}
