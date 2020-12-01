package io.eldarrin.seaops.pipelines.impl;

import io.eldarrin.seaops.pipelines.Pipelines;
import io.eldarrin.seaops.pipelines.PipelinesService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class MongoPipelinesServiceImpl implements PipelinesService {
    @Override
    public PipelinesService initialisePersistence(Handler<AsyncResult<Void>> resultHandler) {
        return null;
    }

    private static final Logger logger = LoggerFactory.getLogger(MongoPipelinesServiceImpl.class);

    @Override
    public PipelinesService addPipelines(Pipelines pipelines, Handler<AsyncResult<Pipelines>> resultHandler) {
        return null;
    }

    @Override
    public PipelinesService retrievePipelines(Handler<AsyncResult<List<Pipelines>>> resultHandler) {
        Promise<List<Pipelines>> promise = Promise.promise();
        promise.future().onComplete(resultHandler);

        List<Pipelines> pl = new ArrayList<>();
        Pipelines p = new Pipelines();
        p.setPipelinesId("id");
        p.setPipelinesName("name");
        pl.add(p);

        promise.complete(pl);
        return this;
    }
}
