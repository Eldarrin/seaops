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

    }


}
