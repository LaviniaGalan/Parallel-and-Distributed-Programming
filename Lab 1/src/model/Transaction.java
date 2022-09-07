package model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Transaction {

    private BankAccount source;
    private BankAccount destination;

    private int amount;

    private static AtomicInteger serialNumber = new AtomicInteger(0);
    private int transactionSerialNumber;

    public Transaction(BankAccount source, BankAccount destination, int amount) {
        this.source = source;
        this.destination = destination;
        this.amount = amount;

        this.transactionSerialNumber = serialNumber.incrementAndGet();
    }

    public int getAmount() {
        return amount;
    }

    public BankAccount getSource() {
        return source;
    }

    public BankAccount getDestination() {
        return destination;
    }

    public int getTransactionSerialNumber() {
        return transactionSerialNumber;
    }

//    public void transfer() {
//        this.source.sendMoney(this);
//        this.destination.receiveMoney(this);
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return transactionSerialNumber == that.transactionSerialNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, destination, amount, transactionSerialNumber);
    }
}
