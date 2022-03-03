package at.ac.univie.t0306.expensetracker.database;

import at.ac.univie.t0306.expensetracker.database.data.Account;
import at.ac.univie.t0306.expensetracker.database.data.Alert;
import at.ac.univie.t0306.expensetracker.database.data.Category;
import at.ac.univie.t0306.expensetracker.database.data.Transaction;

/**
 * it provides methods to check the data before it is going to be inserted to the database
 */
public class DataValidator {


    /**
     * checks that all alert's values are valid
     *
     * @param account
     * @return
     */
    public boolean checkAccount(Account account) {

        return !account.getAccountType().equals("") && account.getCreationData() != null && !account.getName().equals("");
    }


    /**
     * checks the all category's values are valid
     *
     * @param category
     * @return
     */
    public boolean checkCategory(Category category) {
        return category.getName() != null && !category.getName().equals("");
    }


    /**
     * checks the all transaction's values are valid
     *
     * @param transaction
     * @return
     */
    public boolean checkTransaction(Transaction transaction) {
        return transaction.getAmount() != 0.0 && transaction.getCreationData() != null && transaction.getCategory() != "";
    }

    /**
     * checks the the new transaction is really edited and not the same as the old one
     *
     * @param old_transaction
     * @param new_transaction
     * @return
     */
    public boolean checkUpdateTransaction(Transaction old_transaction, Transaction new_transaction) {

        return checkTransaction(old_transaction) && checkTransaction(new_transaction) && old_transaction.getId() == new_transaction.getId() &&
                (!old_transaction.getCategory().equals(new_transaction.getCategory()) ||
                        !old_transaction.getCreationData().equals(new_transaction.getCreationData()) ||
                        old_transaction.getAmount() != new_transaction.getAmount());
    }

    /**
     * checks the the new alert is really edited and not the same as the old one
     *
     * @param old_alert
     * @param new_alert
     * @return
     */
    public boolean checkUpdateAlert(Alert old_alert, Alert new_alert) {
        return old_alert.getId() == new_alert.getId() && old_alert.getAssociatedAccountId() == new_alert.getAssociatedAccountId() && old_alert.getThreshold() != new_alert.getThreshold();
    }

    /**
     * checks the the new category is really edited and not the same as the old one
     *
     * @param old_category
     * @param new_category
     * @return
     */
    public boolean checkUpdateCategory(Category old_category, Category new_category) {
        return checkCategory(old_category) && checkCategory(new_category) && old_category.getId() == new_category.getId() &&
                !old_category.getName().equals(new_category.getName());
    }

    /**
     * checks the the new account is really edited and not the same as the old one
     *
     * @param old_account
     * @param new_account
     * @return
     */
    public boolean checkUpdateAccount(Account old_account, Account new_account) {

        return checkAccount(old_account) && checkAccount(new_account) &&
                old_account.getId() == new_account.getId() && (
                !old_account.getName().equals(new_account.getName()) ||
                        !old_account.getAccountType().equals(new_account.getAccountType()) ||
                        !old_account.getCreationData().equals(new_account.getCreationData()) ||
                        old_account.getBalance() != new_account.getBalance() ||
                        !old_account.getDescription().equals(new_account.getDescription()));
    }


}
