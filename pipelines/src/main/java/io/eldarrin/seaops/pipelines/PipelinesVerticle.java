package io.eldarrin.seaops.pipelines;

import io.eldarrin.seaops.common.BaseMicroserviceVerticle;
import io.eldarrin.seaops.pipelines.api.RestPipelinesAPIVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Promise;

public class PipelinesVerticle extends BaseMicroserviceVerticle {

    private PipelinesService pipelinesService;

    @Override
    public void start(Promise<Void> startPromise) throws Exception {


        vertx.createHttpServer().requestHandler(req -> {
            req.response()
                    .putHeader("content-type", "text/plain")
                    .end("Hello from Vert.x!");
        }).listen(8080, http -> {
            if (http.succeeded()) {
                startPromise.complete();
                deployRestVerticle();
                System.out.println("HTTP server started on port 8888");
            } else {
                startPromise.fail(http.cause());
            }
        });
    }

    private Future<Void> deployRestVerticle() {
        Promise<String> promise = Promise.promise();
        vertx.deployVerticle(new RestPipelinesAPIVerticle(pipelinesService),
                new DeploymentOptions().setConfig(config()), promise);
        return promise.future().map(r -> null);
    }

    @Override
    public void stop() throws Exception {
        super.stop();


    }
}
