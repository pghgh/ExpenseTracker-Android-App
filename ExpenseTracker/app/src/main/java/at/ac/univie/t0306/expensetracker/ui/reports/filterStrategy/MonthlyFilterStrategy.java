package at.ac.univie.t0306.expensetracker.ui.reports.filterStrategy;


import java.time.OffsetDateTime;
import java.util.List;

import at.ac.univie.t0306.expensetracker.database.data.Transaction;

/**
 * implements Interface FilterStrategy
 */
public class MonthlyFilterStrategy implements FilterStrategy {
    /**
     * empty constructor
     */
    public MonthlyFilterStrategy() {
    }

    /**
     * removes all transactions older than one month
     *
     * @param transactions the list to be filtered
     */
    @Override
    public void filterTransactions(List<Transaction> transactions) {
        transactions.removeIf(transaction -> transaction.getCreationData().compareTo(OffsetDateTime.now().minusMonths(1)) < 0);
    }
}
