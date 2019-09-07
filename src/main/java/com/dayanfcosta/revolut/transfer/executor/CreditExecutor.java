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
class CreditExecutor extends AbstractTransactionExecutor {

  private static final Logger LOGGER = LoggerFactory.getLogger(CreditExecutor.class);

  public CreditExecutor(AccountRepository accountRepository,
                        TransactionRepository repository) {
    super(accountRepository, repository);
  }

  @Override
  protected Transaction process(Transaction transaction) {
    LOGGER.info("Executing debit transaction {}", transaction);
    var account = transaction.getSource();
    account.credit(transaction.getAmmount());
    return transaction(transaction, TransactionState.DONE).build();
  }

  @Override
  protected Logger logger() {
    return LOGGER;
  }

  @Override
  public boolean apply(Transaction transaction) {
    return TransactionType.CREDIT.equals(transaction.getType());
  }
}
