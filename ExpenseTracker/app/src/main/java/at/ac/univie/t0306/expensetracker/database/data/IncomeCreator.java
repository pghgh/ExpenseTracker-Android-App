package at.ac.univie.t0306.expensetracker.database.data;

import java.time.OffsetDateTime;

/**
 * Class to create a new Income
 */
public class IncomeCreator extends TransactionCreator {

    /**
     * {@inheritDoc}
     *
     * @param accountId
     * @param category
     * @param description
     * @param amount
     * @param creationData
     * @return
     */
    @Override
    public Transaction createTransaction(int accountId, String category, String description, double amount, OffsetDateTime creationData) {
        return new IncomeTransaction(accountId, category, description, amount, creationData);
    }

}
