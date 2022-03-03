package at.ac.univie.t0306.expensetracker.ui.reports.filterStrategy;

import java.util.List;

import at.ac.univie.t0306.expensetracker.database.data.Transaction;

/**
 * Interface for implementing the Strategy pattern
 * includes only one methode:filterTransactions which filters a sortedset of transactions
 */
public interface FilterStrategy {
    /**
     * filters the list transactions
     * @param transactions list of transactions to be filtered
     */
    void filterTransactions(List<Transaction> transactions);
}
