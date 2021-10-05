package com.example.r2dbclearning.database.entity.generation;

import com.example.r2dbclearning.database.entity.generation.base.StatementItemEntity;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@ToString(callSuper = true)
@Getter
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class PayableStatementItemEntity extends StatementItemEntity {

  /*
   *  TODO: Currently not supported from spring data R2DBC, feature requested relate to this pull request
   *  [DATAJDBC-352] https://github.com/spring-projects/spring-data-jdbc/issues/574
   */
  private String payableStatementId;
  private String payableStatementItemId;
}
