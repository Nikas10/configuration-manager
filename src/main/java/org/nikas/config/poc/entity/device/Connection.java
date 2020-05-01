package org.nikas.config.poc.entity.device;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Connection {

  private String host;

  private String port;

}
