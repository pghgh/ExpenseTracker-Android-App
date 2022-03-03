package at.ac.univie.t0306.expensetracker.iterators;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import at.ac.univie.t0306.expensetracker.database.data.Transaction;

/**
 * {@inheritDoc}
 */
public class TransactionCategoryContainer implements IContainer {

    private final List<Transaction> transactions;

    public TransactionCategoryContainer(List<Transaction> transactions) {
        this.transactions = new ArrayList<>(transactions);
        sortTransactionsList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IIterator getIterator() {
        return new TransactionCategoryIterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sortTransactionsList() {
        transactions.sort(Comparator.comparing(Transaction::getCategory));
    }


    /**
     * {@inheritDoc}
     */
    public class TransactionCategoryIterator implements IIterator {

        int index;

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean hasNext() {
            return index < transactions.size();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Transaction next() {
            if (this.hasNext()) {
                return transactions.get(index++);
            }
            return null;
        }

    }

}
