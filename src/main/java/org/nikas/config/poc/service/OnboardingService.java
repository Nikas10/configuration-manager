package org.nikas.config.poc.service;

import org.nikas.config.poc.entity.DeviceProfile;
import org.nikas.config.poc.rest.dto.DeviceProfileDto;

public interface OnboardingService {

  String onboard(DeviceProfileDto deviceProfileDto);

  DeviceProfile get(String profileId);

}
