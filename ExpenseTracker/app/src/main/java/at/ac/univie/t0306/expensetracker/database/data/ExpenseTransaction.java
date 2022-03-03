package at.ac.univie.t0306.expensetracker.database.data;

import androidx.room.Entity;

import java.time.OffsetDateTime;

/**
 * The ExpenseTransaction data class which represents an Table in the SQLLite database!!
 */
@Entity
public class ExpenseTransaction extends Transaction {

    public ExpenseTransaction(int accountId, String category, String description, double amount, OffsetDateTime creationData) {
        super(accountId, category, description, amount, creationData);
    }
}
