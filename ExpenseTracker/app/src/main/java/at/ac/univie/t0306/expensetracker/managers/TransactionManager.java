package at.ac.univie.t0306.expensetracker.managers;

import android.database.*;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import at.ac.univie.t0306.expensetracker.database.data.*;
import at.ac.univie.t0306.expensetracker.database.data_facade.*;


public class TransactionManager {

    private DataRepo dataRepo;

    public TransactionManager(DataRepo dataRepo) {
        this.dataRepo = dataRepo;
    }

    /**
     * to add a new transaction
     *
     * @param transaction
     * @param affectedAccount
     */
    public void addTransaction(Transaction transaction, Account affectedAccount) {
        double newAmountForAccountUpdate = .0;
        if (transaction instanceof IncomeTransaction) {
            dataRepo.addIncomeTransaction((IncomeTransaction) transaction);
            newAmountForAccountUpdate = transaction.getAmount();
        } else {
            dataRepo.addExpenseTransaction((ExpenseTransaction) transaction);
            newAmountForAccountUpdate = -transaction.getAmount();
        }

        handleAccountBalanceUpdating(newAmountForAccountUpdate, affectedAccount);
    }

    /**
     * to update an transaction
     *
     * @param OldTransaction
     * @param updatedTransaction
     * @param affectedAccount
     */
    public void updateTransaction(Transaction OldTransaction, Transaction updatedTransaction, Account affectedAccount) {
        double newAmountForAccountUpdate = .0;
        if (updatedTransaction instanceof IncomeTransaction) {
            dataRepo.updateIncomeTransaction((IncomeTransaction) OldTransaction, (IncomeTransaction) updatedTransaction);
            newAmountForAccountUpdate = updatedTransaction.getAmount() - OldTransaction.getAmount();
        } else {
            dataRepo.updateExpenseTransaction((ExpenseTransaction) OldTransaction, (ExpenseTransaction) updatedTransaction);
            newAmountForAccountUpdate = OldTransaction.getAmount() - updatedTransaction.getAmount();
        }
        handleAccountBalanceUpdating(newAmountForAccountUpdate, affectedAccount);
    }

    /**
     * to update the balance of account of the affected account for this transaction
     *
     * @param newAmountForAccountUpdate
     * @param affectedAccount
     */
    private void handleAccountBalanceUpdating(double newAmountForAccountUpdate, Account affectedAccount) {
        Account updatedAccount;
        if (affectedAccount != null) {
            updatedAccount = new Account(affectedAccount.getName(), affectedAccount.getDescription(), (affectedAccount.getBalance() + newAmountForAccountUpdate), affectedAccount.getAccountType(), affectedAccount.getCreationData());
            updatedAccount.setId(affectedAccount.getId());
            dataRepo.updateAccount(affectedAccount, updatedAccount);
        } else {
            Log.d("Error", "ERROR UPDATE");
        }
    }

    /**
     * to delete a transaction
     *
     * @param transaction
     */
    public void deleteTransaction(Transaction transaction) {
        double newAmountForAccountUpdate = .0;
        List<Account> accountsList = dataRepo.getAccounts();
        Account affectedAccount = accountsList.stream().filter(a -> a.getId() == transaction.getAccountId()).findAny().orElse(null);

        if (transaction instanceof IncomeTransaction) {
            dataRepo.deleteIncomeTransaction((IncomeTransaction) transaction);
            newAmountForAccountUpdate = -transaction.getAmount();
        } else {
            dataRepo.deleteExpenseTransaction((ExpenseTransaction) transaction);
            newAmountForAccountUpdate = transaction.getAmount();
        }
        handleAccountBalanceUpdating(newAmountForAccountUpdate, affectedAccount);
    }


}
