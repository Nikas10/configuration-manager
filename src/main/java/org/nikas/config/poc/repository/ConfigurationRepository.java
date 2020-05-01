package org.nikas.config.poc.repository;

import org.nikas.config.poc.entity.Configuration;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConfigurationRepository extends MongoRepository<Configuration, String> {
}
