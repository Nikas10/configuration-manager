package org.nikas.config.poc.exception;

public class DeviceNotFoundException extends RuntimeException {
  public DeviceNotFoundException(String cause) {
    super(cause);
  }
}
