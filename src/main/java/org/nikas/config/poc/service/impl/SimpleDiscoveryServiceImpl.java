package org.nikas.config.poc.service.impl;

import com.jcraft.jsch.JSchException;
import org.nikas.config.poc.entity.DeviceProfile;
import org.nikas.config.poc.service.ConnectionService;
import org.nikas.config.poc.service.DiscoveryService;
import org.nikas.config.poc.service.OnboardingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("simpleDiscovery")
public class SimpleDiscoveryServiceImpl implements DiscoveryService {

  @Autowired
  private OnboardingService onboardingService;

  @Autowired
  @Qualifier("NETCONFService")
  private NETCONFConnectionService connectionService;

  @Override
  public String get(String profileId, String rpc) {
    DeviceProfile profile = onboardingService.get(profileId);
    connectionService.createConnection(profile);
    try {
      connectionService.connect();
    } catch (JSchException e) {
      throw new RuntimeException("Error during device connection!", e);
    }
    String response = connectionService.getConfig(rpc);
    connectionService.disconnect();
    return response;
  }
}
