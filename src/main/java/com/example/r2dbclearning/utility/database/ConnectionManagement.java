package com.example.r2dbclearning.utility.database;

import io.r2dbc.spi.Connection;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

public class ConnectionManagement {
  public static <T> Mono<T> close(Connection connection) {
    return Mono.from(connection.close())
        .then(Mono.empty());
  }
}
