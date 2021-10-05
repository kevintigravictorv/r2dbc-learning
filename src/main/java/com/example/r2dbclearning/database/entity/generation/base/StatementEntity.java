package com.example.r2dbclearning.database.entity.generation.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatementEntity {
  private BigDecimal totalAmount;
}
