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
import ru.comptech.bc.server.contracts.Donating;
import ru.comptech.bc.server.model.Donation;
import ru.comptech.bc.server.util.DateFormatter;
import ru.comptech.bc.server.util.ExceptionSupplier;
import ru.comptech.bc.server.transactions.TransactionService;


import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

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

            final BigInteger Id = BigInteger.valueOf(paymentId);

            final Donation donation;
            try {
                donation = donating.donations(Id).send();
            } catch (IllegalArgumentException e) {
                throw new RestException(HttpStatus.NOT_FOUND, "Not found");
            }


            ArrayList<String> donaters = new ArrayList<String>();
            ArrayList<Long> summ = new ArrayList<>();

            for (int i = 1; i <= donation.getAmountOfPayers(); i++) {
                String wallet = donating.moneysenders(Id,BigInteger.valueOf(i)).send();
                donaters.add(wallet);
                BigInteger money = donating.donates(Id,wallet).send();
                BigInteger div = BigInteger.TEN.pow(18);
                money = money.divide(div);
                summ.add(money.longValue());
            }

            ArrayList<Map> parts = new ArrayList<>();
            for (int i = 0; i < donaters.size(); i++) {
                parts.add(Map.of("wallet",donaters.get(i),"amount", summ.get(i)));
            }


            BigInteger needId = BigInteger.valueOf(paymentId);

            long id = donation.getId();
            int amountOfPayers = donation.getAmountOfPayers();
            String title = donation.getTitle();
            String description = donation.getDescription();
            long curBalance = donation.getCurrentBalance();
            long needToCollect = donation.getNeedToCollect();
            String walletAuthor = donation.getWalletAuthor();
            String walletReceiver = donation.getWalletReceiver();
            Date startingTime = donation.getStartingTime();
            Date endingTime = donation.getEndingTime();
            boolean isActive = donation.getIsActive();

            HashMap<String,Object> map = new HashMap<>();
            map.putAll(Map.of("id",id,"title",title,"description",description,"amountNeed", needToCollect, "amountGot",curBalance,
                    "parts",parts,"authorWallet",walletAuthor,"receiverWallet",walletReceiver,"begin",startingTime.getTime()/1000,
                    "end",endingTime.getTime()/1000));
            map.put("isActive",isActive);
            map.put("amountOfPayers", amountOfPayers);

            return map;
        }

        @PostMapping
        @Secured("ROLE_USER")
        public Object createNewPayment (@RequestBody Map<String,Object> payment) throws Exception{
            final TransactionManager manager = transactionService.get(TransactionService.Type.WRITE);

            final Donating donating = Donating.load(contractAddres, web3j, manager, GAS_PRICE, GAS_LIMIT);

            final String title,description,walletReceiver;
            final BigInteger amountNeed,secondsToVote;
            try {
                title = (String) payment.get("title");
                description = (String) payment.get("description");
                amountNeed = BigInteger.valueOf((int) payment.get("amountNeed"));  //FIXME : IS IT OKAY???
                walletReceiver = (String) payment.get("receiverWallet");


                final Date dateBegin = DateFormatter.stringToData((String) payment.get("begin"));
                final Date dateEnd = DateFormatter.stringToData((String) payment.get("end"));

                secondsToVote = BigInteger.valueOf(dateEnd.getTime() / 1000 - dateBegin.getTime() / 1000);
            } catch (Exception e) {
                throw INCORRECT_DATA;
            }

            final BigInteger id = writeTransaction(() -> {
                final TransactionReceipt transaction = donating.createDonation(title,description,
                        amountNeed,walletReceiver,secondsToVote).send();
                testTransaction(transaction);
                return donating.getCreationEvents(transaction).get(0).donationId;
            });

            return Map.of("id",id);
        }

        @PutMapping(path = "/{paymentId}")
        @Secured("ROLE_USER")
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

            BigInteger weiValue = BigInteger.valueOf(json.get("amount"));

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
