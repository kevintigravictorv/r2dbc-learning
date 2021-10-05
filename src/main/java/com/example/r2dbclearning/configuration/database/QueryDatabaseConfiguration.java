package com.example.r2dbclearning.configuration.database;

import com.example.r2dbclearning.configuration.DatabasePoolProperties;
import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Configuration
public class QueryDatabaseConfiguration {

  @Autowired
  private DatabasePoolProperties databasePoolProperties;

  @Value(value = "${query.connection.url}")
  private String queryConnectionUrl;

  @Bean("query-connection")
  public ConnectionFactory queryConnection() {
    return new ConnectionPool(
        ConnectionPoolConfiguration.builder(ConnectionFactories.get(queryConnectionUrl))
            .maxIdleTime(Duration.ofMinutes(databasePoolProperties.getMaxIdleTime()))
            .initialSize(databasePoolProperties.getInitialSize())
            .maxSize(databasePoolProperties.getMaxSize())
            .maxCreateConnectionTime(Duration.ofSeconds(databasePoolProperties.getMaxCreateConnectionTime()))
            .build());
  }
}
