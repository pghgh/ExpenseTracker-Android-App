package at.ac.univie.t0306.expensetracker.ui.observer;

import java.util.ArrayList;
import java.util.List;

import at.ac.univie.t0306.expensetracker.database.data.Account;
import at.ac.univie.t0306.expensetracker.database.data.ExpenseTransaction;
import at.ac.univie.t0306.expensetracker.database.data.IncomeTransaction;
import at.ac.univie.t0306.expensetracker.database.data_facade.DataRepo;
import at.ac.univie.t0306.expensetracker.database.data_facade.ITransactionsReadOperations;

public class DataCacheProxy extends Subject implements ITransactionsReadOperations {

    private DataRepo dataRepo;
    private static DataCacheProxy dataCacheProxy;
    private List<Account> accountList;

    List<IncomeTransaction> incomes;
    List<ExpenseTransaction> expenses;

    private DataCacheProxy() {
        accountList = new ArrayList<>();
        incomes = new ArrayList<>();
        expenses = new ArrayList<>();
    }

    public synchronized static DataCacheProxy getInstance() {
        if (dataCacheProxy == null)
            dataCacheProxy = new DataCacheProxy();
        return dataCacheProxy;
    }

    public synchronized List<Account> getAccountList() {
        return accountList;
    }

    public synchronized void setDataRepo(DataRepo dataRepo) {
        if (this.dataRepo == null) {
            this.dataRepo = dataRepo;
            listenOnUpdates();
        }
    }

    private synchronized void listenOnUpdates() {
        dataRepo.getAccountsLiveData().observeForever(accounts -> {
            accountList = accounts;
            notifyObservers();
        });

        dataRepo.getExpenseTransactionsLiveData().observeForever(expenseTransactions -> expenses = expenseTransactions);
        dataRepo.getIncomeTransactionsLiveData().observeForever(incomeTransactions -> incomes = incomeTransactions);
    }

    @Override
    public List<IncomeTransaction> getIncomeTransactions() {
        return this.incomes;
    }

    @Override
    public List<ExpenseTransaction> getExpenseTransactions() {
        return this.expenses;
    }

}