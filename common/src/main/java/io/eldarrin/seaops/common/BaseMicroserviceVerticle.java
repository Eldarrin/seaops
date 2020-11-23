package io.eldarrin.seaops.common;

import io.vertx.circuitbreaker.CircuitBreaker;
import io.vertx.circuitbreaker.CircuitBreakerOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.impl.ConcurrentHashSet;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import io.vertx.servicediscovery.types.EventBusService;
import io.vertx.servicediscovery.types.HttpEndpoint;
import io.vertx.servicediscovery.types.JDBCDataSource;
import io.vertx.servicediscovery.types.MessageSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class BaseMicroserviceVerticle extends AbstractVerticle {

    private static final String API_NAME = "api.name";

    private static final String CIRCUIT_BREAKER = "circuit-breaker";

    private static final String LOG_EVENT_ADDRESS = "events.log";

    private static final Logger logger = LoggerFactory.getLogger(BaseMicroserviceVerticle.class);

    protected ServiceDiscovery discovery;
    protected CircuitBreaker circuitBreaker;
    private final Set<Record> registeredRecords = new ConcurrentHashSet<>();

    @Override
    public void start() {
        // init service discovery instance
        discovery = ServiceDiscovery.create(vertx, new ServiceDiscoveryOptions().setBackendConfiguration(config()));

        // init circuit breaker instance
        JsonObject cbOptions = config().getJsonObject(CIRCUIT_BREAKER) != null ? config().getJsonObject(CIRCUIT_BREAKER)
                : new JsonObject();
        circuitBreaker = CircuitBreaker.create(cbOptions.getString("name", CIRCUIT_BREAKER), vertx,
                new CircuitBreakerOptions().setMaxFailures(cbOptions.getInteger("max-failures", 5))
                        .setTimeout(cbOptions.getLong("timeout", 10000L)).setFallbackOnFailure(true)
                        .setResetTimeout(cbOptions.getLong("reset-timeout", 30000L)));
    }

    protected Promise<Void> publishHttpEndpoint(String name, String host, int port) {
        Record record = HttpEndpoint.createRecord(name, host, port, "/",
                new JsonObject().put(API_NAME, config().getString(API_NAME, "")));
        return publish(record);
    }

    protected Promise<Void> publishHttpEndpoint(String name, String host, int port, String apiName) {
        Record record = HttpEndpoint.createRecord(name, host, port, "/",
                new JsonObject().put(API_NAME, apiName));
        return publish(record);
    }

    protected Promise<Void> publishApiGateway(String host, int port) {
        Record record = HttpEndpoint.createRecord("api-gateway", true, host, port, "/", null).setType("api-gateway");
        return publish(record);
    }

    protected Promise<Void> publishMessageSource(String name, String address) {
        Record record = MessageSource.createRecord(name, address);
        return publish(record);
    }

    protected Promise<Void> publishJDBCDataSource(String name, JsonObject location) {
        Record record = JDBCDataSource.createRecord(name, location, new JsonObject());
        return publish(record);
    }

    protected Promise<Void> publishEventBusService(String name, String address, Class<?> serviceClass) {
        Record record = EventBusService.createRecord(name, address, serviceClass);
        return publish(record);
    }

    /**
     * Publish a service with record.
     *
     * @param record service record
     * @return async result
     */
    private Promise<Void> publish(Record record) {
        if (discovery == null) {
            try {
                start();
            } catch (Exception e) {
                throw new IllegalStateException("Cannot create discovery service");
            }
        }

        Promise<Void> promise = Promise.promise();
        // publish the service
        discovery.publish(record, ar -> {
            if (ar.succeeded()) {
                registeredRecords.add(record);
                logger.info("Service <" + ar.result().getName() + "> published on ");
                promise.complete();
            } else {
                promise.fail(ar.cause());
            }
        });

        return promise;
    }

    /**
     * A helper method that simply publish logs on the event bus.
     *
     * @param type log type
     * @param data log message data
     */
    protected void publishLogEvent(String type, JsonObject data) {
        JsonObject msg = new JsonObject().put("type", type).put("message", data);
        vertx.eventBus().publish(LOG_EVENT_ADDRESS, msg);
    }

    protected void publishLogEvent(String type, JsonObject data, boolean succeeded) {
        JsonObject msg = new JsonObject().put("type", type).put("status", succeeded).put("message", data);
        vertx.eventBus().publish(LOG_EVENT_ADDRESS, msg);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void stop(Promise<Void> promise) {
        // In current design, the publisher is responsible for removing the service
        List<Promise> promises = new ArrayList<>();
        registeredRecords.forEach(record -> {
            Promise<Void> cleanupPromise = Promise.promise();
            promises.add(cleanupPromise);
            discovery.unpublish(record.getRegistration(), cleanupPromise);
        });

        if (promises.isEmpty()) {
            discovery.close();
            promise.complete();
        } else {

            List<Future> allFutures = new ArrayList<>();
            promises.forEach(promiseItem -> allFutures.add(promiseItem.future()));

            CompositeFuture.all(allFutures).onFailure(fail -> {
                promise.fail(fail.getCause());
            });

        }
    }



}

