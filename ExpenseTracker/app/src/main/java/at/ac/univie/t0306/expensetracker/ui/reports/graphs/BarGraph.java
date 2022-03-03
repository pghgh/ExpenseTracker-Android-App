package at.ac.univie.t0306.expensetracker.ui.reports.graphs;

import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import at.ac.univie.t0306.expensetracker.database.data.ExpenseTransaction;
import at.ac.univie.t0306.expensetracker.database.data.IncomeTransaction;
import at.ac.univie.t0306.expensetracker.database.data.Transaction;

/**
 * Class which will plot data on a bar graph
 * with the help of a specific adapter which facilitates the communication with a Third Party Visualization Library
 */
public class BarGraph implements GraphVisualization {

    private BarGraphAdapter barGraphAdapter;
    private Map<String, Double> categoriesMap;
    private View root;

    /**
     * Constructor which needs a view for setting up the graph
     *
     * @param root
     */
    public BarGraph(View root) {
        this.root = root;
    }

    /**
     * Getter method for the CategoriesMap
     *
     * @return
     */
    public Map<String, Double> getCategoriesMap() {
        return categoriesMap;
    }

    /**
     * The BarGraph object delegates the actual work of plotting the data to the specific graph adapter
     */
    @Override
    public void plotData() {
        barGraphAdapter.setView(root);
        barGraphAdapter.plotData();
    }

    /**
     * Obtains the dataset which will be plotted, namely a map with categories and the sums spent/received on each category
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
        The idea of retrieving the amounts of each category by using a map and streams was taken from:
        https://docs.oracle.com/javase/tutorial/collections/streams/reduction.html
        */
        Map<String, Double> updatedCategoriesMap = incomesAndExpensesTransactionsList.stream()
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,
                        Collectors.reducing(
                                .0,
                                Transaction::getAmount,
                                Double::sum)));
        // CODE TAKEN FROM END <7>
        this.categoriesMap = updatedCategoriesMap;
        this.barGraphAdapter = new BarGraphAdapter(categoriesMap);
    }

    /**
     * barGraphAdapter getter method
     *
     * @return
     */
    public BarGraphAdapter getBarGraphAdapter() {
        return barGraphAdapter;
    }

}
