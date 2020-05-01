package org.nikas.config.poc.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.nikas.config.poc.entity.device.Connection;
import org.nikas.config.poc.entity.device.Credential;
import org.nikas.config.poc.entity.device.Parameters;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceProfileDto {

  private Credential credentials;

  private Connection connection;

  private Parameters parameters;

}
