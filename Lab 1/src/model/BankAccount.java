package model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BankAccount {

    private int accountId;

    private int initialAmount;
    private int currentAmount;

    private List<Transaction> loggedTransactions = new ArrayList<>();

    public Lock mutex = new ReentrantLock();

    public BankAccount(int accountId, int amount) {
        this.accountId = accountId;
        this.initialAmount = amount;
        this.currentAmount = amount;
    }


    public void makeTransfer(Transaction transaction) {

        if (transaction.getSource().getAccountId() < transaction.getDestination().getAccountId()) {
            this.mutex.lock();
            transaction.getDestination().mutex.lock();
        } else {
            transaction.getDestination().mutex.lock();
            this.mutex.lock();
        }

        this.currentAmount -= transaction.getAmount();
        transaction.getDestination().currentAmount += transaction.getAmount();
        this.loggedTransactions.add(transaction);
        transaction.getDestination().loggedTransactions.add(transaction);


        this.mutex.unlock();
        transaction.getDestination().mutex.unlock();

    }



    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getCurrentAmount() {
        return currentAmount;
    }

    public List<Transaction> getLoggedTransactions() {
        return this.loggedTransactions;
    }


    public int getInitialAmount() {
        return initialAmount;
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "accountId='" + accountId + '\'' +
                ", amount=" + currentAmount +
                '}';
    }
}
