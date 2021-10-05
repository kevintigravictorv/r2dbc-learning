package com.example.r2dbclearning.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "database.pool")
public class DatabasePoolProperties {
  private Integer maxIdleTime;
  private Integer initialSize;
  private Integer maxSize;
  private Integer maxCreateConnectionTime;
}
