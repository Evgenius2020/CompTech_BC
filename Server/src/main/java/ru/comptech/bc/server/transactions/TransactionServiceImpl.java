package ru.comptech.bc.server.transactions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.ReadonlyTransactionManager;
import org.web3j.tx.TransactionManager;
import ru.comptech.bc.server.rest.RestException;
import ru.comptech.bc.server.security.auth.WalletAuthentication;

import java.io.IOException;

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
        final Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof WalletAuthentication) || !authentication.isAuthenticated())
            throw new RuntimeException("Not authorized");

        return new RawTransactionManager(web3j, (Credentials) authentication.getDetails());
    }
}
