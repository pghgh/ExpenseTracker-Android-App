package at.ac.univie.t0306.expensetracker.database.data;

import java.time.OffsetDateTime;

/**
 * Class to create a new Transaction if not differed between Income and Expense
 */
public class TransactionCreator {

    /**
     * Empty Constructor for creator class
     */
    public TransactionCreator() {
    }

    /**
     * Method to create a new Transaction Object
     *
     * @param accountId
     * @param category
     * @param description
     * @param amount
     * @param creationData
     * @return
     */
    public Transaction createTransaction(int accountId, String category, String description, double amount, OffsetDateTime creationData) {
        return new Transaction(accountId, category, description, amount, creationData);
    }

}
