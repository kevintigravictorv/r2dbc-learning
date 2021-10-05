package com.example.r2dbclearning.runner.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountBase {
  private String invoiceBaseId;
  private String invoiceType;
  private String transactionType;
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
  private String additionalData;
  private String paymentStatus;
}
