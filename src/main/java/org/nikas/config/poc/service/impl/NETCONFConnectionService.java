package org.nikas.config.poc.service.impl;

import com.jcraft.jsch.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.nikas.config.poc.entity.DeviceProfile;
import org.nikas.config.poc.entity.device.Connection;
import org.nikas.config.poc.entity.device.Credential;
import org.nikas.config.poc.entity.device.FramingProtocol;
import org.nikas.config.poc.service.ConnectionService;
import org.springframework.stereotype.Service;

import java.io.*;

@Slf4j
@Service("NETCONFService")
public class NETCONFConnectionService implements ConnectionService {

  private FramingProtocol framingProtocol;

  public static final String getConfig = "<rpc>\n" +
          "<get-config>\n" +
          "<source>\n" +
          "<running/>\n" +
          "</source>\n" +
          "</get-config>\n" +
          " \n" +
          "<get-config>\n" +
          "<source>\n" +
          "<running/>\n" +
          "</source>\n" +
          "<filter type=\"subtree\">\n" +
          "<configuration>\n" +
          "</configuration>\n" +
          "</filter>\n" +
          "</get-config>\n" +
          "</rpc>";

  @Getter
  private ChannelSubsystem subsystem;
  private Session session;

  public void createConnection(DeviceProfile deviceProfile) {
    Connection connection = deviceProfile.getConnection();
    Credential credential = deviceProfile.getCredentials();
    this.framingProtocol = connection.getFramingProtocol();
    log.info("Current framer: {}", framingProtocol.getFramer());
    try {
      JSch jsch = new JSch();
      Session session = jsch.getSession(credential.getLogin(),
              connection.getHost(), Integer.valueOf(connection.getPort()));
      session.setPassword(credential.getPassword());

      java.util.Properties config = new java.util.Properties();
      config.put("StrictHostKeyChecking", "no");
      session.setConfig(config);
      this.session = session;
      this.session.connect();

      this.subsystem = (ChannelSubsystem) this.session.openChannel("subsystem");
      this.subsystem.setSubsystem("netconf");
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void disconnect() {
    session.disconnect();
    subsystem.disconnect();
  }

  public void connect() throws JSchException {
    subsystem.connect();
  }

  public String getConfig(String rpc) {
    try {
      OutputStream os = subsystem.getOutputStream();
      String rpcMessage = rpc + framingProtocol.getFramer();
      os.write(rpcMessage.getBytes());
      os.flush();
      return formatRPCOutput(readChannelOutput());
    } catch (IOException e) {
      throw new RuntimeException("Error during RPC command execution", e);
    }
  }

  private String readChannelOutput() {

    byte[] buffer = new byte[1024];

    try{
      InputStream in = subsystem.getInputStream();
      String line = "";
      StringBuilder outputBuilder = new StringBuilder();
      while (true){
        while (in.available() > 0) {
          int i = in.read(buffer, 0, 1024);
          if (i < 0) {
            break;
          }
          line = new String(buffer, 0, i);
          log.info(line);
          outputBuilder.append(line);
        }
        if (line.contains(framingProtocol.getFramer()) || line.contains("</rpc-reply>")) {
          log.info("Found end of response");
          break;
        }

        if (subsystem.isClosed()){
          break;
        }
        try {
          Thread.sleep(1000);
        } catch (Exception ee){}
      }
      return outputBuilder.toString();
    }catch(Exception e){
      System.out.println("Error while reading channel output: "+ e);
    }
    return "";
  }

  private String formatRPCOutput(String output) {
    return output.replace(framingProtocol.getFramer(), "")
            .replace(output.substring(output.indexOf("<hello"),
                    output.lastIndexOf("</hello>")), "")
            .replace(output.substring(output.indexOf("<rpc-error"),
                    output.lastIndexOf("</rpc-error>")), "")
            .replace("</hello>", "")
            .replace("</rpc-error>", "");
  }
}
