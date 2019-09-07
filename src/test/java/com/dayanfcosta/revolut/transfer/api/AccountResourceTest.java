package com.dayanfcosta.revolut.transfer.api;

import com.dayanfcosta.revolut.transfer.command.account.AccountSaveCommand;
import com.dayanfcosta.revolut.transfer.model.account.Account;
import com.dayanfcosta.revolut.transfer.repository.AccountRepository;
import io.micronaut.context.ApplicationContext;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.runtime.server.EmbeddedServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static io.micronaut.http.HttpHeaders.LOCATION;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author dayan.costa
 */
class AccountResourceTest {

  private RxHttpClient httpClient;
  private AccountSaveCommand command;
  private AccountRepository repository;

  private EmbeddedServer server;

  @BeforeEach
  void setUp() {
    server = ApplicationContext.build().run(EmbeddedServer.class);
    var context = server.getApplicationContext();

    httpClient = context.createBean(RxHttpClient.class, server.getURL());
    repository = context.getBean(AccountRepository.class);

    command = new AccountSaveCommand("1");
  }

  @AfterEach
  void tearDown() {
    server.close();
  }

  @Test
  @DisplayName("Should save a new account")
  void testSave() {
    assertThat(repository.findAll()).hasSize(0);

    var response = client().exchange(HttpRequest.POST("/api/accounts", command));

    assertThat(response.getStatus().getCode()).isEqualTo(HttpStatus.CREATED.getCode());
    assertThat(repository.findAll()).hasSize(1);
  }

  @Test
  @DisplayName("Should return an account with the specified ID")
  void testById() {
    var response = client().exchange(HttpRequest.POST("/api/accounts", command));

    var account = client().retrieve(HttpRequest.GET(response.header(LOCATION)), Account.class);

    assertThat(account).isNotNull();
    assertThat(account.getId()).isGreaterThan(0);
  }

  @Test
  @DisplayName("Should return a list containing all accounts")
  void testFindAll() {
    var totalAccounts = 10;
    insertAccounts(totalAccounts);

    var accounts = client()
        .retrieve(HttpRequest.GET("/api/accounts"), Argument.of(List.class, Account.class));

    assertThat(accounts).isNotEmpty();
    assertThat(accounts).hasSize(totalAccounts);
  }

  private void insertAccounts(int ammount) {
    Stream.iterate(1, n -> n + 1)
        .limit(ammount)
        .forEach(i -> {
          var command = new AccountSaveCommand(String.valueOf(i));
          client().exchange(HttpRequest.POST("/api/accounts", command));
        });
  }

  private BlockingHttpClient client() {
    return httpClient.toBlocking();
  }

}