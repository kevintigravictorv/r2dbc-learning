package com.example.r2dbclearning.runner.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class InvoiceGroupPayload {
  private String invoicePartnerId;
  private String invoiceAgreementId;
  @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ssX")
  private Date batchTime;
}
