package at.ac.univie.t0306.expensetracker.database.data;

import androidx.room.Entity;

import java.time.OffsetDateTime;

/**
 * The IncomeTransaction data class which represents an Table in the SQLLite database!!
 */
@Entity
public class IncomeTransaction extends Transaction {

    public IncomeTransaction(int accountId, String category, String description, double amount, OffsetDateTime creationData) {
        super(accountId, category, description, amount, creationData);
    }

}
