package ru.comptech.bc.server.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import org.web3j.tx.TransactionManager;
import org.web3j.utils.Convert;
import ru.comptech.bc.server.contracts.Donating;
import ru.comptech.bc.server.model.Donation;
import ru.comptech.bc.server.util.DateFormatter;
import ru.comptech.bc.server.util.ExceptionSupplier;
import ru.comptech.bc.server.transactions.TransactionService;


import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import static org.bouncycastle.pqc.math.linearalgebra.IntegerFunctions.pow;
import static org.web3j.tx.Contract.GAS_LIMIT;
import static org.web3j.tx.Contract.GAS_PRICE;


@RestController
@RequestMapping(path = "/rest/payments")
public class DonatingsController {

    @Autowired
    private Web3j web3j;

    @Autowired
    private TransactionService transactionService;

    @Value("${contracts.payments}")
    private String contractAddres;

    private static final RestException NODE_IS_UNAVAILABLE =
            new RestException(HttpStatus.GATEWAY_TIMEOUT, "Node is unavailable");
    private static final RestException NOT_FOUND =
            new RestException(HttpStatus.NOT_FOUND, "Not found");
    private static final RestException INCORRECT_DATA =
            new RestException(HttpStatus.BAD_REQUEST, "Incorrect data");
    private static final RestException NOT_ENOUGH_MONEY =
            new RestException(HttpStatus.PAYMENT_REQUIRED, "Not enough money");
    private static final RestException CONTRACT_EXCEPTION =
            new RestException(HttpStatus.BAD_GATEWAY, "Contract exception");


    @GetMapping
        public Object getMaxId() throws Exception {
            final TransactionManager manager = transactionService.get(TransactionService.Type.READ);
            final Donating donating = Donating.load(contractAddres,web3j,manager,GAS_PRICE,GAS_LIMIT);

            final BigInteger currId = donating.currId().send();

            return Map.of("maxId",currId.intValue() - 1);
        }

        @GetMapping(path = "/{paymentId}")
        public Object getPaymentById(@PathVariable Integer paymentId) throws Exception {
            final TransactionManager manager = transactionService.get(TransactionService.Type.READ);
            final Donating donating = Donating.load(contractAddres, web3j, manager, GAS_PRICE, GAS_LIMIT);

            final BigInteger id = BigInteger.valueOf(paymentId);
            final Donation donation;
            try {
                donation = donating.donations(id).send();
            } catch (IllegalArgumentException e) {
                throw new RestException(HttpStatus.NOT_FOUND, "Not found");
            }


            final ArrayList<String> donaters = new ArrayList<String>();
            final ArrayList<Long> summ = new ArrayList<>();

            for (int i = 1; i <= donation.getAmountOfPayers(); i++) {
                final String wallet = donating.moneysenders(id,BigInteger.valueOf(i)).send();
                donaters.add(wallet);

                final BigInteger weiValue = donating.donates(id,wallet).send();
                final BigInteger ethValue = (Convert.fromWei(weiValue.toString(), Convert.Unit.ETHER)).toBigInteger();
                summ.add(ethValue.longValue());
            }

            final ArrayList<Map> parts = new ArrayList<>();
            for (int i = 0; i < donaters.size(); i++) {
                parts.add(Map.of("wallet",donaters.get(i),"amount", summ.get(i)));
            }


            final int amountOfPayers = donation.getAmountOfPayers();
            final String title = donation.getTitle();
            final String description = donation.getDescription();

            final String walletAuthor = donation.getWalletAuthor();
            final String walletReceiver = donation.getWalletReceiver();
            final Date startingTime = donation.getStartingTime();
            final Date endingTime = donation.getEndingTime();
            final boolean isActive = donation.getIsActive();

            final BigInteger curBalance = donation.getCurrentBalance();
            final BigInteger amountGot = (Convert.fromWei(curBalance.toString(), Convert.Unit.ETHER)).toBigInteger();
            final BigInteger needToCollect = donation.getNeedToCollect();
            final BigInteger amountNeed = (Convert.fromWei(needToCollect.toString(), Convert.Unit.ETHER)).toBigInteger();

            final HashMap<String,Object> map = new HashMap<>();
            map.putAll(Map.of("id",id,"title",title,"description",description,"amountNeed", amountNeed, "amountGot",amountGot,
                    "parts",parts,"authorWallet",walletAuthor,"receiverWallet",walletReceiver,"begin",startingTime.getTime()/1000,
                    "end",endingTime.getTime()/1000));
            map.put("isActive",isActive);
            map.put("amountOfPayers", amountOfPayers);

            return map;
        }

