package com.example.r2dbclearning.database.entity.generation;

import com.example.r2dbclearning.database.entity.generation.base.StatementEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class PayableStatementEntity extends StatementEntity {
  private String payableStatementId;
}
