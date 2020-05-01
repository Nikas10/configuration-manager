package org.nikas.config.poc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "device")
@Data
@AllArgsConstructor
public class DeviceProfile {
  


}
