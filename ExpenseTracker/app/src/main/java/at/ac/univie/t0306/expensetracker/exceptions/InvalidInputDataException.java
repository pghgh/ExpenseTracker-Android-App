package at.ac.univie.t0306.expensetracker.exceptions;

/**
 * An Exception indicates the invalid input from the user
 */
public class InvalidInputDataException extends Exception {
    public InvalidInputDataException(String message) {
        super(message);
    }
}
