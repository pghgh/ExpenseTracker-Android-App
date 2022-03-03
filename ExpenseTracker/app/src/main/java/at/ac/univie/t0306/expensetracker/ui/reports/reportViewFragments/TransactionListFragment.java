package at.ac.univie.t0306.expensetracker.ui.reports.reportViewFragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import at.ac.univie.t0306.expensetracker.R;
import at.ac.univie.t0306.expensetracker.database.data.Category;
import at.ac.univie.t0306.expensetracker.database.data.Transaction;
import at.ac.univie.t0306.expensetracker.database.data_facade.DataRepo;
import at.ac.univie.t0306.expensetracker.database.data_facade.ITransactionsReadOperations;
import at.ac.univie.t0306.expensetracker.databinding.FragmentTransactionListBinding;
import at.ac.univie.t0306.expensetracker.iterators.IIterator;
import at.ac.univie.t0306.expensetracker.iterators.TransactionCategoryContainer;
import at.ac.univie.t0306.expensetracker.iterators.TransactionDateContainer;
import at.ac.univie.t0306.expensetracker.managers.TransactionManager;
import at.ac.univie.t0306.expensetracker.ui.accounts.OnMoreOptionsButtonListener;
import at.ac.univie.t0306.expensetracker.ui.observer.DataCacheProxy;
import at.ac.univie.t0306.expensetracker.ui.recyclers.CategorySpinnerAdapter;
import at.ac.univie.t0306.expensetracker.ui.recyclers.TransactionRecyclerAdapter;
import at.ac.univie.t0306.expensetracker.ui.reports.filterStrategy.DailyFilterStrategy;
import at.ac.univie.t0306.expensetracker.ui.reports.filterStrategy.FilterStrategy;
import at.ac.univie.t0306.expensetracker.ui.reports.filterStrategy.MonthlyFilterStrategy;
import at.ac.univie.t0306.expensetracker.ui.reports.filterStrategy.WeeklyFilterStrategy;
import at.ac.univie.t0306.expensetracker.ui.transaction.EditTransactionActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TransactionListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TransactionListFragment extends Fragment {
    public static final String TRANSACTION_PARAMETER = "transaction";
    private final String ALL_CATEGORIES = "All categories";

    private FragmentTransactionListBinding binding;
    private TransactionManager transactionManager;

    private ITransactionsReadOperations transactionCache;
    //  private SortedSet<Transaction> transactions ;

    //   private DataCacheProxy transactionCache;


    private List<Transaction> transactions;

    private TransactionRecyclerAdapter adapter;
    //Strategy for strategy pattern
    private FilterStrategy filter;

    private EditText fromDateEditText, toDateEditText;
    private Chip dailyChip, weeklyChip, monthlyChip, customChip;
    private CheckBox incomesCheckBox, expensesCheckBox;
    private LinearLayout customDatePartlayout;
    private Button customDateOkBtn;
    private Spinner categoriesSpinnerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTransactionListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        DataRepo dataRepo = new DataRepo(requireActivity().getApplication());
        transactionManager = new TransactionManager(dataRepo);
        transactionCache = DataCacheProxy.getInstance();
        transactions = new ArrayList<>();
        inflateUI();
        setUpSpinnerViews(dataRepo);
        setUpRecyclerView();
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

        categoriesSpinnerView = binding.transListSpinnerCategory;

    }

    public TransactionListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TransactionListFragment.
     */
    public static TransactionListFragment newInstance() {
        TransactionListFragment fragment = new TransactionListFragment();
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
     * set up the recycler view list for transactions
     */
    private void setUpRecyclerView() {
        RecyclerView rvAccounts = binding.transListRv;
        adapter = new TransactionRecyclerAdapter(OnMoreOptionsClicked);
        rvAccounts.setAdapter(adapter);
        rvAccounts.setLayoutManager(new LinearLayoutManager(getContext()));
        listenToFiltersChanges();
    }

    /**
     * to listen on the filter options and update the list accordingly
     */
    private void listenToFiltersChanges() {

        incomesCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            //to uncheck all chips
            uncheckOtherChips(0);
            handleCheckBoxChanged(b, expensesCheckBox.isChecked());
            adapter.setTransactionList(transactions);
        });

        expensesCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            //to uncheck all chips
            uncheckOtherChips(0);
            handleCheckBoxChanged(incomesCheckBox.isChecked(), b);
            adapter.setTransactionList(transactions);
        });

        categoriesSpinnerView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Category selectedCategory = (Category) adapterView.getSelectedItem();
                //to uncheck all chips
                uncheckOtherChips(0);
                handleCheckBoxChanged(incomesCheckBox.isChecked(), expensesCheckBox.isChecked());
                if (!selectedCategory.getName().equals(ALL_CATEGORIES)) {
                    transactions = transactions.stream().filter(transaction -> transaction.getCategory().equals(selectedCategory.getName())).collect(Collectors.toList());
                } else {
                    TransactionCategoryContainer transactionCategoryContainer = new TransactionCategoryContainer(transactions);
                    IIterator iter = transactionCategoryContainer.getIterator();
                    List<Transaction> newOrderTransactions = new ArrayList<>();
                    while (iter.hasNext()) {
                        newOrderTransactions.add(iter.next());
                    }
                    transactions = newOrderTransactions;
                }
                adapter.setTransactionList(transactions);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

        dailyChip.setOnCheckedChangeListener((compoundButton, b) -> {
            handleCheckBoxChanged(incomesCheckBox.isChecked(), expensesCheckBox.isChecked());
            if (b) {
                uncheckOtherChips(dailyChip.getId());
                setFilterStrategy(new DailyFilterStrategy());
                filter.filterTransactions(transactions);
            }
            adapter.setTransactionList(transactions);
        });


        weeklyChip.setOnCheckedChangeListener((compoundButton, b) -> {
            handleCheckBoxChanged(incomesCheckBox.isChecked(), expensesCheckBox.isChecked());
            if (b) {

                uncheckOtherChips(weeklyChip.getId());
                setFilterStrategy(new WeeklyFilterStrategy());
                filter.filterTransactions(transactions);
            }

            adapter.setTransactionList(transactions);
        });

        monthlyChip.setOnCheckedChangeListener((compoundButton, b) -> {
            handleCheckBoxChanged(incomesCheckBox.isChecked(), expensesCheckBox.isChecked());
            if (b) {
                uncheckOtherChips(monthlyChip.getId());
                setFilterStrategy(new MonthlyFilterStrategy());
                filter.filterTransactions(transactions);
            }
            adapter.setTransactionList(transactions);
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
            adapter.setTransactionList(transactions);
            // reset field
            fromDateEditText.setText("");
            toDateEditText.setText("");
        });


    }

    /**
     * filter the data when the checkboxes are changed
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

        TransactionDateContainer transactionDateContainer = new TransactionDateContainer(transactions);
        IIterator iter = transactionDateContainer.getIterator();
        List<Transaction> newOrderTransactions = new ArrayList<>();
        while (iter.hasNext()) {
            newOrderTransactions.add(iter.next());
        }
        transactions = newOrderTransactions;

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
     * to construct the OffsetDateTime object from the input field for Date
     *
     * @param editTextDate
     * @return
     */
    private OffsetDateTime getOffsetDateTime(EditText editTextDate) {
        OffsetDateTime offsetDateTime = OffsetDateTime.now();
        try {
            String day = String.valueOf(editTextDate.getText().charAt(0)) + editTextDate.getText().charAt(1);
            String month = String.valueOf(editTextDate.getText().charAt(3)) + editTextDate.getText().charAt(4);

            String year = String.valueOf(editTextDate.getText().charAt(6)) + editTextDate.getText().charAt(7) + editTextDate.getText().charAt(8) + editTextDate.getText().charAt(9);
            offsetDateTime = OffsetDateTime.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day), 0, 0, 0, 0, ZoneOffset.UTC);
        } catch (Exception e) {
            Toast.makeText(getActivity().getApplicationContext(), "Date format was incorrect!! date of today is taken!!", Toast.LENGTH_SHORT).show();
        }
        return offsetDateTime;
    }

    /**
     * to handle the event when the more Options button clicked
     */
    private OnMoreOptionsButtonListener<Transaction> OnMoreOptionsClicked = (view, transaction, position) -> {
        PopupMenu popup = new PopupMenu(getContext(), view);
        popup.inflate(R.menu.account_options_menu);
        popup.setOnMenuItemClickListener((item) -> {
                    switch (item.getItemId()) {
                        case R.id.account_delete:
                            transactionManager.deleteTransaction(transaction);
                            Toast.makeText(getContext(), "transaction deleted", Toast.LENGTH_SHORT).show();
                            // remove it also from list of recycler view
                            adapter.getTransactionList().remove(transaction);
                            adapter.notifyItemRemoved(position);
                            return true;
                        case R.id.account_update:
                            doChangeAccountOperation(transaction);
                            return true;
                        default:
                            return false;
                    }
                }
        );
        popup.show();
    };

    /**
     * to navigate on the EditTransaction activity to edit the transaction
     *
     * @param transaction
     */
    private void doChangeAccountOperation(Transaction transaction) {
        Intent intent = new Intent(getActivity(), EditTransactionActivity.class);
        intent.putExtra(TRANSACTION_PARAMETER, transaction);
        startActivity(intent);

    }


    /**
     * to set up the spinner view for categories
     *
     * @param dataRepo
     */
    private void setUpSpinnerViews(DataRepo dataRepo) {

        CategorySpinnerAdapter categoryAdapter = new CategorySpinnerAdapter(getContext());
        categoriesSpinnerView.setAdapter(categoryAdapter);

        dataRepo.getCategoriesLiveData().observe(getViewLifecycleOwner(), (categories -> {
            Category allCategories = new Category(ALL_CATEGORIES);
            categories.add(allCategories);
            categoryAdapter.setList(categories);
            categoriesSpinnerView.setSelection(categoryAdapter.getCategoryList().indexOf(allCategories));
        }));


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