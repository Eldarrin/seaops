package io.eldarrin.seaops.pipelines;

import io.eldarrin.seaops.common.BaseMicroserviceVerticle;
import io.eldarrin.seaops.pipelines.api.RestPipelinesAPIVerticle;
import io.eldarrin.seaops.pipelines.impl.MongoPipelinesServiceImpl;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PipelinesVerticle extends BaseMicroserviceVerticle {

    private PipelinesService pipelinesService;

    private static final Logger logger = LoggerFactory.getLogger(PipelinesVerticle.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        pipelinesService = new MongoPipelinesServiceImpl();
        deployRestVerticle();

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
