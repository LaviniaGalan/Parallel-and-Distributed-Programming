package model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Bank {

    private static Random rand = new Random();
    private Logger logger = Logger.getLogger("Log");

    private List<BankAccount> accounts = new ArrayList<>();

    private static final int NUMBER_OF_ACCOUNTS = 500;
    private static final int NUMBER_OF_TRANSACTIONS = 10000;
    private static final int NUMBER_OF_THREADS = 16;

    private ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    private static ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    private ReadWriteLock consistencyLock = new ReentrantReadWriteLock();


    public void run() throws IOException, InterruptedException {

        this.logSetUp();

        this.createAccounts();

        this.simulateTransactions();
        this.scheduleConsistencyCheck();
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);
        scheduledExecutorService.shutdown();

        runConsistencyCheck();
    }


    public void logSetUp() throws IOException{
        FileHandler fileHandler = new FileHandler("E:\\CS\\An 3\\PPD\\Lab 1\\log\\output.txt");
        fileHandler.setFormatter(new SimpleFormatter());
        logger.setUseParentHandlers(false);
        logger.addHandler(fileHandler);
    }

    public void createAccounts(){
        for(int i = 1; i <= NUMBER_OF_ACCOUNTS; i++) {
            int amount = rand.nextInt(1000) + 500;
            BankAccount account = new BankAccount(i, amount);
            this.accounts.add(account);
        }
    }


    public Transaction generateTransaction(){
        int sourcePosition = rand.nextInt(NUMBER_OF_ACCOUNTS);

        while(accounts.get(sourcePosition).getCurrentAmount() == 0) {
            sourcePosition = rand.nextInt(NUMBER_OF_ACCOUNTS);
        }
        BankAccount source = accounts.get(sourcePosition);

        int destinationPosition = rand.nextInt(NUMBER_OF_ACCOUNTS);
        while(source.getAccountId() == accounts.get(destinationPosition).getAccountId()) {
            destinationPosition = rand.nextInt(NUMBER_OF_ACCOUNTS);
        }

        BankAccount destination = accounts.get(destinationPosition);

        int amount = rand.nextInt(source.getCurrentAmount());

        return new Transaction(source, destination, amount);
    }

    public void simulateTransactions(){

        List<Callable<Object>> tasks = new ArrayList<>();

        for(int i = 0; i < NUMBER_OF_TRANSACTIONS; i++) {

            Callable<Object> task = Executors.callable(() -> {

                Transaction transaction = generateTransaction();
                consistencyLock.readLock().lock();

                transaction.getSource().makeTransfer(transaction);
                logTransaction(transaction);
                consistencyLock.readLock().unlock();


            });
            tasks.add(task);
        }

        tasks.forEach((task) -> executorService.submit(task));
    }

    public void logTransaction(Transaction t){

        String message = "";
        message += "Transaction " + t.getTransactionSerialNumber() + ": ";
        message += "Source: " + t.getSource();
        message += "Destination: " + t.getDestination();
        logger.info(message);
    }


    public boolean consistencyCheck() {
        for(int i = 0; i < NUMBER_OF_ACCOUNTS; i++) {
            BankAccount account = accounts.get(i);

            int loggedAmount = 0;
            for(Transaction transaction : account.getLoggedTransactions()) {

                if(transaction.getSource().getAccountId() == account.getAccountId()){
                    loggedAmount = loggedAmount - transaction.getAmount();

                    if(! transaction.getDestination().getLoggedTransactions().contains(transaction)){
                        return false;
                    }
                }
                else if (transaction.getDestination().getAccountId() == account.getAccountId()){
                    loggedAmount = loggedAmount + transaction.getAmount();

                    if(! transaction.getSource().getLoggedTransactions().contains(transaction)){
                        return false;
                    }
                }
                else {
                    return false;
                }

            }
            if (account.getInitialAmount() + loggedAmount != account.getCurrentAmount()) {
                return false;
            }
        }
        return true;
    }

    public void runConsistencyCheck() {

        consistencyLock.writeLock().lock();
        logger.info("Consistency check started...");
        boolean check = consistencyCheck();
        //System.out.println(check);
        logger.info("Consistency check performed: validity = " + check);
        if(check == false) {
            throw new RuntimeException("Inconsistency found!");
        }
        consistencyLock.writeLock().unlock();
    }

    public void scheduleConsistencyCheck(){

        scheduledExecutorService.scheduleWithFixedDelay(
                this::runConsistencyCheck, 0, 100, TimeUnit.MILLISECONDS
                );
    }

}
