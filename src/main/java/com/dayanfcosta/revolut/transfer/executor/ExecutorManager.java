package com.dayanfcosta.revolut.transfer.executor;

import com.dayanfcosta.revolut.transfer.exception.TransactionExecutorNotFound;
import com.dayanfcosta.revolut.transfer.model.transaction.Transaction;

import javax.inject.Singleton;
import java.util.List;

/**
 * @author dayan.costa
 */
@Singleton
public class ExecutorManager {

  private final List<TransactionExecutor> executors;

  public ExecutorManager(List<TransactionExecutor> executors) {
    this.executors = executors;
  }

  public void execute(Transaction transaction) {
    executor(transaction).execute(transaction);
  }

  private TransactionExecutor executor(final Transaction transaction) {
    return executors.stream()
        .filter(executor -> executor.apply(transaction))
        .findFirst()
        .orElseThrow(() -> new TransactionExecutorNotFound(transaction));
  }
}
