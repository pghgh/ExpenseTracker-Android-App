package at.ac.univie.t0306.expensetracker.ui.observer;

/**
 * interface for observer pattern
 */
public interface CacheObserver {
    /**
     * will be called by the subject class when changes happen
     */
    void update();
}