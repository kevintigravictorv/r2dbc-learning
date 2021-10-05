package com.example.r2dbclearning.database.entity.transaction.base;

import io.r2dbc.postgresql.codec.Json;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceBaseEntity {
  private String invoiceType;
  private String invoiceStatus;
  private String documentType;
  private String documentSource;
  private String documentSourceId;
  private Integer documentSourceVersion;
  private String businessLineId;
  private String invoicePartnerId;
  private String invoiceAgreementId;
  private String contractingEntity;
  private String referenceId;
  private BigDecimal totalAmount;
  private BigDecimal totalVatAmount;
  private BigDecimal totalWhtAmount;
  private BigDecimal totalAmountExcludeTax;
  private String transactionCurrency;
  private OffsetDateTime transactionTimestamp;
  private OffsetDateTime settlementInvoiceBaseDate;
  private OffsetDateTime commercialInvoiceBaseDate;
  private Json additionalData;
  private String paymentStatus;
}
