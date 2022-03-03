package at.ac.univie.t0306.expensetracker.filterStrategyTest;

import org.junit.Before;
import org.junit.Test;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import at.ac.univie.t0306.expensetracker.database.data.ExpenseTransaction;
import at.ac.univie.t0306.expensetracker.database.data.Transaction;
import at.ac.univie.t0306.expensetracker.ui.reports.filterStrategy.DailyFilterStrategy;
import at.ac.univie.t0306.expensetracker.ui.reports.filterStrategy.FilterStrategy;
import at.ac.univie.t0306.expensetracker.ui.reports.filterStrategy.MonthlyFilterStrategy;
import at.ac.univie.t0306.expensetracker.ui.reports.filterStrategy.WeeklyFilterStrategy;

/**
 * class to test the Strategy Pattern
 */
public class FilterStrategyTest {


    private FilterStrategy filterStrategy;
    private List<Transaction> transactions;

    private Transaction todayTransaction = new ExpenseTransaction(1, "Food", "Some desc", 100.0, OffsetDateTime.now());
    private Transaction weakTransaction = new ExpenseTransaction(2, "Food", "Some desc", 100.0, OffsetDateTime.now().minusDays(2));
    private Transaction monthTransaction = new ExpenseTransaction(3, "Food", "Some desc", 100.0, OffsetDateTime.now().minusDays(20));
    private Transaction month2Transaction = new ExpenseTransaction(4, "Food", "Some desc", 100.0, OffsetDateTime.now().minusDays(60));

    @Before
    public void setUp(){
        transactions=new ArrayList<>(Arrays.asList(todayTransaction, weakTransaction, monthTransaction,month2Transaction));
    }

    /**
     * test if the DailyTestStrategy will remove all transactions older than one day
     */
    @Test
    public void testDailyFilter(){
        filterStrategy=new DailyFilterStrategy();
        filterStrategy.filterTransactions(transactions);
        assert (transactions.contains(todayTransaction)&&transactions.size()==1);
    }
    /**
     * test if the WeeklyTestStrategy will remove all transactions older than one week
     */
    @Test
    public void testWeeklyFilter(){
        filterStrategy=new WeeklyFilterStrategy();
        filterStrategy.filterTransactions(transactions);
        assert (transactions.contains(todayTransaction)&&transactions.contains(weakTransaction)&&transactions.size()==2);
    }
    /**
     * test if the MonthlyTestStrategy will remove all transactions older than one Month
     */
    @Test
    public void testMonthlyFilter(){
        filterStrategy=new MonthlyFilterStrategy();
        filterStrategy.filterTransactions(transactions);
        assert (transactions.contains(todayTransaction)&&transactions.contains(weakTransaction)&&transactions.contains(monthTransaction)&&!transactions.contains(month2Transaction));
    }



}