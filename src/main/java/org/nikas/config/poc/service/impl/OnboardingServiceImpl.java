package org.nikas.config.poc.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.nikas.config.poc.entity.DeviceProfile;
import org.nikas.config.poc.exception.DeviceNotFoundException;
import org.nikas.config.poc.repository.OnboardingRepository;
import org.nikas.config.poc.rest.dto.DeviceProfileDto;
import org.nikas.config.poc.service.OnboardingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OnboardingServiceImpl implements OnboardingService {

  @Autowired
  private OnboardingRepository onboardingRepository;

  @Override
  public String onboard(DeviceProfileDto deviceProfileDto) {
    DeviceProfile deviceProfile = new DeviceProfile();
    deviceProfile.setConnection(deviceProfileDto.getConnection());
    deviceProfile.setCredentials(deviceProfileDto.getCredentials());
    deviceProfile.setParameters(deviceProfileDto.getParameters());
    return onboardingRepository.save(deviceProfile).getProfileId();
  }

  @Override
  public DeviceProfile get(String profileId) {
    return onboardingRepository.findByProfileId(profileId).orElseThrow( () ->
            new DeviceNotFoundException("Device not found!")
    );
  }


}
