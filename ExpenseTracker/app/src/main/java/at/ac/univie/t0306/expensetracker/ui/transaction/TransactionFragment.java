package at.ac.univie.t0306.expensetracker.ui.transaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;

import at.ac.univie.t0306.expensetracker.database.data.Account;
import at.ac.univie.t0306.expensetracker.database.data.Category;
import at.ac.univie.t0306.expensetracker.database.data.ExpenseCreator;
import at.ac.univie.t0306.expensetracker.database.data.ExpenseTransaction;
import at.ac.univie.t0306.expensetracker.database.data.IncomeCreator;
import at.ac.univie.t0306.expensetracker.database.data.IncomeTransaction;
import at.ac.univie.t0306.expensetracker.database.data.Transaction;
import at.ac.univie.t0306.expensetracker.database.data.TransactionCreator;
import at.ac.univie.t0306.expensetracker.database.data_facade.DataRepo;
import at.ac.univie.t0306.expensetracker.databinding.FragmentTransactionBinding;
import at.ac.univie.t0306.expensetracker.managers.AlertManager;
import at.ac.univie.t0306.expensetracker.managers.TransactionManager;
import at.ac.univie.t0306.expensetracker.ui.recyclers.AlertsSpinnerAdapter;
import at.ac.univie.t0306.expensetracker.ui.recyclers.CategorySpinnerAdapter;

/**
 * {@inheritDoc}
 */
public class TransactionFragment extends Fragment {

    TransactionManager manager;
    private FragmentTransactionBinding binding;
    private EditText etAmount, etDate, etAccountName, etDescription;
    private Button addBtn;
    private ArrayList<EditText> textBoxes = new ArrayList();
    private boolean transactionReady = true;
    private Spinner accountsSpinnerview, categoriesSpinnerView;
    private AlertManager alertManager;
    private RadioGroup radioGroup;
    private RadioButton incomesCheckBox;
    private RadioButton expensesCheckBox;

    public TransactionFragment() {
    }


    public static TransactionFragment newInstance() {
        return new TransactionFragment();
    }

    /**
     * {@inheritDoc}
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentTransactionBinding.inflate(inflater, container, false);
        DataRepo dataRepo = new DataRepo(getActivity().getApplication());
        manager = new TransactionManager(dataRepo);
        alertManager = new AlertManager(dataRepo);

        inflateUI();
        handleAddBtnClicked();
        setUpSpinnerViews(dataRepo);
        return binding.getRoot();
    }

    /**
     * Setup the spinner objects to the actual data in the database
     *
     * @param dataRepo DataRepo object which is the actual database of this instance
     */
    private void setUpSpinnerViews(DataRepo dataRepo) {
        AlertsSpinnerAdapter spinnerAdapter = new AlertsSpinnerAdapter(getContext());
        accountsSpinnerview.setAdapter(spinnerAdapter);

        CategorySpinnerAdapter categoryAdapter = new CategorySpinnerAdapter(getContext());
        categoriesSpinnerView.setAdapter(categoryAdapter);


        dataRepo.getAccountsLiveData().observe(getViewLifecycleOwner(), (accounts -> {
            spinnerAdapter.setList(accounts);
        }));

        dataRepo.getCategoriesLiveData().observe(getViewLifecycleOwner(), (categories -> {
            categoryAdapter.setList(categories);

        }));
    }

