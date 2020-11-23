package io.eldarrion.seaops.pipelines;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

@VertxGen
@ProxyGen
public interface PipelineService {

    @Fluent
    PipelineService initialisePersistence(Handler<AsyncResult<Void>> resultHandler);
}
