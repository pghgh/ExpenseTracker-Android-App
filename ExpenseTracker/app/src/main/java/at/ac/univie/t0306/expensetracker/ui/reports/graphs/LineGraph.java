package at.ac.univie.t0306.expensetracker.ui.reports.graphs;

import android.view.View;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import at.ac.univie.t0306.expensetracker.database.data.ExpenseTransaction;
import at.ac.univie.t0306.expensetracker.database.data.IncomeTransaction;
import at.ac.univie.t0306.expensetracker.database.data.Transaction;

/**
 * Class which will plot data on a line graph
 * with the help of a specific adapter which facilitates the communication with a Third Party Visualization Library
 */
public class LineGraph implements GraphVisualization {


    private LineGraphAdapter lineGraphAdapter;
    private Map<OffsetDateTime, Double> datesMap;
    private View root;

    /**
     * Constructor which needs a view for setting up the graph
     *
     * @param root
     */
    public LineGraph(View root) {
        this.root = root;
    }

    /**
     * Getter method for the DatesMap
     *
     * @return
     */
    public Map<OffsetDateTime, Double> getDatesMap() {
        return datesMap;
    }

    /**
     * The LineGraph object delegates the actual work of plotting the data to the specific graph adapter
     */
    @Override
    public void plotData() {
        lineGraphAdapter.setView(root);
        lineGraphAdapter.plotData();
    }

    /**
     * Obtains the dataset which will be plotted, namely a map with dates and the sums spent/received on each specific day
     *
     * @param transactionsList
     */
    public void obtainDataset(List<Transaction> transactionsList) {
        // CODE TAKEN FROM START <8>
        /*
        The line of code for joining two lists using streams was taken from:
        https://stackoverflow.com/questions/189559/how-do-i-join-two-lists-in-java/34090554#34090554

        The idea of modifying a field of an object using streams was taken from:
        https://stackoverflow.com/questions/38496455/modify-property-value-of-the-objects-in-list-using-java-8-streams/38497684
        */
        List<Transaction> expenseTransactions = new ArrayList<>(transactionsList).stream()
                .filter(transaction -> transaction instanceof ExpenseTransaction)
                .map(expenseTransaction -> new Transaction(expenseTransaction.getAccountId(), expenseTransaction.getCategory(),
                        expenseTransaction.getDescription(), -expenseTransaction.getAmount(), expenseTransaction.getCreationData()))
                .collect(Collectors.toList());
        List<Transaction> incomeTransactions = new ArrayList<>(transactionsList).stream()
                .filter(transaction -> transaction instanceof IncomeTransaction)
                .collect(Collectors.toList());
        List<Transaction> incomesAndExpensesTransactionsList = Stream.concat(expenseTransactions.stream(), incomeTransactions.stream())
                .collect(Collectors.toList());
        // CODE TAKEN FROM END <8>

        // CODE TAKEN FROM START <7>
        /*
        The idea of retrieving the amounts of each date by using a map and streams was taken from:
        https://docs.oracle.com/javase/tutorial/collections/streams/reduction.html
        */
        Map<OffsetDateTime, Double> updatedDatesMap = incomesAndExpensesTransactionsList.stream()
                .collect(Collectors.groupingBy(
                        Transaction::getCreationData,
                        Collectors.reducing(
                                .0,
                                Transaction::getAmount,
                                Double::sum)));
        // CODE TAKEN FROM END <7>
        this.datesMap = updatedDatesMap;
        this.lineGraphAdapter = new LineGraphAdapter(datesMap);
    }

    /**
     * lineGraphAdapter getter method
     *
     * @return
     */
    public LineGraphAdapter getLineGraphAdapter() {
        return lineGraphAdapter;
    }
}
