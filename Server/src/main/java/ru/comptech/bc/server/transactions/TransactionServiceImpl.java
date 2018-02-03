package ru.comptech.bc.server.transactions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.ReadonlyTransactionManager;
import org.web3j.tx.TransactionManager;

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
        final Credentials credentials;
        try {
            credentials = WalletUtils.loadCredentials(
                    "1111",
                    "/home/bolodya/Downloads/" +
                            "UTC--2018-02-01T07-37-31.095482915Z--3a192eeeae04ab16f59d54aef2fb33b9d35592f0");
//                            "UTC--2018-02-01T07-37-40.709258427Z--bfcb5ce3c723404ac96a95685377c1526927c070");
        } catch (CipherException | IOException e) {
            throw new UnsupportedOperationException();
        }

        return new RawTransactionManager(web3j, credentials);
    }
}
