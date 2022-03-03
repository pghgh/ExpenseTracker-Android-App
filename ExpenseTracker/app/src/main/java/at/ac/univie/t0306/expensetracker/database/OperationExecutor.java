package at.ac.univie.t0306.expensetracker.database;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * provides an ExecutorService to run the CRUD operations of the database in parallel
 */
public class OperationExecutor {
    private static final int NUM_THREADS = 1;
    private static final ExecutorService operationExecutor = Executors.newFixedThreadPool(NUM_THREADS);

    public static ExecutorService getOperationExecutor() {
        return operationExecutor;
    }
}
