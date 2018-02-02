package ru.comptech.bc.server.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.tx.ReadonlyTransactionManager;
import org.web3j.tx.TransactionManager;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private Web3j web3j;

    @Override
    public TransactionManager get(Type type) {
        switch (type) {
            case READ:
                return getRead();
            case WRITE:
                return getWrite();
            default:
                throw new UnsupportedOperationException();
        }
    }

    private TransactionManager getRead() {
        return new ReadonlyTransactionManager(web3j, null);
    }

    private TransactionManager getWrite() {
        throw new UnsupportedOperationException();
    }
}
