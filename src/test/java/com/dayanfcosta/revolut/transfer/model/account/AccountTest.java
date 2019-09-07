package com.dayanfcosta.revolut.transfer.model.account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

/**
 * @author dayan.costa
 */
class AccountTest {

  private Account account;

  @BeforeEach
  void setUp() {
    account = new Account("123");
  }

  @Test
  @DisplayName("Account number is blank")
  void testConstructor_numberBlank() {
    assertThatThrownBy(() -> new Account(""))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Account Number Invalid");
  }

  @Test
  @DisplayName("Account number is null")
  void testConstructor_numberNull() {
    assertThatThrownBy(() -> new Account(null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("Account Number Invalid");
  }

  @Test
  @DisplayName("Withdraw null value")
  void testWithdraw_nullValue() {
    assertThatThrownBy(() -> account.debit(null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("Invalid ammount");
  }

  @Test
  @DisplayName("Withdraw value negative")
  void testWithdraw_negativeValue() {
    assertThatThrownBy(() -> account.debit(new BigDecimal(-1)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Withdraws just allow ammounts bigger than 0 (zero)");
  }

  @Test
  @DisplayName("Withdraw value zero")
  void testWithdraw_zeroAmmout() {
    assertThatThrownBy(() -> account.debit(BigDecimal.ZERO))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Withdraws just allow ammounts bigger than 0 (zero)");
  }

  @Test
  @DisplayName("Withdraw with a valid value")
  void testWithdraw() {
    account.credit(BigDecimal.TEN);
    account.debit(BigDecimal.ONE);
    assertThat(account.getBalance()).isEqualTo(new BigDecimal(9));
  }

  @Test
  @DisplayName("Deposit null ammout")
  void testDeposit_nullValue() {
    assertThatThrownBy(() -> account.credit(null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("Invalid ammount");
  }

  @Test
  @DisplayName("Deposit with a negative ammout")
  void testDeposit_negativeValue() {
    assertThatThrownBy(() -> account.credit(new BigDecimal(-1)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Deposits just allow ammounts bigger than 0 (zero)");
  }

  @Test
  @DisplayName("Deposit with a zero ammout")
  void testDeposit_zeroAmmout() {
    assertThatThrownBy(() -> account.credit(BigDecimal.ZERO))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Deposits just allow ammounts bigger than 0 (zero)");
  }

  @Test
  @DisplayName("Deposit with a valid value")
  void testDeposit() {
    account.credit(BigDecimal.ONE);

    assertThat(account.getBalance()).isEqualTo(BigDecimal.ONE);
  }

  @Test
  @DisplayName("Equals")
  void testEquals() {
    assertThat(account).isEqualTo(account);
    assertThat(account).isNotEqualTo(null);
    assertThat(account).isNotEqualTo(new Object());
    assertThat(account).isEqualTo(new Account("123"));
    assertThat(account).isEqualTo(new Account("123"));
    assertThat(account).isNotEqualTo(new Account("1233"));
  }

  @Test
  @DisplayName("HashCode")
  void testHashCode() {
    assertThat(account.hashCode()).isEqualTo(account.hashCode());
    assertThat(account.hashCode()).isNotEqualTo(null);
    assertThat(account.hashCode()).isNotEqualTo(new Object().hashCode());
    assertThat(account.hashCode()).isEqualTo(new Account("123").hashCode());
    assertThat(account.hashCode()).isEqualTo(new Account("123").hashCode());
    assertThat(account.hashCode()).isNotEqualTo(new Account("1233").hashCode());
  }
}