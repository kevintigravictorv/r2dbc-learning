package com.example.r2dbclearning.database.entity.generation;

import com.example.r2dbclearning.database.entity.generation.base.StatementEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class ReceivableStatementEntity extends StatementEntity {
  private String receivableStatementId;
}
