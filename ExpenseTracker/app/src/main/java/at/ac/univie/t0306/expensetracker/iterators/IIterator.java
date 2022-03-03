package at.ac.univie.t0306.expensetracker.iterators;

import at.ac.univie.t0306.expensetracker.database.data.Transaction;

/**
 * Iterator class for Transactions
 */
public interface IIterator {

    /**
     * Boolean to differ if there are other objects to be iterated
     *
     * @return Boolean indicates other availability of objects.
     */
    boolean hasNext();

    /**
     * Gives the next Objext, if no next object then it returns null
     *
     * @return A transaction Object
     */
    Transaction next();

}