    /**
     * Method which checks if the add button was clicked and check for valid inputs.
     */
    private void handleAddBtnClicked() {

        addBtn.setOnClickListener((view) -> {
            transactionReady = true;
            for (EditText e : textBoxes) {
                if (TextUtils.isEmpty(e.getText())) {
                    transactionReady = false;
                    Toast.makeText(getActivity().getApplicationContext(), "Please check your inputs", Toast.LENGTH_SHORT).show();
                }
            }

            Account selectedAccount = (Account) accountsSpinnerview.getSelectedItem();
            if (selectedAccount == null) {
                transactionReady = false;
                Toast.makeText(getActivity().getApplicationContext(), "An account must be created beforehand", Toast.LENGTH_SHORT).show();
            }

            Category category = (Category) categoriesSpinnerView.getSelectedItem();
            if (category == null) {
                transactionReady = false;
                Toast.makeText(getActivity().getApplicationContext(), "A category must be created beforehand", Toast.LENGTH_SHORT).show();
            }

            if (!expensesCheckBox.isChecked() && !incomesCheckBox.isChecked()) {
                transactionReady = false;
                Toast.makeText(getActivity().getApplicationContext(), "A type of Transaction must be selected", Toast.LENGTH_SHORT).show();
            }

            if (transactionReady) {

                Transaction transaction = null;

                if (radioGroup.getCheckedRadioButtonId() == incomesCheckBox.getId()) {
                    IncomeCreator incomeCreator = new IncomeCreator();
                    transaction = incomeCreator.createTransaction(selectedAccount.getId(), category.getName(), etDescription.getText().toString(), Double.parseDouble(etAmount.getText().toString()), getOffsetDateTime(etDate));
                    addTransaction(transaction, selectedAccount);
                } else if (radioGroup.getCheckedRadioButtonId() == expensesCheckBox.getId()) {
                    ExpenseCreator expenseCreator = new ExpenseCreator();
                    transaction = expenseCreator.createTransaction(selectedAccount.getId(), category.getName(), etDescription.getText().toString(), Double.parseDouble(etAmount.getText().toString()), getOffsetDateTime(etDate));

                    if (alertManager.checkThresholdExceededWhenExpense(selectedAccount.getId(), transaction.getAmount())) {
                        createAlertDialog(transaction, selectedAccount);
                    } else {
                        addTransaction(transaction, selectedAccount);
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "An error occurred while creating a transaction, retry", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });

    }

    /**
     * After inserting the transaction all inputs can be cleared.
     */
    private void clearInputs() {

        etAmount.getText().clear();
        etDate.getText().clear();
        etAccountName.getText().clear();
        etDescription.getText().clear();

    }

    /**
     * Method to build a OffsetTime object of a string
     *
     * @param editTextDate String which will be converted to a OffsetTime Object
     * @return OffsetDateTime Object
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
     * creates Alert Dialog to ask the user before adding new expense transaction when the threshold was exceeded
     *
     * @param transaction the transaction to be added when the user will add the transaction
     * @param account     the account to which the transaction belongs
     */
    private void createAlertDialog(Transaction transaction, Account account) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Warning : Threshold for your account exceeded")
                .setMessage("Would you like to continue anyway?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addTransaction(transaction, account);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearInputs();
                        dialog.dismiss();
                    }

                });
        AlertDialog dialog = builder.create();
        Log.i("called", "asdfda");
        dialog.show();
    }

    /**
     * Helper method to insert a new transaction
     *
     * @param transaction Transaction Object
     * @param account     Account which the transaction affects
     */
    private void addTransaction(Transaction transaction, Account account) {
        manager.addTransaction(transaction, account);
        Toast.makeText(getActivity().getApplicationContext(), "Transaction added", Toast.LENGTH_SHORT).show();
        clearInputs();
    }

    /**
     * Method to map all instance variables to the UIElements in the app.
     */
    private void inflateUI() {

        etAmount = binding.etTransactionAmount;
        etDate = binding.etTransactionDate;
        etAccountName = binding.etTransactionAccountName;
        etDescription = binding.etTransactionDescription;
        addBtn = binding.addActivityBtnAddTransaction;
        accountsSpinnerview = binding.spinnerAccountsTransaction;
        categoriesSpinnerView = binding.spinnerCategoryTransaction;
        incomesCheckBox = binding.incomeRadio;
        expensesCheckBox = binding.expenseRadio;
        radioGroup = binding.radioGroupTransaction;

        textBoxes.add(etAmount);
        textBoxes.add(etDate);
        textBoxes.add(etAmount);
        textBoxes.add(etDescription);
    }
}