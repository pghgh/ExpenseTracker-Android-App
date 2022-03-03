package at.ac.univie.t0306.expensetracker.managers;


import android.view.View;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import at.ac.univie.t0306.expensetracker.database.data.Transaction;
import at.ac.univie.t0306.expensetracker.ui.reports.graphs.LineGraph;
import at.ac.univie.t0306.expensetracker.ui.reports.graphs.BarGraph;
import at.ac.univie.t0306.expensetracker.database.data.IncomeTransaction;
import at.ac.univie.t0306.expensetracker.database.data.ExpenseTransaction;

/*
This class contains tests which verify the correctness of the methods which compute the datasets based on different criteria
*/
public class ReportsLogicTest {

    private LineGraph lineGraph;
    private BarGraph barGraph;
    private View root;

    @Before
    public void setUp() {
        root = Mockito.mock(View.class);
        lineGraph = new LineGraph(root);
        barGraph = new BarGraph(root);
    }


    @Test
    public void datesMapTest() {
        OffsetDateTime offsetDateTime = OffsetDateTime.now();
        offsetDateTime = OffsetDateTime.of(offsetDateTime.getYear(), offsetDateTime.getMonthValue(), offsetDateTime.getDayOfMonth(), 0, 0, 0, 0, ZoneOffset.UTC);

        // we create two transactions which have the same creation date
        IncomeTransaction incomeTransaction = new IncomeTransaction(1, "Voucher", "description", 50.0, offsetDateTime);
        ExpenseTransaction expenseTransaction = new ExpenseTransaction(1, "Rent", "description", 300.0, offsetDateTime);

        // we add the transactions to a list
        List<Transaction> listOfTransactions = new ArrayList<>();
        listOfTransactions.add(incomeTransaction);
        listOfTransactions.add(expenseTransaction);

        /*
        the "obtainDataset" method of LineGraph will create a map of type Map<OffsetDateTime, Double>
        keys = different dates
        values = the amount of money earned/spent on each date
        */
        lineGraph.obtainDataset(listOfTransactions);

        /*
        the dates map should contain one entry:
        key = the creation date shared by the two transactions created above
        value = the amount of the income transaction - the amount of the expense transaction
        */
        Map<OffsetDateTime, Double> datesMap = lineGraph.getDatesMap();
        double amountForTheSpecificDate = incomeTransaction.getAmount() - expenseTransaction.getAmount();
        Assert.assertEquals(datesMap.get(offsetDateTime), amountForTheSpecificDate, 0.);

    }

    @Test
    public void categoriesMapTest() {
        OffsetDateTime offsetDateTime = OffsetDateTime.now();
        offsetDateTime = OffsetDateTime.of(offsetDateTime.getYear(), offsetDateTime.getMonthValue(), offsetDateTime.getDayOfMonth(), 0, 0, 0, 0, ZoneOffset.UTC);

        // we create two transactions which have the same category
        // this is an example of a category which is suitable for both incomes (e.g. scholarship) and expenses (e.g. tuition fee)
        String categoryName = "Education";
        IncomeTransaction incomeTransaction = new IncomeTransaction(1, categoryName, "description", 800.0, offsetDateTime);
        ExpenseTransaction expenseTransaction = new ExpenseTransaction(1, categoryName, "description", 20.0, offsetDateTime);

        // we add the transactions to a list
        List<Transaction> listOfTransactions = new ArrayList<>();
        listOfTransactions.add(incomeTransaction);
        listOfTransactions.add(expenseTransaction);

        /*
        the "obtainDataset" method of BarGraph will create a map of type Map<String, Double>
        keys = the names of the categories of the transactions from the list
        values = the amount of money earned/spent on each category
        */
        barGraph.obtainDataset(listOfTransactions);

        /*
        the categories map should contain one entry:
        key = the category shared by the two transactions created above
        value = the amount of the income transaction - the amount of the expense transaction
        */
        Map<String, Double> categoriesMap = barGraph.getCategoriesMap();
        double amountForTheSpecificCategory = incomeTransaction.getAmount() - expenseTransaction.getAmount();
        Assert.assertEquals(categoriesMap.get(categoryName), amountForTheSpecificCategory, 0.);

    }


}