        @PostMapping
        //@Secured("ROLE_USER")
        public Object createNewPayment (@RequestBody Map<String,Object> payment) throws Exception{
            final TransactionManager manager = transactionService.get(TransactionService.Type.WRITE);

            final Donating donating = Donating.load(contractAddres, web3j, manager, GAS_PRICE, GAS_LIMIT);

            final String title,description,walletReceiver;
            final BigInteger amountNeed,secondsToVote;
            final BigInteger weiValue;
            try {
                title = (String) payment.get("title");
                description = (String) payment.get("description");
                amountNeed = BigInteger.valueOf((int) payment.get("amountNeed"));
                walletReceiver = (String) payment.get("receiverWallet");
                final Date dateBegin = DateFormatter.stringToData((String) payment.get("begin"));
                final Date dateEnd = DateFormatter.stringToData((String) payment.get("end"));
                weiValue = Convert.toWei(amountNeed.toString(), Convert.Unit.ETHER).toBigInteger();
                secondsToVote = BigInteger.valueOf((dateEnd.getTime() - dateBegin.getTime()) / 1000);
            } catch (Exception e) {
                throw INCORRECT_DATA;
            }

            final BigInteger id = writeTransaction(() -> {
                final TransactionReceipt transaction = donating.createDonation(title,description,
                        weiValue,walletReceiver,secondsToVote).send();
                testTransaction(transaction);
                return donating.getCreationEvents(transaction).get(0).donationId;
            });

            return Map.of("id",id);
        }

        @PutMapping(path = "/{paymentId}")
        //@Secured("ROLE_USER")
        public void Donate(@PathVariable Integer paymentId,@RequestBody Map<String,Integer> json) throws Exception{
            final TransactionManager manager = transactionService.get(TransactionService.Type.WRITE);
            final Donating donating = Donating.load(contractAddres, web3j, manager, GAS_PRICE, GAS_LIMIT);

            final BigInteger id;
            try {

                if (paymentId == 0 || paymentId > donating.currId().send().intValue())
                    throw new Exception();
                id = BigInteger.valueOf(paymentId);
            } catch (Exception e) {
                throw INCORRECT_DATA;
            }

            final BigInteger ethValue =  BigInteger.valueOf(json.get("amount"));

            final BigInteger weiValue = (Convert.toWei(ethValue.toString(), Convert.Unit.ETHER)).toBigInteger();
            writeTransaction(() -> {
                final TransactionReceipt transaction =
                        donating.donate(id, weiValue).send();
                testTransaction(transaction);
                return 0;
            });

        }

    private Donation getDonationById(Donating donating, BigInteger donationnId) throws Exception {
        try {
            return readTransaction(() -> donating.donations(donationnId).send());
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
        } catch (RestException e) {
            throw e;    //  RestException extends RuntimeException
        } catch (IOException e) {
            throw NODE_IS_UNAVAILABLE;
        } catch (RuntimeException e) {  //  TODO: replace RuntimeException with smt better
            throw NOT_ENOUGH_MONEY;
        }
    }

    private void testTransaction(TransactionReceipt transaction) {
        if (transaction.getGasUsed().equals(GAS_LIMIT))
            throw CONTRACT_EXCEPTION;
    }


}
