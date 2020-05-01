package org.nikas.config.poc.entity.device;

import lombok.Getter;

@Getter
public enum FramingProtocol {
  CHUNK("##"), END_OF_MESSAGE("]]>]]>");

  @Getter
  private final String framer;

  FramingProtocol(String framer) {
    this.framer = framer;
  }
}
