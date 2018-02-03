package ru.comptech.bc.server.model;

import java.math.BigInteger;
import java.text.DateFormat;
import java.util.Date;

public class Donation {

    private final long id;

    private final int amountOfPayers;

    private final String title;
    private final String description;

    private final BigInteger currentBalance;
    private final BigInteger needToCollect;

    private final String walletAuthor;
    private final String walletReceiver;

    private final Date startingTime;
    private final Date endingTime;

    private boolean isActive;

    public Donation(long id,int amountOfPayers,String title,String description,
                    BigInteger currentBalance,BigInteger needToCollect,String walletAuthor,
                    String walletReceiver,Date startingTime, Date endingTime,boolean isActive) {
        this.id = id;
        this.amountOfPayers = amountOfPayers;
        this.title = title;
        this.description = description;
        this.currentBalance = currentBalance;
        this.needToCollect = needToCollect;
        this.walletAuthor = walletAuthor;
        this.walletReceiver = walletReceiver;
        this.startingTime = startingTime;
        this.endingTime = endingTime;
        this.isActive = isActive;


    }


    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getAmountOfPayers() {
        return amountOfPayers;
    }

    public Date getEndingTime() {
        return endingTime;
    }

    public Date getStartingTime() {
        return startingTime;
    }

    public BigInteger getCurrentBalance() {
        return currentBalance;
    }

    public BigInteger getNeedToCollect() {
        return needToCollect;
    }

    public String getDescription() {
        return description;
    }

    public String getWalletAuthor() {
        return walletAuthor;
    }

    public String getWalletReceiver() {
        return walletReceiver;
    }

    public boolean getIsActive() {
        return isActive;
    }

}
