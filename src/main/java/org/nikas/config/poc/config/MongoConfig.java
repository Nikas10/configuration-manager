package org.nikas.config.poc.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import com.mongodb.MongoClientOptions;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Configuration
@EnableMongoRepositories
@EnableMongoAuditing
@Slf4j
public class MongoConfig {

  private static final String DOT = ".";
  private static final String SYMBOL = "┼";

  private static MongoDbPrepareConfigs prepareConfigs;
  private static ObjectMapper objectMapper;

  @Autowired
  public MongoConfig(MongoDbPrepareConfigs mongoDbPrepareConfigs, ObjectMapper mapper) {
    prepareConfigs = mongoDbPrepareConfigs;
    objectMapper = mapper;
  }

  @Bean
  public MongoClientOptions mongoOptions() {
    return MongoClientOptions.builder()
            .serverSelectionTimeout(30000)
            .socketTimeout(30000)
            .connectTimeout(30000)
            .build();
  }

  @Bean
  public MongoCustomConversions customConversions() {
    final ArrayList<? extends Converter>
            converters = Lists.newArrayList(
            ArrayNodeToListConverter.INSTANCE,
            ObjectNodeToDocumentConverter.INSTANCE,
            ListDocumentToJsonNodeConverter.INSTANCE,
            DocumentToJsonNodeConverter.INSTANCE,
            ListToArrayNodeConverter.INSTANCE
    );

    log.debug("Register converters: {}", converters);
    return new MongoCustomConversions(converters);
  }


  @WritingConverter
  public enum ArrayNodeToListConverter implements Converter<ArrayNode, List> {
    INSTANCE;

    @Override
    public List convert(ArrayNode beforeChanging) {
      try {
        ArrayNode arrayNode = prepareConfigs
                .changeFieldSymbolsInArrayNode(beforeChanging, DOT, SYMBOL);
        return objectMapper.convertValue(arrayNode, List.class);
      } catch (Exception e) {
        log.error("Error while parsing arrayNode to mongo object", e);
        throw new RuntimeException("Error while parsing arrayNode to mongo object", e);
      }
    }
  }

  @ReadingConverter
  public enum ListToArrayNodeConverter implements Converter<List, ArrayNode> {
    INSTANCE;

    @Override
    public ArrayNode convert(List source) {
      try {
        ArrayNode beforeChanging = objectMapper.convertValue(source, ArrayNode.class);
        return prepareConfigs
                .changeFieldSymbolsInArrayNode(beforeChanging, SYMBOL, DOT);
      } catch (Exception e) {
        log.error("Error while parsing arrayNode to mongo object", e);
        throw new RuntimeException("Error while parsing arrayNode to mongo object", e);
      }
    }
  }

  @WritingConverter
  public enum ObjectNodeToDocumentConverter implements Converter<ObjectNode, Document> {
    INSTANCE;

    @Override
    public Document convert(ObjectNode source) {
      try {
        ObjectNode afterChanging = prepareConfigs
                .changeFieldSymbolsInObjectNode(source, DOT, SYMBOL);
        return new Document(
                objectMapper.convertValue(afterChanging, new TypeReference<Map<String, Object>>() {
                }));
      } catch (Exception e) {
        log.error("Error while parsing jsonNode to mongo object", e);
        throw new RuntimeException("Error while parsing jsonNode to mongo object", e);
      }
    }
  }

  @ReadingConverter
  public enum ListDocumentToJsonNodeConverter implements Converter<List<Document>, JsonNode> {
    INSTANCE;

    @Override
    public JsonNode convert(List<Document> configBody) {
      try {
        JsonNode beforeChanging = objectMapper.convertValue(configBody, JsonNode.class);
        return prepareConfigs
                .changeFieldSymbolsInArrayNode((ArrayNode) beforeChanging, SYMBOL, DOT);

      } catch (Exception e) {
        log.error("Error while parsing jsonNode from mongo object", e);
        throw new RuntimeException("Error while parsing jsonNode from mongo object", e);
      }
    }
  }

  @ReadingConverter
  public enum DocumentToJsonNodeConverter implements Converter<Document, JsonNode> {
    INSTANCE;

    @Override
    public JsonNode convert(Document configBody) {
      try {
        JsonNode beforeChanging = objectMapper.convertValue(configBody, JsonNode.class);
        return prepareConfigs
                .changeFieldSymbolsInObjectNode((ObjectNode) beforeChanging, SYMBOL, DOT);
      } catch (Exception e) {
        log.error("Error while parsing jsonNode from mongo object", e);
        throw new RuntimeException("Error while parsing jsonNode from mongo object", e);
      }
    }

  }
}
