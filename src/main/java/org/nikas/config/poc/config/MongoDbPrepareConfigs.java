package org.nikas.config.poc.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Map;

/**
 * Change letter in fields names of {@link com.fasterxml.jackson.databind.JsonNode} or {@link
 * com.fasterxml.jackson.databind.node.ArrayNode} according to input symbols
 */
@Service
public class MongoDbPrepareConfigs {

  private final ObjectMapper objectMapper;

  @Autowired
  public MongoDbPrepareConfigs(ObjectMapper mapper) {
    this.objectMapper = mapper;
  }

  public ArrayNode changeFieldSymbolsInArrayNode(ArrayNode beforeChanging, String oldSymbol,
                                                 String newSymbol) {
    ArrayNode afterChanging = objectMapper.createArrayNode();
    for (JsonNode node : beforeChanging) {
      if (node.isObject()) {
        JsonNode changedNode = changeFieldSymbolsInObjectNode((ObjectNode) node, oldSymbol,
                newSymbol);
        afterChanging.add(changedNode);
      } else if (node.isArray()) {
        afterChanging.add(changeFieldSymbolsInArrayNode((ArrayNode) node, oldSymbol, newSymbol));
      } else {
        afterChanging.add(node);
      }
    }
    return afterChanging;
  }

  public ObjectNode changeFieldSymbolsInObjectNode(ObjectNode node, String oldSymbol,
                                                   String newSymbol) {
    ObjectNode objectNode = objectMapper.createObjectNode();
    Iterator<Map.Entry<String, JsonNode>> iterator = node.fields();
    while (iterator.hasNext()) {
      Map.Entry<String, JsonNode> entry = iterator.next();
      String key = entry.getKey();
      JsonNode value = entry.getValue();
      if (value.isObject()) {
        objectNode.set(key.contains(oldSymbol) ? key.replace(oldSymbol, newSymbol) : key,
                changeFieldSymbolsInObjectNode((ObjectNode) value, oldSymbol, newSymbol));
      } else if (value.isArray()) {
        ArrayNode arrayNode = changeFieldSymbolsInArrayNode((ArrayNode) value, oldSymbol,
                newSymbol);
        objectNode
                .set(key.contains(oldSymbol) ? key.replace(oldSymbol, newSymbol) : key, arrayNode);
      } else {
        objectNode.set(key.contains(oldSymbol) ? key.replace(oldSymbol, newSymbol) : key, value);
      }
    }
    return objectNode;
  }
}
