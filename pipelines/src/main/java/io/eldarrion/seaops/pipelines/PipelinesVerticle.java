package io.eldarrion.seaops.pipelines;

import io.vertx.circuitbreaker.CircuitBreaker;
import io.vertx.circuitbreaker.CircuitBreakerOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import io.vertx.servicediscovery.types.HttpEndpoint;

public class PipelinesVerticle extends AbstractVerticle {

    ServiceDiscovery discovery;
    Record record;

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        discovery = ServiceDiscovery.create(vertx);

        // Customize the configuration
        discovery = ServiceDiscovery.create(vertx,
                new ServiceDiscoveryOptions()
                        .setAnnounceAddress("service-announce")
                        .setName("seaops-pipelines"));

        record = new Record()
                .setType("eventbus-service-proxy")
                .setLocation(new JsonObject().put("endpoint", "the-service-address"))
                .setName("my-service")
                .setMetadata(new JsonObject().put("some-label", "some-value"));

        discovery.publish(record, ar -> {
            if (ar.succeeded()) {
                // publication succeeded
                Record publishedRecord = ar.result();
            } else {
                // publication failed
            }
        });

        // Record creation from a type
        record = HttpEndpoint.createRecord("some-rest-api", "localhost", 8080, "/api");
        discovery.publish(record, ar -> {
            if (ar.succeeded()) {
                // publication succeeded
                Record publishedRecord = ar.result();
            } else {
                // publication failed
            }
        });


        CircuitBreaker breaker = CircuitBreaker.create("my-circuit-breaker", vertx,
                new CircuitBreakerOptions().setMaxFailures(5).setTimeout(2000)
        );

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
        });
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        discovery.unpublish(record.getRegistration(), ar -> {
            if (ar.succeeded()) {
                // Ok
            } else {
                // cannot un-publish the service, may have already been removed, or the record is not published
            }
        });
        discovery.close();
    }
}
