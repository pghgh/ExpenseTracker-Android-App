package at.ac.univie.t0306.expensetracker.database.data_facade;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;

import at.ac.univie.t0306.expensetracker.database.daos.AccountDao;
import at.ac.univie.t0306.expensetracker.database.daos.AlertDao;
import at.ac.univie.t0306.expensetracker.database.AppDatabase;
import at.ac.univie.t0306.expensetracker.database.daos.CategoryDao;
import at.ac.univie.t0306.expensetracker.database.DataValidator;
import at.ac.univie.t0306.expensetracker.database.DatabaseProvider;
import at.ac.univie.t0306.expensetracker.database.daos.ExpenseDao;
import at.ac.univie.t0306.expensetracker.database.daos.IncomeDao;
import at.ac.univie.t0306.expensetracker.database.OperationExecutor;
import at.ac.univie.t0306.expensetracker.database.data.Account;
import at.ac.univie.t0306.expensetracker.database.data.AccountAndAlert;
import at.ac.univie.t0306.expensetracker.database.data.Alert;
import at.ac.univie.t0306.expensetracker.database.data.Category;
import at.ac.univie.t0306.expensetracker.database.data.ExpenseTransaction;
import at.ac.univie.t0306.expensetracker.database.data.IncomeTransaction;

/**
 * represents a facade over the database functionalities
 * it provides the all CRUD operations for all  dataclasses
 */
public class DataRepo implements ITransactionsReadOperations {

    private AlertDao alertDao;
    private AccountDao accountDao;
    private CategoryDao categoryDao;
    private IncomeDao incomeDao;
    private ExpenseDao expenseDao;
    private ExecutorService operationsExecutor;
    private DataValidator validator;


    public DataRepo(Application application) {
        AppDatabase db = DatabaseProvider.getDB(application);
        alertDao = db.alertDao();
        accountDao = db.accountDao();
        categoryDao = db.categoryDao();
        incomeDao = db.incomeDao();
        expenseDao = db.expenseDao();
        operationsExecutor = OperationExecutor.getOperationExecutor();
        validator = new DataValidator();
    }

    // add operations

    /**
     * add new income Transaction
     *
     * @param transaction
     */
    public void addIncomeTransaction(IncomeTransaction transaction) {
        if (validator.checkTransaction(transaction))
            operationsExecutor.execute(() -> incomeDao.insert(transaction));

    }

    /**
     * add new expense transaction
     *
     * @param transaction
     */
    public void addExpenseTransaction(ExpenseTransaction transaction) {
        if (validator.checkTransaction(transaction))
            operationsExecutor.execute(() -> expenseDao.insert(transaction));

    }

    /**
     * add new alert
     *
     * @param alert
     */
    public void addAlert(Alert alert) {
        operationsExecutor.execute(() -> alertDao.insert(alert));
    }

    /**
     * add new category
     *
     * @param category
     */
    public void addCategories(Category category) {
        if (validator.checkCategory(category))
            operationsExecutor.execute(() -> categoryDao.insert(category));
    }

    /**
     * add new account
     *
     * @param account
     */
    public void addAccounts(Account account) {
        if (validator.checkAccount(account))
            operationsExecutor.execute(() -> accountDao.insert(account));
    }

    // update operations

    /**
     * update income transaction
     *
     * @param old_transaction
     * @param new_transaction
     */
    public void updateIncomeTransaction(IncomeTransaction old_transaction, IncomeTransaction new_transaction) {
        if (validator.checkUpdateTransaction(old_transaction, new_transaction))
            operationsExecutor.execute(() -> incomeDao.update(new_transaction));

    }

    /**
     * update expense transaction
     *
     * @param old_transaction
     * @param new_transaction
     */
    public void updateExpenseTransaction(ExpenseTransaction old_transaction, ExpenseTransaction new_transaction) {
        if (validator.checkUpdateTransaction(old_transaction, new_transaction))
            operationsExecutor.execute(() -> expenseDao.update(new_transaction));

    }

