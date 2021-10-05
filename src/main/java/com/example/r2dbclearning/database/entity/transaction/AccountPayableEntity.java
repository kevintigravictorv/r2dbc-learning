package com.example.r2dbclearning.database.entity.transaction;

import com.example.r2dbclearning.database.entity.transaction.base.InvoiceBaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class AccountPayableEntity extends InvoiceBaseEntity {
  private String accountPayableId;
}
