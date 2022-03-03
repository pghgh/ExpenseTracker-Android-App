package at.ac.univie.t0306.expensetracker.database.data;

import androidx.room.Embedded;
import androidx.room.Relation;


/**
 * The AccountAndAlert data class which represents the 1:1 relationship between an account and an alert
 */
public class AccountAndAlert {
    @Embedded
    private Account account;
    @Relation(
            parentColumn = "id",
            entityColumn = "associatedAccountId"
    )

    private Alert alert;

    public Alert getAlert() {
        return alert;
    }

    public void setAlert(Alert alert) {
        this.alert = alert;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