    /**
     * update alert
     *
     * @param old_alert
     * @param new_alert
     */
    public void updateAlert(Alert old_alert, Alert new_alert) {
        if (validator.checkUpdateAlert(old_alert, new_alert))
            operationsExecutor.execute(() -> alertDao.update(new_alert));

    }

    /**
     * update category
     *
     * @param old_category
     * @param new_category
     */
    public void updateCategory(Category old_category, Category new_category) {
        if (validator.checkUpdateCategory(old_category, new_category))
            operationsExecutor.execute(() -> categoryDao.update(new_category));
    }

    /**
     * update account
     *
     * @param old_account
     * @param new_account
     */
    public void updateAccount(Account old_account, Account new_account) {
        if (validator.checkUpdateAccount(old_account, new_account))
            operationsExecutor.execute(() -> accountDao.update(new_account));
    }
    // delete operations

    /**
     * delete income transaction
     *
     * @param transaction
     */
    public void deleteIncomeTransaction(IncomeTransaction transaction) {
        operationsExecutor.execute(() -> incomeDao.delete(transaction));
    }

    /**
     * delete expense transaction
     *
     * @param transaction
     */
    public void deleteExpenseTransaction(ExpenseTransaction transaction) {
        System.out.println("------------------");
        System.out.println(transaction);
        operationsExecutor.execute(() -> expenseDao.delete(transaction));
    }

    /**
     * delete alert
     *
     * @param alert
     */
    public void deleteAlert(Alert alert) {
        operationsExecutor.execute(() -> alertDao.delete(alert));
    }

    /**
     * delete category
     */
    public void deleteCategory(Category category) {
        operationsExecutor.execute(() -> categoryDao.delete(category));
    }

    /**
     * delete account
     *
     * @param account
     */
    public void deleteAccount(Account account) {
        operationsExecutor.execute(() -> accountDao.delete(account));
    }


    // query operations

    /**
     * return income transactions
     *
     * @return
     */
    public List<IncomeTransaction> getIncomeTransactions() {
        return incomeDao.getAll();
    }

    /**
     * return expense transactions
     *
     * @return
     */
    public List<ExpenseTransaction> getExpenseTransactions() {
        return expenseDao.getAll();
    }

    /**
     * return income transactions wrapped with LiveData
     *
     * @return
     */
    public LiveData<List<IncomeTransaction>> getIncomeTransactionsLiveData() {
        return incomeDao.getAllAsLiveData();
    }

    /**
     * return expense transactions wrapped with LiveData
     *
     * @return
     */
    public LiveData<List<ExpenseTransaction>> getExpenseTransactionsLiveData() {
        return expenseDao.getAllAsLiveData();
    }


    /**
     * return alerts
     *
     * @return
     */
    public List<Alert> getAlerts() {
        return alertDao.getAll();
    }


    public Alert getAlertForAccount(int accountId) {
        return alertDao.getAlertFoAccount(accountId);
    }

    /**
     * return alerts  wrapped with LiveData
     *
     * @return
     */

    public LiveData<List<Category>> getCategoriesLiveData() {
        return categoryDao.getAllAsLiveData();
    }

    /**
     * return accounts
     *
     * @return
     */
    public List<Account> getAccounts() {
        return accountDao.getAll();
    }

    /**
     * return accounts  wrapped with LiveData
     *
     * @return
     */
    public LiveData<List<Account>> getAccountsLiveData() {
        return accountDao.getAllAsLiveData();
    }

    /**
     * return AccountAndAlerts  wrapped with LiveData
     *
     * @return
     */
    public LiveData<List<AccountAndAlert>> getAccountAndAlerts() {
        return accountDao.getAccountAndAlert();
    }

    /**
     * return one account based on its id
     *
     * @param id
     * @return
     */
    public Account getAccount(int id) {
        return accountDao.getAccount(id);
    }


}
