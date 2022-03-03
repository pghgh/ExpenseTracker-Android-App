package at.ac.univie.t0306.expensetracker.iterators;

import java.util.Comparator;

import at.ac.univie.t0306.expensetracker.database.data.Transaction;

/**
 * Interface for the Container needed for the iterator.
 */
public interface IContainer {

    /**
     * Returns the Iterator for this Container
     *
     * @return A IIterator Object
     */
    IIterator getIterator();

    /**
     * Method to sort the data in this container.
     */
    void sortTransactionsList();

}
