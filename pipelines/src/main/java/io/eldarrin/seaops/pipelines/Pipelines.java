package io.eldarrin.seaops.pipelines;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

@DataObject(generateConverter = true)
public class Pipelines {

    private String pipelinesId;
    private String pipelinesName;

    public Pipelines(Pipelines pipelines) {
        super();
        this.pipelinesId = pipelines.pipelinesId;
        this.pipelinesName = pipelines.pipelinesName;
    }

    public String getPipelinesId() {
        return pipelinesId;
    }

    public void setPipelinesId(String pipelinesId) {
        this.pipelinesId = pipelinesId;
    }

    public String getPipelinesName() {
        return pipelinesName;
    }

    public void setPipelinesName(String pipelinesName) {
        this.pipelinesName = pipelinesName;
    }

    public Pipelines(JsonObject json) {
        PipelinesConverter.fromJson(json, this);
    }

    public Pipelines() {

    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        PipelinesConverter.toJson(this, json);
        return json;
    }

    public String toString() {
        return this.toJson().encodePrettily();
    }

}
