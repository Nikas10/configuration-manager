package org.nikas.config.poc.repository;

import org.nikas.config.poc.entity.DeviceProfile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface OnboardingRepository extends MongoRepository<DeviceProfile, String> {

  Optional<DeviceProfile> findByProfileId(String id);

}
