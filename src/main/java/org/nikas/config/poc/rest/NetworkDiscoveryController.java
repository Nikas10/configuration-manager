package org.nikas.config.poc.rest;

import org.nikas.config.poc.service.DiscoveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static org.nikas.config.poc.service.impl.NETCONFConnectionService.getConfig;

@RestController
public class NetworkDiscoveryController {

  @Autowired
  @Qualifier("simpleDiscovery")
  private DiscoveryService discoveryService;

  @GetMapping("/api/discovery/{deviceId}")
  public String getRunningConfiguration(@PathVariable("deviceId") String deviceId) {
    return discoveryService.get(deviceId, getConfig);
  }
}
