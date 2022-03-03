package at.ac.univie.t0306.expensetracker.ui.transaction;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import at.ac.univie.t0306.expensetracker.database.data.Account;
import at.ac.univie.t0306.expensetracker.database.data.Category;
import at.ac.univie.t0306.expensetracker.database.data.ExpenseTransaction;
import at.ac.univie.t0306.expensetracker.database.data.IncomeTransaction;
import at.ac.univie.t0306.expensetracker.database.data.Transaction;
import at.ac.univie.t0306.expensetracker.database.data_facade.DataRepo;
import at.ac.univie.t0306.expensetracker.databinding.ActivityEditTransactionBinding;
import at.ac.univie.t0306.expensetracker.exceptions.InvalidInputDataException;
import at.ac.univie.t0306.expensetracker.managers.TransactionManager;
import at.ac.univie.t0306.expensetracker.ui.recyclers.AlertsSpinnerAdapter;
import at.ac.univie.t0306.expensetracker.ui.recyclers.CategorySpinnerAdapter;
import at.ac.univie.t0306.expensetracker.ui.reports.reportViewFragments.TransactionListFragment;

/**
 * {@inheritDoc}
 */
public class EditTransactionActivity extends AppCompatActivity {


    private ActivityEditTransactionBinding binding;
    private EditText etDescription, etAmount, etDate;
    private Button btnEdit;
    TransactionManager transactionManager;
    private Transaction passedTransaction;
    private Spinner spnAccounts, spnCategories;
    private DataRepo dataRepo;

    /**
     * {@inheritDoc}
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditTransactionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        passedTransaction = (Transaction) getIntent().getSerializableExtra(TransactionListFragment.TRANSACTION_PARAMETER);
        transactionManager = new TransactionManager(new DataRepo(getApplication()));
        dataRepo = new DataRepo(getApplication());

        inflateUI();
        setTransactionData();
        handleEditClicked();

    }

    /**
     * Method to handle scenarios when the save button is saved. E.g: check for valid inputs.
     */
    private void handleEditClicked() {

        btnEdit.setOnClickListener(view -> {
            // check the input
            try {
                checkInputData();
                boolean transactionReady = true;
                Account selectedAccount = (Account) spnAccounts.getSelectedItem();
                if (selectedAccount == null) {
                    transactionReady = false;
                    Toast.makeText(getApplicationContext(), "An account must be created beforehand", Toast.LENGTH_SHORT).show();
                }

                Category category = (Category) spnCategories.getSelectedItem();
                if (category == null) {
                    transactionReady = false;
                    Toast.makeText(getApplicationContext(), "A category must be created beforehand", Toast.LENGTH_SHORT).show();
                }

                if (transactionReady) {
                    double transactionAmount = Double.parseDouble(etAmount.getText().toString());
                    String transactionDescription = etDescription.getText().toString();
                    OffsetDateTime transactionDate = getOffsetDateTime(etDate);

                    Transaction newTransaction = null;
                    if (passedTransaction instanceof IncomeTransaction) {
                        newTransaction = new IncomeTransaction(selectedAccount.getId(), category.getName(), transactionDescription, transactionAmount, transactionDate);
                    } else {
                        newTransaction = new ExpenseTransaction(selectedAccount.getId(), category.getName(), transactionDescription, transactionAmount, transactionDate);
                    }
                    newTransaction.setId(passedTransaction.getId());
                    transactionManager.updateTransaction(passedTransaction, newTransaction, selectedAccount);
                    Toast.makeText(getApplicationContext(), "Transaction edited", Toast.LENGTH_SHORT).show();
                    finish();
                }

            } catch (InvalidInputDataException e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("add account", e.getMessage());
            }


        });
    }

    /**
     * Check data before realizing the change to the database
     *
     * @throws InvalidInputDataException
     */
    private void checkInputData() throws InvalidInputDataException {
        if (TextUtils.isEmpty(etDate.getText()) || TextUtils.isEmpty(etDescription.getText()) || TextUtils.isEmpty(etAmount.getText()))
            throw new InvalidInputDataException("please fill all fields!!");
    }

    /**
     * Fill the editable TextInputs with present data
     */
    @SuppressLint("SetTextI18n")
    private void setTransactionData() {
        setUpSpinnerViews();
        etDescription.setText(passedTransaction.getDescription());
        etAmount.setText(passedTransaction.getAmount() + "");
        etDate.setText(passedTransaction.getCreationData().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
    }

    /**
     * Setup the spinner objects to the actual data in the database
     */
    private void setUpSpinnerViews() {
        AlertsSpinnerAdapter spinnerAdapter = new AlertsSpinnerAdapter(getApplicationContext());
        spnAccounts.setAdapter(spinnerAdapter);

        CategorySpinnerAdapter categoryAdapter = new CategorySpinnerAdapter(getApplicationContext());
        spnCategories.setAdapter(categoryAdapter);

        dataRepo.getAccountsLiveData().observe(this, (accounts -> {
            spinnerAdapter.setList(accounts);
            // set the account of the passed transaction based on the id of the account
            int transactionAccountPosition = accounts.stream().map(Account::getId).collect(Collectors.toList()).indexOf(passedTransaction.getAccountId());
            spnAccounts.setSelection(transactionAccountPosition);
        }));

        dataRepo.getCategoriesLiveData().observe(this, (categories -> {
            Category transactionCategory = new Category(passedTransaction.getCategory());
            if (!categories.stream().anyMatch(category -> category.getName().equals(transactionCategory.getName())))
                categories.add(transactionCategory);
            categoryAdapter.setList(categories);
            spnCategories.setSelection(categories.stream().map(Category::getName).collect(Collectors.toList()).indexOf(passedTransaction.getCategory()));
        }));
    }

    /**
     * Method to map all instance variables to the UIElements in the app.
     */
    private void inflateUI() {
        etDescription = binding.activityEditTransactionEtDescription;
        etAmount = binding.activityEditTransactionEtAmount;
        etDate = binding.activityEditTransactionEtDate;
        btnEdit = binding.activityEditTransactionBtnEdit;
        spnAccounts = binding.activityEditTransactionSpnAccountName;
        spnCategories = binding.activityEditTransactionSpnCategoryEdit;

        Toolbar toolbar = binding.activityEditTransactionToolbar;

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(v -> finish());
    }

    /**
     * Method to build a OffsetTime object of a string
     *
     * @param editTextDate String which will be converted to a OffsetTime Object
     * @return OffsetDateTime Object
     */
    private OffsetDateTime getOffsetDateTime(EditText editTextDate) {
        OffsetDateTime offsetDateTime = OffsetDateTime.now();

        try {
            String day = String.valueOf(editTextDate.getText().charAt(0)) + editTextDate.getText().charAt(1);
            String month = String.valueOf(editTextDate.getText().charAt(3)) + editTextDate.getText().charAt(4);
            String year = String.valueOf(editTextDate.getText().charAt(6)) + editTextDate.getText().charAt(7) + editTextDate.getText().charAt(8) + editTextDate.getText().charAt(9);
            offsetDateTime = OffsetDateTime.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day), 0, 0, 0, 0, ZoneOffset.UTC);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Date format was incorrect!! date of today is taken!!", Toast.LENGTH_SHORT).show();
        }
        return offsetDateTime;
    }
}