package com.example.r2dbclearning.runner;

import com.example.r2dbclearning.database.entity.generation.PayableStatementEntity;
import com.example.r2dbclearning.database.entity.generation.PayableStatementItemEntity;
import com.example.r2dbclearning.database.entity.generation.enumeration.StatementType;
import com.example.r2dbclearning.database.entity.generation.enumeration.TransactionType;
import com.example.r2dbclearning.database.entity.transaction.enumeration.InvoiceStatus;
import com.example.r2dbclearning.runner.model.AccountBase;
import com.example.r2dbclearning.runner.payload.InvoiceGroupPayload;
import com.example.r2dbclearning.runner.transaction.AccountBaseTransformer;
import com.example.r2dbclearning.service.GenerationService;
import com.example.r2dbclearning.service.QueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Component
public class R2dbcRunner implements CommandLineRunner {

  @Value("${invoicePartnerId}")
  private String invoicePartnerId;

  @Value("${invoiceAgreementId}")
  private String invoiceAgreementId;

  @Value("${batchTime}")
  private String batchTime;

  @Value("${isReactive:false}")
  private String isReactive;

  @Value(value = "${r2dbc.fetch-size}")
  private Integer fetchSize;

  @Autowired
  private QueryService queryService;

  @Autowired
  private GenerationService generationService;

  public void groupInvoiceReactive(InvoiceGroupPayload payload, InvoiceStatus invoiceStatus) {
    log.info("Start grouping the invoice using reactive approach...");

    Flux<AccountBase> payableFlux = queryService.findAccountPayableByParametersAndStatus(payload, invoiceStatus)
        .publishOn(Schedulers.boundedElastic())
        .map(accountPayableEntity -> AccountBaseTransformer.transformInvoiceBaseEntityToModel(accountPayableEntity,
            accountPayableEntity.getAccountPayableId(), TransactionType.ACCOUNT_PAYABLE.name()));

    Flux<AccountBase> receivableFlux = queryService.findAccountReceivableByParametersAndStatus(payload, invoiceStatus)
        .publishOn(Schedulers.boundedElastic())
        .map(accountReceivableEntity -> AccountBaseTransformer.transformInvoiceBaseEntityToModel(accountReceivableEntity,
            accountReceivableEntity.getAccountReceivableId(), TransactionType.ACCOUNT_RECEIVABLE.name()));

    StatementType invoiceType = StatementType.SIP_NETOFF;

    PayableStatementEntity payableStatementEntity = createNewEntity(payload);
    generationService.savePayableStatement(payableStatementEntity).blockFirst();

    //Net Off Statement
    if (invoiceType.equals(StatementType.SIP_NETOFF)) { //NOSONAR
      //SIP Net Off mean payable amount will be positive, so there's nothing to do.

      //SIP Net Off mean receivable amount will be negative
      receivableFlux = receivableFlux.map(accountBase -> {
        accountBase.setTotalAmount(accountBase.getTotalAmount().negate());
        return accountBase;
      });
    }

    AtomicReference<BigDecimal> totalAmount = new AtomicReference<>(BigDecimal.ZERO);
    Flux<PayableStatementItemEntity> payableStatementItemEntityFlux = payableFlux.concatWith(receivableFlux)
        .publishOn(Schedulers.boundedElastic())
        //.limitRate(fetchSize)
        .map(accountBase -> {
          totalAmount.set(totalAmount.get().add(accountBase.getTotalAmount()));
          return AccountBaseTransformer.transformModelToPayableStatementItemEntity(accountBase, payableStatementEntity.getPayableStatementId(), true);
        });

    Flux<PayableStatementItemEntity> finalFlux = payableStatementItemEntityFlux
        .publishOn(Schedulers.boundedElastic())
        //.limitRate(fetchSize)
        .flatMap(payableStatementItemEntity -> generationService.savePayableStatementItem(payableStatementItemEntity));

    finalFlux
        .collectList()
        .block();

    payableStatementEntity.setTotalAmount(totalAmount.get());
    generationService.updatePayableStatement(payableStatementEntity).blockFirst();

    log.info("Finish grouping invoice....");
  }

  private PayableStatementEntity createNewEntity(InvoiceGroupPayload payload) {
    final String statementId = String.join(payload.getInvoicePartnerId(), payload.getInvoiceAgreementId(), String.valueOf(payload.getBatchTime().getTime()));
    return PayableStatementEntity.builder()
        .payableStatementId("SIP-" + statementId)
        .totalAmount(BigDecimal.ZERO)
        //.isNew(true)
        .build();
  }

  @Override
  public void run(String... args) throws Exception {
    log.info("Start running invoice group batch, with invoice-partner-id: {}, invoice-agreement-id: {}, batch-time:{}", invoicePartnerId, invoiceAgreementId, batchTime);

    Date batchDate;
    try {
      batchDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSX").parse(this.batchTime);
    } catch (ParseException e) {
      log.error("There's an error when parsing batchTime, err: {}", e.getMessage());
      return;
    }

    InvoiceGroupPayload payload = new InvoiceGroupPayload(invoicePartnerId, invoiceAgreementId, batchDate);

    if (Boolean.parseBoolean(isReactive)) {
      groupInvoiceReactive(payload, InvoiceStatus.OPEN);
    }
  }
}
