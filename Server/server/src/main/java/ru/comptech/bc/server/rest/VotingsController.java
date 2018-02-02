package ru.comptech.bc.server.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.protocol.Web3j;
import org.web3j.tx.Contract;
import org.web3j.tx.ManagedTransaction;
import org.web3j.tx.ReadonlyTransactionManager;
import org.web3j.tx.TransactionManager;
import ru.comptech.bc.server.contracts.Voting;
import ru.comptech.bc.server.model.Votation;
import ru.comptech.bc.server.util.DateFormatter;
import ru.comptech.bc.server.util.TransactionService;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping(path = "/rest/votings")
public class VotingsController {

    @Autowired
    private Web3j web3j;

    @Autowired
    private TransactionService transactionService;

    @Value("${contracts.voting}")
    private String address;

    @GetMapping
    public Map<String, Integer> get() throws Exception {
        final TransactionManager manager = transactionService.get(TransactionService.Type.READ);

        final Voting voting = Voting.load(address, web3j, manager,
                ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT);
        final BigInteger currId = voting.currId().send();

        return Map.of("maxId", currId.intValue() - 1);
    }

    @GetMapping(path = "/{votingId}")
    public Map<String, Object> getById(@PathVariable Integer votingId) throws Exception {
        final TransactionManager manager = transactionService.get(TransactionService.Type.READ);

        final Voting voting = Voting.load(address, web3j, manager,
                ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT);

        final BigInteger votationId = BigInteger.valueOf(votingId);
        final Votation votation;
        try {
            votation = voting.votations(votationId).send();
        } catch (IllegalArgumentException e) {
            throw new RestException(HttpStatus.NOT_FOUND, "Not found");
        }

        final ArrayList[] votes = new ArrayList[votation.getOptions().length];
        for (int i = 0; i < votes.length; i++) {
            votes[i] = new ArrayList<>();
        }
        for (int i = 0; i < votation.getVotesCount(); i++) {
            final String address = voting.addresses(votationId, BigInteger.valueOf(i)).send();
            final int optionId = voting.votes(votationId, address).send().intValue();
            votes[optionId - 1].add(address);
        }

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
                "id", votingId,
                "title", votation.getTitle(),
                "description", votation.getDescription(),
                "options", options,
                "authorWallet", votation.getAuthor(),
                "begin", DateFormatter.dateToString(votation.getStart()),
                "end", DateFormatter.dateToString(votation.getEnd()),
                "active", votation.isActive()
        );
    }
}
