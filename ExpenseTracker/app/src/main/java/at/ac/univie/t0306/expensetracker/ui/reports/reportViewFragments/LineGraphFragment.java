package at.ac.univie.t0306.expensetracker.ui.reports.reportViewFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.chip.Chip;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import at.ac.univie.t0306.expensetracker.database.data.Transaction;
import at.ac.univie.t0306.expensetracker.databinding.FragmentLineGraphBinding;
import at.ac.univie.t0306.expensetracker.ui.observer.DataCacheProxy;
import at.ac.univie.t0306.expensetracker.ui.reports.filterStrategy.DailyFilterStrategy;
import at.ac.univie.t0306.expensetracker.ui.reports.filterStrategy.FilterStrategy;
import at.ac.univie.t0306.expensetracker.ui.reports.filterStrategy.MonthlyFilterStrategy;
import at.ac.univie.t0306.expensetracker.ui.reports.filterStrategy.WeeklyFilterStrategy;
import at.ac.univie.t0306.expensetracker.ui.reports.graphs.LineGraph;
import at.ac.univie.t0306.expensetracker.ui.reports.graphsUserInteraction.GraphScrollingAndZooming;
import at.ac.univie.t0306.expensetracker.ui.reports.graphsUserInteraction.GraphTappingOnData;
import at.ac.univie.t0306.expensetracker.ui.reports.graphsUserInteraction.GraphVisualizationUserInterfaceFeatures;

/**
 * A {@link Fragment} subclass.
 * The {@link LineGraphFragment#newInstance} factory method is used to
 * create an instance of this fragment.
 */
public class LineGraphFragment extends Fragment {

    private FragmentLineGraphBinding binding;
    private DataCacheProxy transactionCache;
    private List<Transaction> transactions;

