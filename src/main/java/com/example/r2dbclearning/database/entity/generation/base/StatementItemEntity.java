package com.example.r2dbclearning.database.entity.generation.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatementItemEntity {
  private String invoiceType;
  private String transactionType;
}
