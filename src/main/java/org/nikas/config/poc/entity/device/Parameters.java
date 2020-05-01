package org.nikas.config.poc.entity.device;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Parameters {

  private String model;

  private String vendor;

  private String name;

  @JsonProperty("firmware-version")
  private String firmwareVersion;

  private String site;

  private String domain;

  private String type;

  @JsonProperty("software-version")
  private String softwareVersion;

}
