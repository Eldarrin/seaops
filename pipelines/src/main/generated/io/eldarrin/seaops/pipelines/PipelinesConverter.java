package io.eldarrin.seaops.pipelines;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.impl.JsonUtil;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * Converter and mapper for {@link io.eldarrin.seaops.pipelines.Pipelines}.
 * NOTE: This class has been automatically generated from the {@link io.eldarrin.seaops.pipelines.Pipelines} original class using Vert.x codegen.
 */
public class PipelinesConverter {


  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, Pipelines obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "pipelinesId":
          if (member.getValue() instanceof String) {
            obj.setPipelinesId((String)member.getValue());
          }
          break;
        case "pipelinesName":
          if (member.getValue() instanceof String) {
            obj.setPipelinesName((String)member.getValue());
          }
          break;
      }
    }
  }

  public static void toJson(Pipelines obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(Pipelines obj, java.util.Map<String, Object> json) {
    if (obj.getPipelinesId() != null) {
      json.put("pipelinesId", obj.getPipelinesId());
    }
    if (obj.getPipelinesName() != null) {
      json.put("pipelinesName", obj.getPipelinesName());
    }
  }
}
