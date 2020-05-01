package org.nikas.config.poc.entity.device;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Credential {

  private String login;

  private String password;

  private String publicKey;

  private String privateKey;

}
