package com.dayanfcosta.revolut.transfer.executor;

import com.dayanfcosta.revolut.transfer.model.transaction.Transaction;
import com.dayanfcosta.revolut.transfer.model.transaction.TransactionState;
import com.dayanfcosta.revolut.transfer.model.transaction.TransactionType;
import com.dayanfcosta.revolut.transfer.repository.AccountRepository;
import com.dayanfcosta.revolut.transfer.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

/**
 * @author dayan.costa
 */
@Singleton
class TransferExecutor extends AbstractTransactionExecutor {

  private static final Logger LOGGER = LoggerFactory.getLogger(Transaction.class);

  public TransferExecutor(AccountRepository accountRepository,
                          TransactionRepository transactionRepository) {
    super(accountRepository, transactionRepository);
  }

  @Override
  public boolean apply(Transaction transaction) {
    return TransactionType.TRANSFER.equals(transaction.getType());
  }

  @Override
  protected Transaction process(Transaction transaction) {
    LOGGER.info("Executing transfer transaction {}", transaction);
    debit(transaction);
    credit(transaction);
    return transaction(transaction, TransactionState.DONE).build();
  }

  private void credit(Transaction transaction) {
    var destination = transaction.getDestination();
    if (destination.isPresent()) {
      destination.get().credit(transaction.getAmmount());
    } else {
      throw new RuntimeException("Destination account is missing");
    }
  }

  private void debit(Transaction transaction) {
    var source = transaction.getSource();
    source.debit(transaction.getAmmount());
  }

  @Override
  protected Logger logger() {
    return LOGGER;
  }
}
