package io.eldarrin.seaops.pipelines.api;

import io.eldarrin.seaops.common.RestAPIVerticle;
import io.eldarrin.seaops.pipelines.Pipelines;
import io.eldarrin.seaops.pipelines.PipelinesService;
import io.vertx.core.Promise;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class RestPipelinesAPIVerticle extends RestAPIVerticle {

    public static final String SERVICE_NAME = "pipelines-rest-api";

    private static final String API_ADD = "/add";
    private static final String API_RETRIEVE = "/";

    private final PipelinesService pipelinesService;

    public RestPipelinesAPIVerticle(PipelinesService pipelinesService) {
        super();
        this.pipelinesService = pipelinesService;
    }

    @Override
    public void start(Promise<Void> promise) {
        super.start();
        final Router router = Router.router(vertx);
        // body handler
        router.route().handler(BodyHandler.create());
        // API route handler
        addHealthHandler(router, promise);
        router.post(API_ADD).handler(this::apiAdd);
        router.get(API_RETRIEVE).handler(this::apiRetrieve);

        startRestService(router, promise, SERVICE_NAME, "client", "seaops", "seaops-pipelines");
    }

    private void apiAdd(RoutingContext rc) {
        try {
            Pipelines pipelines = new Pipelines(new JsonObject(rc.getBodyAsString()));
            pipelinesService.addPipelines(pipelines, resultHandler(rc, r -> {
                String result = new JsonObject().put("message", "pipelines_added")
                        .put("pipelinesId", pipelines.getPipelinesId()).encodePrettily();
                rc.response().setStatusCode(201).putHeader("content-type", "application/json").end(result);
            }));
        } catch (DecodeException e) {
            badRequest(rc, e);
        }
    }

    private void apiRetrieve(RoutingContext rc) {
        pipelinesService.retrievePipelines(resultHandlerNonEmpty(rc));
    }


}