    private EditText fromDateEditText, toDateEditText;
    private Chip dailyChip, weeklyChip, monthlyChip, customChip;
    private CheckBox incomesCheckBox, expensesCheckBox;
    private LinearLayout customDatePartlayout;
    private Button customDateOkBtn;
    private LineGraph lineGraph;
    private FilterStrategy filter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLineGraphBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        transactionCache = DataCacheProxy.getInstance();
        transactions = new ArrayList<>();
        lineGraph = new LineGraph(root);
        inflateUI();
        listenToDataChanges();
        return root;
    }

    private void inflateUI() {
        expensesCheckBox = binding.transListExpenseBox;
        incomesCheckBox = binding.transListIncomeBox;

        fromDateEditText = binding.transLitEdFrom;
        toDateEditText = binding.transLitEdTo;
        customDatePartlayout = binding.customLayout;
        customDateOkBtn = binding.transListOk;

        dailyChip = binding.transListDailyChip;
        weeklyChip = binding.transListWeeklyChip;
        monthlyChip = binding.transListMonthlyChip;
        customChip = binding.transListCustopmChip;
    }

    public LineGraphFragment() {
        // Required empty public constructor
    }

    /**
     * Factory method which creates a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LineGraphFragment
     */
    public static LineGraphFragment newInstance() {
        LineGraphFragment fragment = new LineGraphFragment();
        return fragment;
    }

    /**
     * {@inheritDoc}
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Constructs a line graph object which is also "decorated"/enhanced with UI features (tapping on data and graph scrolling/zooming)
     */
    private void constructGraph() {
        lineGraph.obtainDataset(transactions);

        // CODE TAKEN FROM START <1>
        /*
        The lines for decorating the graph object were inspired from the following decorator pattern code exmaple:
        https://refactoring.guru/design-patterns/decorator/java/example
         */
        GraphVisualizationUserInterfaceFeatures userInterfaceFeatures = new GraphTappingOnData(
                new GraphScrollingAndZooming(lineGraph, lineGraph.getLineGraphAdapter()),
                lineGraph.getLineGraphAdapter(), getActivity());
        userInterfaceFeatures.plotData();
        // CODE TAKEN FROM END <1>
    }

    /**
     * Listens to data changes and calls a method to construct an updated graph
     */
    private void listenToDataChanges() {

        incomesCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            //to uncheck all chips
            uncheckOtherChips(0);
            handleCheckBoxChanged(b, expensesCheckBox.isChecked());
            // plot data
            constructGraph();
        });
        expensesCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            //to uncheck all chips
            uncheckOtherChips(0);
            handleCheckBoxChanged(incomesCheckBox.isChecked(), b);
            lineGraph.obtainDataset(transactions);
            // plot data
            constructGraph();
        });

        dailyChip.setOnCheckedChangeListener((compoundButton, b) -> {
            handleCheckBoxChanged(incomesCheckBox.isChecked(), expensesCheckBox.isChecked());
            if (b) {
                uncheckOtherChips(dailyChip.getId());
                setFilterStrategy(new DailyFilterStrategy());
                filter.filterTransactions(transactions);
            }
            lineGraph.obtainDataset(transactions);
            // plot data
            constructGraph();
        });


        weeklyChip.setOnCheckedChangeListener((compoundButton, b) -> {
            handleCheckBoxChanged(incomesCheckBox.isChecked(), expensesCheckBox.isChecked());
            if (b) {

                uncheckOtherChips(weeklyChip.getId());
                setFilterStrategy(new WeeklyFilterStrategy());
                filter.filterTransactions(transactions);
            }

            lineGraph.obtainDataset(transactions);
            // plot data
            constructGraph();
        });

        monthlyChip.setOnCheckedChangeListener((compoundButton, b) -> {
            handleCheckBoxChanged(incomesCheckBox.isChecked(), expensesCheckBox.isChecked());
            if (b) {
                uncheckOtherChips(monthlyChip.getId());
                setFilterStrategy(new MonthlyFilterStrategy());
                filter.filterTransactions(transactions);
            }
            lineGraph.obtainDataset(transactions);
            // plot data
            constructGraph();
        });

        customChip.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                customDatePartlayout.setVisibility(View.VISIBLE);
                uncheckOtherChips(customChip.getId());
            } else {
                customDatePartlayout.setVisibility(View.GONE);
            }

        });

        customDateOkBtn.setOnClickListener((v) -> {
            handleCheckBoxChanged(incomesCheckBox.isChecked(), expensesCheckBox.isChecked());
            OffsetDateTime from = getOffsetDateTime(fromDateEditText);
            OffsetDateTime to = getOffsetDateTime(toDateEditText);

            Log.d("from", from.toString());
            Log.d("to", to.toString());
            transactions.removeIf(transaction -> transaction.getCreationData().compareTo(from) < 0 || transaction.getCreationData().compareTo(to) > 0);
            lineGraph.obtainDataset(transactions);
            // plot data
            constructGraph();
            // reset field
            fromDateEditText.setText("");
            toDateEditText.setText("");
        });


    }

    /**
     * Filters the data when the checkboxes are changed
     *
     * @param incomeCheckbox
     * @param expenseCheckbox
     */
    private void handleCheckBoxChanged(boolean incomeCheckbox, boolean expenseCheckbox) {
        transactions.clear();
        if (incomeCheckbox && expenseCheckbox) {
            transactions.addAll(transactionCache.getIncomeTransactions());
            transactions.addAll(transactionCache.getExpenseTransactions());
        } else if (incomeCheckbox) {
            transactions.addAll(transactionCache.getIncomeTransactions());
        } else if (expenseCheckbox) {
            transactions.addAll(transactionCache.getExpenseTransactions());
        }

    }

    private void uncheckOtherChips(int id) {
        if (id != dailyChip.getId()) {
            dailyChip.setChecked(false);
        }
        if (id != weeklyChip.getId()) {
            weeklyChip.setChecked(false);
        }
        if (id != monthlyChip.getId()) {
            monthlyChip.setChecked(false);
        }
        if (id != customChip.getId()) {
            customChip.setChecked(false);
        }

    }

    /**
     * Constructs the OffsetDateTime object from the input field for Date
     *
     * @param editTextDate
     * @return
     */
    private OffsetDateTime getOffsetDateTime(EditText editTextDate) {
        OffsetDateTime offsetDateTime = OffsetDateTime.now();
        offsetDateTime = OffsetDateTime.of(offsetDateTime.getYear(), offsetDateTime.getMonthValue(), offsetDateTime.getDayOfMonth(), 0, 0, 0, 0, ZoneOffset.UTC);
        try {
            String day = String.valueOf(editTextDate.getText().charAt(0)) + editTextDate.getText().charAt(1);
            String month = String.valueOf(editTextDate.getText().charAt(3)) + editTextDate.getText().charAt(4);

            String year = String.valueOf(editTextDate.getText().charAt(6)) + editTextDate.getText().charAt(7) + editTextDate.getText().charAt(8) + editTextDate.getText().charAt(9);
            offsetDateTime = OffsetDateTime.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day), 0, 0, 0, 0, ZoneOffset.UTC);
        } catch (Exception e) {
            Toast.makeText(getActivity().getApplicationContext(), "The given date format was incorrect. The date of today was chosen instead.", Toast.LENGTH_SHORT).show();
        }
        return offsetDateTime;
    }

    /**
     * setter for Strategy pattern
     * to set new Strategy
     *
     * @param filterStrategy the new Strategy class(Filter strategy)
     */
    private void setFilterStrategy(FilterStrategy filterStrategy) {
        this.filter = filterStrategy;
    }

}