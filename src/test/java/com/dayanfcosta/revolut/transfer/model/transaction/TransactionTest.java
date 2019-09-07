package com.dayanfcosta.revolut.transfer.model.transaction;

import com.dayanfcosta.revolut.transfer.model.account.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static java.math.BigDecimal.*;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.*;

/**
 * @author dayan.costa
 */
class TransactionTest {

  private Account account1;
  private Transaction transaction;
  private Optional<Account> account2;

  @BeforeEach
  void setUp() {
    account1 = new Account("1234");
    account2 = Optional.ofNullable(new Account("12345"));
    transaction = TransactionBuilder.create().source(account1).destination(account2).ammount(ONE).type(TransactionType.DEBIT).build();
  }

  @Test
  @DisplayName("Constructor with invalid source account")
  void testConstructor_invalidSourceAccount() {
    assertThatNullPointerException()
        .isThrownBy(() -> TransactionBuilder.create()
            .source(null)
            .ammount(ONE)
            .type(TransactionType.DEBIT)
            .build())
        .withMessage("Source Account Invalid");
  }

  @Test
  @DisplayName("Constructor with invalid destination account")
  void testConstructor_invalidDestinationAccount() {
    assertThatIllegalArgumentException()
        .isThrownBy(() -> TransactionBuilder.create()
            .destination(empty())
            .source(account1)
            .type(TransactionType.TRANSFER)
            .ammount(ONE)
            .build())
        .withMessage("Transfers must have a destination account");
  }

  @Test
  @DisplayName("Constructor with negative Ammount")
  void testConstructor_negativeAmmout() {
    assertThatIllegalArgumentException()
        .isThrownBy(() -> TransactionBuilder.create()
            .ammount(new BigDecimal(-1))
            .destination(account2)
            .source(account1)
            .type(TransactionType.DEBIT)
            .build())
        .withMessage("Transaction's ammout must be bigger than 0 (zero)");
  }

  @Test
  @DisplayName("Constructor with zero Ammount")
  void testConstructor_zeroAmmout() {
    assertThatIllegalArgumentException()
        .isThrownBy(() -> TransactionBuilder.create()
            .destination(account2)
            .source(account1)
            .ammount(ZERO)
            .type(TransactionType.DEBIT)
            .build())
        .withMessage("Transaction's ammout must be bigger than 0 (zero)");
  }

  @Test
  @DisplayName("Constructor with same source and destination")
  void testConstructor_equalsSourceAndDestination() {
    assertThatIllegalArgumentException()
        .isThrownBy(() -> TransactionBuilder.create()
            .destination(of(account1))
            .source(account1)
            .type(TransactionType.TRANSFER)
            .ammount(TEN)
            .build())
        .withMessage("Transfers must have differents source and destination accounts");
  }

  @Test
  @DisplayName("Equals")
  void testEquals() {
    var account3 = new Account("1111");
    assertThat(transaction).isNotEqualTo(null);
    assertThat(transaction).isEqualTo(transaction);
    assertThat(transaction).isNotEqualTo(new Object());
    assertThat(transaction).isNotEqualTo(TransactionBuilder.create().source(account1).destination(account2).ammount(TEN).type(TransactionType.DEBIT).build());
    assertThat(transaction).isEqualTo(TransactionBuilder.create().source(account1).destination(account2).date(transaction.getDate()).ammount(ONE).type(TransactionType.DEBIT).build());
    assertThat(transaction).isNotEqualTo(TransactionBuilder.create().source(account1).destination(of(account3)).ammount(ONE).type(TransactionType.DEBIT).build());
    assertThat(transaction).isNotEqualTo(TransactionBuilder.create().source(account3).destination(of(account1)).ammount(ONE).type(TransactionType.DEBIT).build());
    assertThat(transaction).isNotEqualTo(TransactionBuilder.create().source(account3).destination(account2).ammount(ONE).type(TransactionType.DEBIT).build());
  }

  @Test
  @DisplayName("Hash Code")
  void testHashCode() {
    var account3 = new Account("1111");
    assertThat(transaction.hashCode()).isNotEqualTo(null);
    assertThat(transaction.hashCode()).isEqualTo(transaction.hashCode());
    assertThat(transaction.hashCode()).isNotEqualTo(new Object().hashCode());

    assertThat(transaction.hashCode())
        .isNotEqualTo(TransactionBuilder.create()
            .destination(account2)
            .source(account1)
            .ammount(TEN)
            .type(TransactionType.DEBIT)
            .build().hashCode());

    assertThat(transaction.hashCode())
        .isEqualTo(TransactionBuilder.create()
            .date(transaction.getDate())
            .destination(account2)
            .source(account1)
            .ammount(ONE)
            .type(TransactionType.DEBIT)
            .build().hashCode());

    assertThat(transaction.hashCode())
        .isNotEqualTo(TransactionBuilder.create()
            .destination(of(account3))
            .source(account1)
            .ammount(ONE)
            .type(TransactionType.DEBIT)
            .build().hashCode());

    assertThat(transaction.hashCode())
        .isNotEqualTo(TransactionBuilder.create()
            .destination(of(account1))
            .source(account3)
            .ammount(ONE)
            .type(TransactionType.DEBIT)
            .build().hashCode());

    assertThat(transaction.hashCode())
        .isNotEqualTo(TransactionBuilder.create()
            .destination(account2)
            .source(account3)
            .ammount(ONE)
            .type(TransactionType.DEBIT)
            .build().hashCode());
  }
}