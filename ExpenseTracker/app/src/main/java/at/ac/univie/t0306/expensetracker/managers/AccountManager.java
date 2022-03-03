package at.ac.univie.t0306.expensetracker.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import at.ac.univie.t0306.expensetracker.database.AppDatabase;
import at.ac.univie.t0306.expensetracker.database.data.Account;
import at.ac.univie.t0306.expensetracker.database.data_facade.DataRepo;

/**
 * class to manage the accounts
 */
public class AccountManager {

    private DataRepo dataRepo;

    /**
     * @param dataRepo
     */
    public AccountManager(DataRepo dataRepo) {
        this.dataRepo = dataRepo;
    }

    /**
     * to delete an account
     *
     * @param account
     */
    public void deleteAccount(Account account) {
        dataRepo.deleteAccount(account);
    }

    /**
     * to add a new account
     *
     * @param account
     */
    public void addAccount(Account account) {
        dataRepo.addAccounts(account);

    }

    /**
     * to get an account based on the id
     *
     * @param id
     * @return
     */
    public Account getAccount(int id) {
        return dataRepo.getAccount(id);
    }

    /**
     * to update the account
     *
     * @param old_account
     * @param new_account
     */
    public void updateAccount(Account old_account, Account new_account) {
        dataRepo.updateAccount(old_account, new_account);
    }


}
