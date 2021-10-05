package com.example.r2dbclearning.service;

import com.example.r2dbclearning.database.entity.transaction.AccountPayableEntity;
import com.example.r2dbclearning.database.entity.transaction.AccountReceivableEntity;
import com.example.r2dbclearning.database.entity.transaction.enumeration.InvoiceStatus;
import com.example.r2dbclearning.runner.payload.InvoiceGroupPayload;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static com.example.r2dbclearning.utility.database.ConnectionManagement.close;

@Service
public class QueryService {

  @Value(value = "${r2dbc.fetch-size}")
  private Integer fetchSize;

  @Autowired
  @Qualifier("query-connection")
  private ConnectionFactory queryConnection;

  public Flux<AccountPayableEntity> findAccountPayableByParametersAndStatus(InvoiceGroupPayload payload, InvoiceStatus invoiceStatus) {
    return Mono.from(queryConnection.create())
        .flatMapMany(connection ->
            Flux.from(connection.createStatement("SELECT account_payable_id FROM invoice_transaction.account_payable WHERE invoice_partner_id = $1 AND " +
                "invoice_agreement_id = $2 AND settlement_invoice_base_date <= $3 AND invoice_group_status = $4")
                .bind("$1", payload.getInvoicePartnerId())
                .bind("$2", payload.getInvoiceAgreementId())
                .bind("$3", payload.getBatchTime())
                .bind("$4", invoiceStatus.name())
                .execute())
                .flatMap(result -> result.map((row, rowData) -> row.get("account_payable_id", String.class)))
                .flatMap(this::findAccountPayableById)
                .concatWith(close(connection))
        );
  }

  private Flux<AccountPayableEntity> findAccountPayableById(String id) {
    return Mono.from(queryConnection.create())
        .flatMapMany(connection ->
            Flux.from(connection.createStatement("SELECT * FROM invoice_transaction.account_payable WHERE account_payable_id = $1")
                .bind("$1", id)
                //.fetchSize(fetchSize)
                .execute())
                .flatMap(result -> result.map((row, rowData) -> AccountPayableEntity.builder()
                    .accountPayableId(row.get("account_payable_id", String.class))
                    .invoiceType(row.get("invoice_type", String.class))
                    .totalAmount(row.get("total_amount", BigDecimal.class))
                    .build()))
                .concatWith(close(connection)));
  }

  public Flux<AccountReceivableEntity> findAccountReceivableByParametersAndStatus(InvoiceGroupPayload payload, InvoiceStatus invoiceStatus) {
    return Mono.from(queryConnection.create())
        .flatMapMany(connection ->
            Flux.from(connection.createStatement("SELECT account_receivable_id FROM invoice_transaction.account_receivable WHERE invoice_partner_id = $1 AND " +
                "invoice_agreement_id = $2 AND settlement_invoice_base_date <= $3 AND invoice_group_status = $4")
                .bind("$1", payload.getInvoicePartnerId())
                .bind("$2", payload.getInvoiceAgreementId())
                .bind("$3", payload.getBatchTime())
                .bind("$4", invoiceStatus.name())
                .execute())
                .flatMap(result -> result.map((row, rowData) -> row.get("account_receivable_id", String.class)))
                .flatMap(this::findAccountReceivableById)
                .concatWith(close(connection))
        );
  }

  private Flux<AccountReceivableEntity> findAccountReceivableById(String id) {
    return Mono.from(queryConnection.create())
        .flatMapMany(connection ->
            Flux.from(connection.createStatement("SELECT * FROM invoice_transaction.account_receivable WHERE account_receivable_id = $1")
                .bind("$1", id)
                //.fetchSize(fetchSize)
                .execute())
                .flatMap(result -> result.map((row, rowData) -> AccountReceivableEntity.builder()
                    .accountReceivableId(row.get("account_receivable_id", String.class))
                    .invoiceType(row.get("invoice_type", String.class))
                    .totalAmount(row.get("total_amount", BigDecimal.class))
                    .build()))
                .concatWith(close(connection)));
  }
}
