package io.eldarrin.seaops.pipelines;

import io.eldarrin.seaops.common.BaseMicroserviceVerticle;
import io.eldarrin.seaops.pipelines.api.RestPipelinesAPIVerticle;
import io.eldarrin.seaops.pipelines.impl.MongoPipelinesServiceImpl;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class PipelinesVerticle extends BaseMicroserviceVerticle {

    private PipelinesService pipelinesService;

    private static final Logger logger = LoggerFactory.getLogger(PipelinesVerticle.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        logger.info("Starting PipelinesVerticle");
        pipelinesService = new MongoPipelinesServiceImpl();
        deployRestVerticle();
        logger.info("Deployed Rest verticle");
/*
        vertx.createHttpServer().requestHandler(req -> {
            req.response()
                    .putHeader("content-type", "text/plain")
                    .end("Hello from Vert.x!");
        }).listen(8080, http -> {
            if (http.succeeded()) {
                startPromise.complete();

                System.out.println("HTTP server started on port 8888");
            } else {
                startPromise.fail(http.cause());
            }
        });*/
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
