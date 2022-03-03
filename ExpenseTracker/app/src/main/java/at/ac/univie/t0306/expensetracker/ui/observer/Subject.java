package at.ac.univie.t0306.expensetracker.ui.observer;

import java.util.ArrayList;
import java.util.List;

public class Subject {

    protected List<CacheObserver> observerList;

    public Subject() {
        observerList = new ArrayList<>();
    }

    public void attachObserver(CacheObserver observer) {
        observerList.add(observer);
        observer.update();
    }

    public void detachObserver(CacheObserver observer) {
        observerList.remove(observer);
    }

    protected void notifyObservers() {
        for (CacheObserver observer : observerList
        ) {
            observer.update();
        }
    }
}