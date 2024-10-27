package com.example.starter;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.semconv.resource.attributes.ResourceAttributes;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.tracing.opentelemetry.OpenTelemetryOptions;

import java.io.IOException;

public class BaseMain {


    public static void main(String[] args) {

        VertxOptions vertxOptions = new VertxOptions();

        SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder()
                .setResource(Resource.create(Attributes.of(ResourceAttributes.SERVICE_NAME, "testService")))
                .build();

        OpenTelemetry openTelemetry =  OpenTelemetrySdk.builder()
                .setTracerProvider(sdkTracerProvider)
                .setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
                .buildAndRegisterGlobal();

        vertxOptions.setTracingOptions(new OpenTelemetryOptions(openTelemetry));

        Vertx vertx = Vertx.vertx(vertxOptions);
        vertx.deployVerticle(new MainVerticle()).onComplete(handler -> {
                if (handler.succeeded()) {
                    System.out.println("started service verticle");
                } else {
                    System.out.println("Error while starting the verticle: ");
                }
            });
    }
}
