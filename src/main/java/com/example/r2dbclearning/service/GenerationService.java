package com.example.r2dbclearning.service;

import com.example.r2dbclearning.database.entity.generation.PayableStatementEntity;
import com.example.r2dbclearning.database.entity.generation.PayableStatementItemEntity;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static com.example.r2dbclearning.utility.database.ConnectionManagement.close;

@Service
public class GenerationService {

  @Autowired
  @Qualifier("genset-connection")
  private ConnectionFactory gensetConnection;

  public Flux<PayableStatementEntity> savePayableStatement(PayableStatementEntity entity) {
    return Mono.from(gensetConnection.create())
        .flatMapMany(connection ->
            Flux.from(connection.createStatement("INSERT INTO invoice_generation.payable_statement VALUES ($1, $2)")
                .bind("$1", entity.getPayableStatementId())
                .bind("$2", entity.getTotalAmount())
                .execute())
                .flatMap(result -> result.map((row, rowData) -> PayableStatementEntity.builder()
                    .payableStatementId(row.get("payable_statement_id", String.class))
                    .totalAmount(row.get("total_amount", BigDecimal.class))
                    .build()))
                .map(payableStatement -> (PayableStatementEntity) payableStatement)
                .concatWith(close(connection))
        );
  }

  public Flux<PayableStatementItemEntity> savePayableStatementItem(PayableStatementItemEntity itemEntity) {
    return Mono.from(gensetConnection.create())
        .flatMapMany(connection ->
            Flux.from(connection.createStatement("INSERT INTO invoice_generation.payable_statement_item (payable_statement_id, payable_statement_item_id" +
                ", invoice_type, transaction_type) VALUES ($1, $2, $3, $4)")
                .bind("$1", itemEntity.getPayableStatementId())
                .bind("$2", itemEntity.getPayableStatementItemId())
                .bind("$3", itemEntity.getInvoiceType())
                .bind("$4", itemEntity.getTransactionType())
                .execute())
                .flatMap(result -> result.map((row, rowData) -> PayableStatementItemEntity.builder()
                    .payableStatementId(row.get("payable_statement_id", String.class))
                    .payableStatementItemId(row.get("payable_statement_item_id", String.class))
                    .invoiceType(row.get("invoice_type", String.class))
                    .transactionType(row.get("transaction_type", String.class))
                    .build()))
                .map(payableStatementItem -> (PayableStatementItemEntity) payableStatementItem)
                .concatWith(close(connection))
        );
  }

  public Flux<PayableStatementEntity> updatePayableStatement(PayableStatementEntity entity) {
    return Mono.from(gensetConnection.create())
        .flatMapMany(connection ->
            Flux.from(connection.createStatement("UPDATE invoice_generation.payable_statement SET total_amount = $2 WHERE payable_statement_id = $1")
                .bind("$1", entity.getPayableStatementId())
                .bind("$2", entity.getTotalAmount())
                .execute())
                .flatMap(result -> result.map((row, rowData) -> PayableStatementEntity.builder()
                    .payableStatementId(row.get("payable_statement_id", String.class))
                    .totalAmount(row.get("total_amount", BigDecimal.class))
                    .build()))
                .map(payableStatement -> (PayableStatementEntity) payableStatement)
                .concatWith(close(connection))
        );
  }
}
