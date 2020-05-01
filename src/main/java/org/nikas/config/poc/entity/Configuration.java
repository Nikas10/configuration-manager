package org.nikas.config.poc.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "configuration")
@Data
@AllArgsConstructor
public class Configuration {

  @Id
  private String id;

  @Indexed
  private String deviceProfileId;


  private JsonNode configBody;


  @Indexed
  @JsonSerialize(using = ToStringSerializer.class)
  private Long version;


  @CreatedDate
  private Date createdWhen;


}
