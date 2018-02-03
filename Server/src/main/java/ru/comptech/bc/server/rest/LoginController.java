package ru.comptech.bc.server.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.comptech.bc.server.transactions.TransactionService;

import java.util.Map;

@RestController
public class LoginController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping(path = "/rest/login")
    public Map<String, Boolean> get() {
        boolean loggedIn = true;
        try {
            transactionService.get(TransactionService.Type.WRITE);
        } catch (RestException e) {
            loggedIn = false;
        }

        return Map.of("loggedIn", loggedIn);
    }
}
