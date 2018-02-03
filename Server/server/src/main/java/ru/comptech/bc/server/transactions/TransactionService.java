package ru.comptech.bc.server.transactions;

import org.web3j.tx.TransactionManager;

public interface TransactionService {

    enum Type {
        READ,
        WRITE
    }

    TransactionManager get(Type type);
}
