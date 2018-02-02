package ru.comptech.bc.server.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.ManagedTransaction;
import org.web3j.tx.TransactionManager;
import ru.comptech.bc.server.contracts.Voting;
import ru.comptech.bc.server.model.Votation;
import ru.comptech.bc.server.util.ExceptionSupplier;
import ru.comptech.bc.server.util.DateFormatter;
import ru.comptech.bc.server.transactions.TransactionService;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/rest/votings")
public class VotingsController {

    @Autowired
    private Web3j web3j;

    @Autowired
    private TransactionService transactionService;

    @Value("${contracts.voting}")
    private String address;

    private static final RestException NODE_IS_UNAVAILABLE =
            new RestException(HttpStatus.GATEWAY_TIMEOUT, "Node is unavailable");
    private static final RestException NOT_FOUND =
            new RestException(HttpStatus.NOT_FOUND, "Not found");
    private static final RestException INCORRECT_DATA =
            new RestException(HttpStatus.BAD_REQUEST, "Incorrect data");
    private static final RestException NOT_ENOUGH_MONEY =
            new RestException(HttpStatus.PAYMENT_REQUIRED, "Not enough money");

    @GetMapping
    public Map<String, Integer> get() throws Exception {
        final TransactionManager manager = transactionService.get(TransactionService.Type.READ);

        final BigInteger currId = readTransaction(() -> {
            final Voting voting = Voting.load(address, web3j, manager,
                    ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT);
            return voting.currId().send();
        });

        return Map.of("maxId", currId.intValue() - 1);
    }

    @GetMapping(path = "/{id}")
    public Map<String, Object> getById(@PathVariable Integer id) throws Exception {
        final TransactionManager manager = transactionService.get(TransactionService.Type.READ);

        final Voting voting = Voting.load(address, web3j, manager,
                ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT);

        final BigInteger votationId = BigInteger.valueOf(id);
        final Votation votation = getVotationById(voting, votationId);

        final ArrayList[] votes = new ArrayList[votation.getOptions().length];
        for (int i = 0; i < votes.length; i++) {
            votes[i] = new ArrayList<>();
        }
        readTransaction(() -> {
            for (int i = 0; i < votation.getVotesCount(); i++) {
                final String address = voting.addresses(votationId, BigInteger.valueOf(i)).send();
                final int optionId = voting.votes(votationId, address).send().intValue();
                votes[optionId - 1].add(address);
            }
            return 0;
        });

        final Map[] options = new Map[votes.length];
        for (int i = 0; i < options.length; i++) {
            options[i] = Map.of(
                    "number", i + 1,
                    "name", votation.getOptions()[i],
                    "votes", votes[i].size(),
                    "wallets", votes[i]
            );
        }

        return Map.of(
                "id", id,
                "title", votation.getTitle(),
                "description", votation.getDescription(),
                "options", options,
                "authorWallet", votation.getAuthor(),
                "begin", DateFormatter.dateToString(votation.getStart()),
                "end", DateFormatter.dateToString(votation.getEnd()),
                "active", votation.isActive()
        );
    }

    @PostMapping
    public Map<String, Integer> post(@RequestBody Map<String, Object> json) throws Exception {
        final TransactionManager manager = transactionService.get(TransactionService.Type.WRITE);

        final Voting voting = Voting.load(address, web3j, manager,
                ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT);

        final String title, description, rawOptions;
        final int optionsCount;
        final Date begin, end;
        try {
            title = (String) json.get("title");
            description = (String) json.get("description");

            final ArrayList<String> tmp = (ArrayList<String>) json.get("options");
            rawOptions = tmp.stream().collect(Collectors.joining(","));
            optionsCount = tmp.size();

            begin = DateFormatter.stringToData((String) json.get("begin"));
            end = DateFormatter.stringToData((String) json.get("end"));
        } catch (Exception e) {
            throw INCORRECT_DATA;
        }

        final BigInteger id = writeTransaction(() -> {
            final TransactionReceipt transaction = voting.createVotation(title, description,
                    rawOptions, BigInteger.valueOf(optionsCount),
                    BigInteger.valueOf((end.getTime() - begin.getTime()) / 1000)).send();
            return voting.getCreationEvents(transaction).get(0).votationId;
        });

        return Map.of("id", id.intValue());
    }

    @PutMapping(path = "/{id}")
    public void putById(@PathVariable Integer id,
                        @RequestBody Map<String, Object> json) throws Exception {
        final TransactionManager manager = transactionService.get(TransactionService.Type.WRITE);

        final Voting voting = Voting.load(address, web3j, manager,
                ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT);

        final BigInteger votationId = BigInteger.valueOf(id);
        final Votation votation = getVotationById(voting, votationId);

        final int optionId;
        try {
            optionId = (int) json.get("option");
            if (optionId == 0 || optionId > votation.getOptions().length)
                throw new Exception();
        } catch (Exception e) {
            throw INCORRECT_DATA;
        }

        writeTransaction(() -> {
            voting.vote(votationId, BigInteger.valueOf(optionId)).send();
            return 0;
        });
    }

    private Votation getVotationById(Voting voting, BigInteger votationId) throws Exception {
        try {
            return readTransaction(() -> voting.votations(votationId).send());
        } catch (IllegalArgumentException e) {
            throw NOT_FOUND;
        }
    }

    private <T> T readTransaction(ExceptionSupplier<T> supplier) throws Exception {
        try {
            return supplier.run();
        } catch (IOException e) {
            throw NODE_IS_UNAVAILABLE;
        }
    }

    private <T> T writeTransaction(ExceptionSupplier<T> supplier) throws Exception {
        try {
            return supplier.run();
        } catch (IOException e) {
            throw NODE_IS_UNAVAILABLE;
        } catch (RuntimeException e) {  //  TODO: replace RuntimeException with smt better
            throw NOT_ENOUGH_MONEY;
        }
    }
}
