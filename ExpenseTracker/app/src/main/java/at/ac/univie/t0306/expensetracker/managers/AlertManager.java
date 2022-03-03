package at.ac.univie.t0306.expensetracker.managers;

import android.util.Log;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import at.ac.univie.t0306.expensetracker.database.data.*;
import at.ac.univie.t0306.expensetracker.database.data_facade.DataRepo;

/**
 * manages the Alerts
 * it will be used to add/remove check threshold
 */
public class AlertManager {


    private DataRepo dataRepo;

    /**
     * constructor
     *
     * @param dataRepo datarepo to access the Database
     */
    public AlertManager(DataRepo dataRepo) {
        this.dataRepo = dataRepo;
    }

    /**
     * set new alert for the account with account id
     *
     * @param threshold alert threshold
     * @param accountId account id
     */
    public void setNewAlert(double threshold, int accountId) {
        Alert alert = new Alert(threshold, accountId);
        dataRepo.addAlert(alert);
    }


    /**
     * updates the alert!
     *
     * @param old_alert
     * @param new_alert
     */
    public void updateAlert(Alert old_alert, Alert new_alert) {
        dataRepo.updateAlert(old_alert, new_alert);
    }


    /**
     * delete the alert for account with account id
     *
     * @param accountId account id
     */
    public void deleteAlert(int accountId) {
        Alert alert = dataRepo.getAlertForAccount(accountId);
        dataRepo.deleteAlert(alert);
    }

    /**
     * returns true if threshold is exceeded
     *
     * @param accountId
     * @return
     */
    public boolean checkBalanceLessThanThreshold(int accountId) {

        Account account = dataRepo.getAccount(accountId);

        return account.getBalance() < dataRepo.getAlertForAccount(accountId).getThreshold();
    }

    /**
     * checks if the threshold will be exceeded after doing an expense with amount
     *
     * @param accountId     account id
     * @param expenseAmount amount of the expense
     * @return true when the threshold will be exceeded if the amount of the expense will be deducted else false
     */
    public boolean checkThresholdExceededWhenExpense(int accountId, double expenseAmount) {
        if (!accountHasAlert(accountId)) {
            Log.i("alert", "no alert found");
            return false;
        }
        Log.i("alert", "alert found");
        Account account = dataRepo.getAccount(accountId);
        Log.i("alert", account.getBalance() + " " + " " + expenseAmount + "  " + getThreshold(accountId));
        return (account.getBalance() - expenseAmount) < getThreshold(accountId);
    }

    /**
     * @param accountId account id
     * @return true when account has Alert else false
     */
    public boolean accountHasAlert(int accountId) {
        Alert alert = dataRepo.getAlertForAccount(accountId);
        return alert != null;
    }

    /**
     * return the threshold of the alert for the Account with acccountId
     *
     * @param accountId ID of the Account
     * @return threshold of the Alert
     */
    public double getThreshold(int accountId) {
        return dataRepo.getAlertForAccount(accountId).getThreshold();
    }

    /**
     * remove the alert for the Account with accountId
     *
     * @param accountId ID of the Account
     * @return true when the account has an Alert els false
     */
    public boolean removeAlertOfAccount(int accountId) {
        if (accountHasAlert(accountId)) {
            dataRepo.deleteAlert(dataRepo.getAlertForAccount(accountId));
            return true;
        }
        return false;
    }

}
