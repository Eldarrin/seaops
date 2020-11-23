package io.eldarrin.seaops.pipelines;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

import java.util.List;

@VertxGen
@ProxyGen
public interface PipelinesService {

    @Fluent
    PipelinesService initialisePersistence(Handler<AsyncResult<Void>> resultHandler);

    @Fluent
    PipelinesService addPipelines(Pipelines pipelines, Handler<AsyncResult<Pipelines>> resultHandler);

    @Fluent
    PipelinesService retrievePipelines(Handler<AsyncResult<List<Pipelines>>> resultHandler);
}
