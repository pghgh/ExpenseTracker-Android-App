package at.ac.univie.t0306.expensetracker.database.data;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;


/**
 * The AccountAndAlert data class which represents the 1:1 relationship between an account and its transactions
 */
public class AccountWithTransactions {

    @Embedded
    public Account account;
    @Relation(
            parentColumn = "Id",
            entityColumn = "accountId"
    )
    public List<Transaction> transactions;
}


