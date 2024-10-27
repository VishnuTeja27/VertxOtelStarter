package com.example.starter;

import io.opentelemetry.api.trace.Span;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    vertx.createHttpServer().requestHandler(req -> {
      System.out.println(Span.current().getSpanContext().getTraceId());

      vertx.setTimer(1000, ar ->{
        System.out.println(Span.current().getSpanContext().getTraceId());
      });

      req.response()
        .putHeader("content-type", "text/plain")
        .end("Hello from Vert.x! ");
    }).listen(8888).onComplete(http -> {
      if (http.succeeded()) {
        startPromise.complete();
        System.out.println("HTTP server started on port 8888");
      } else {
        startPromise.fail(http.cause());
      }
    });
  }
}
