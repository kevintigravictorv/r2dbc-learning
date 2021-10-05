package com.example.r2dbclearning.runner.transaction;

import com.example.r2dbclearning.database.entity.generation.PayableStatementItemEntity;
import com.example.r2dbclearning.database.entity.transaction.base.InvoiceBaseEntity;
import com.example.r2dbclearning.runner.model.AccountBase;

public class AccountBaseTransformer {
  private AccountBaseTransformer() {
  }

  public static AccountBase transformInvoiceBaseEntityToModel(InvoiceBaseEntity entity, String invoiceBaseId, String transactionType) {
    return AccountBase.builder()
        .invoiceBaseId(invoiceBaseId)
        .invoiceType(entity.getInvoiceType())
        .transactionType(transactionType)
        .invoiceStatus(entity.getInvoiceStatus())
        .documentType(entity.getDocumentType())
        .documentSource(entity.getDocumentSource())
        .documentSourceId(entity.getDocumentSourceId())
        .documentSourceVersion(entity.getDocumentSourceVersion())
        .businessLineId(entity.getBusinessLineId())
        .invoicePartnerId(entity.getInvoicePartnerId())
        .invoiceAgreementId(entity.getInvoiceAgreementId())
        .contractingEntity(entity.getContractingEntity())
        .referenceId(entity.getReferenceId())
        .totalAmount(entity.getTotalAmount())
        .totalVatAmount(entity.getTotalVatAmount())
        .totalWhtAmount(entity.getTotalWhtAmount())
        .totalAmountExcludeTax(entity.getTotalAmountExcludeTax())
        .transactionCurrency(entity.getTransactionCurrency())
        .transactionTimestamp(entity.getTransactionTimestamp())
        .settlementInvoiceBaseDate(entity.getSettlementInvoiceBaseDate())
        .commercialInvoiceBaseDate(entity.getCommercialInvoiceBaseDate())
        .additionalData((entity.getAdditionalData() == null) ? "" : entity.getAdditionalData().asString())
        .paymentStatus(entity.getPaymentStatus())
        .build();
  }

  public static PayableStatementItemEntity transformModelToPayableStatementItemEntity(AccountBase entity,
                                                                                      String payableStatementId,
                                                                                      boolean isNew) {
    return PayableStatementItemEntity.builder()
        .payableStatementId(payableStatementId)
        .payableStatementItemId("SIP-Item-" + entity.getInvoiceBaseId())
        .invoiceType(entity.getInvoiceType())
        .transactionType(entity.getTransactionType())
        //.isNew(isNew)
        .build();
  }
}
