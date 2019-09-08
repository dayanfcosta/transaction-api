package com.dayanfcosta.revolut.transfer.api;

import com.dayanfcosta.revolut.transfer.api.handler.Error;
import com.dayanfcosta.revolut.transfer.command.transaction.TransactionSaveCommand;
import com.dayanfcosta.revolut.transfer.model.account.Account;
import com.dayanfcosta.revolut.transfer.model.transaction.Transaction;
import com.dayanfcosta.revolut.transfer.model.transaction.TransactionBuilder;
import com.dayanfcosta.revolut.transfer.repository.AccountRepository;
import com.dayanfcosta.revolut.transfer.repository.TransactionRepository;
import io.micronaut.context.ApplicationContext;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.runtime.server.EmbeddedServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.dayanfcosta.revolut.transfer.model.transaction.TransactionType.CREDIT;
import static io.micronaut.http.HttpRequest.GET;
import static io.micronaut.http.HttpRequest.POST;
import static io.micronaut.http.HttpStatus.CREATED;
import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author dayan.costa
 */
class TransactionResourceTest {

  private RxHttpClient httpClient;
  private AccountRepository accountRepository;
  private TransactionRepository transactionRepository;

  private Account source;
  private EmbeddedServer server;
  private Transaction transaction;

  @BeforeEach
  void setUp() {
    server = ApplicationContext.build().run(EmbeddedServer.class);
    var context = server.getApplicationContext();

    httpClient = context.createBean(RxHttpClient.class, server.getURL());
    transactionRepository = context.getBean(TransactionRepository.class);
    accountRepository = context.getBean(AccountRepository.class);

    source = accountRepository.save(new Account("1"));
    transaction = TransactionBuilder.create()
        .source(source)
        .ammount(TEN)
        .type(CREDIT)
        .build();
    transaction = transactionRepository.save(transaction);
  }

  @AfterEach
  void tearDown() {
    server.close();
  }

  @Test
  @DisplayName("Should realize a credit transaction")
  void testCreditTransaction() {
    // given
    var initialBalance = source.getBalance();
    var total = transactionRepository.findAll().size();
    var command = new TransactionSaveCommand(source.getId(), 0, ONE, CREDIT);

    // when
    var response = client().exchange(POST("/api/transactions", command));
    source = accountRepository.findById(source.getId()).get();

    // then
    assertThat(response.getStatus().getCode()).isEqualTo(CREATED.getCode());
    assertThat(source.getBalance()).isEqualTo(initialBalance.add(ONE));
    assertThat(transactionRepository.findAll()).hasSize(total + 1);
  }

  @Test
  @DisplayName("Should get an 404 on finding transaction by id")
  void testTransactionById_notFound() {
    assertThatThrownBy(() -> client().retrieve(GET("/api/transactions/999"), Error.class))
        .isInstanceOf(HttpClientResponseException.class)
        .hasMessage("Transaction 999 not found");
  }

  @Test
  @DisplayName("Should find a transaction with the given id")
  void testTransactionById_transactionFound() {
    // given
    var id = transaction.getId();

    // when
    var response = client().retrieve(GET("/api/transactions/" + id));

    //then
    assertThat(response).isNotNull();
  }

  @Test
  @DisplayName("Should retrieve a transaction from an account")
  void testFromAccount() {
    // given
    var sourceId = source.getId();

    // when
    var response = client().retrieve(GET("/api/transactions/account/" + sourceId));

    // then
    assertThat(response).isNotNull();
  }

  private BlockingHttpClient client() {
    return httpClient.toBlocking();
  }

}