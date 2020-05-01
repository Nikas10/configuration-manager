package org.nikas.config.poc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.nikas.config.poc.entity.device.Connection;
import org.nikas.config.poc.entity.device.Credential;
import org.nikas.config.poc.entity.device.Parameters;
import org.nikas.config.poc.rest.dto.DeviceProfileDto;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "device")
@AllArgsConstructor
@NoArgsConstructor
public class DeviceProfile {

  @Id
  private String profileId;

  private Credential credentials;

  private Connection connection;

  private Parameters parameters;

  public DeviceProfileDto toDto() {
    return new DeviceProfileDto(credentials, connection, parameters);
  }

}
