package at.ac.univie.t0306.expensetracker.ui.accounts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.time.OffsetDateTime;

import at.ac.univie.t0306.expensetracker.database.data.Account;
import at.ac.univie.t0306.expensetracker.database.data_facade.DataRepo;
import at.ac.univie.t0306.expensetracker.databinding.ActivityEditAccountBinding;
import at.ac.univie.t0306.expensetracker.exceptions.InvalidInputDataException;
import at.ac.univie.t0306.expensetracker.managers.AccountManager;

public class EditAccountActivity extends AppCompatActivity {


    private ActivityEditAccountBinding binding;
    private EditText etName, etDescription, etBalance, etAccountType;
    private Button editBtn;
    AccountManager manager;
    private Account passedAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        passedAccount = (Account) getIntent().getSerializableExtra(AccountsFragment.ACCOUNT_PARAMETER);
        manager = new AccountManager(new DataRepo(getApplication()));
        inflateUI();
        setAccountData();
        handleEditClicked();

    }

    /**
     * to handle the event when the edit button is clicked
     */
    private void handleEditClicked() {

        editBtn.setOnClickListener(view -> {
            // check the input
            try {
                checkInputData();

                String accountType = etAccountType.getText().toString();
                Account newAccount = new Account(etName.getText().toString(), etDescription.getText().toString(), Double.parseDouble(etBalance.getText().toString()), accountType, OffsetDateTime.now());
                newAccount.setId(passedAccount.getId());
                manager.updateAccount(passedAccount, newAccount);
                Toast.makeText(getApplicationContext(), "Account edited", Toast.LENGTH_SHORT).show();
                finish();
            } catch (InvalidInputDataException e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("add account", e.getMessage());
            }


        });
    }

    /**
     * checks the input fields
     *
     * @throws InvalidInputDataException
     */
    private void checkInputData() throws InvalidInputDataException {
        if (TextUtils.isEmpty(etName.getText()) || TextUtils.isEmpty(etDescription.getText()) || TextUtils.isEmpty(etBalance.getText()) || TextUtils.isEmpty(etAccountType.getText()))
            throw new InvalidInputDataException("please fill all fields!!");
    }

    /**
     * set the to be edited account data in the input fields
     */
    @SuppressLint("SetTextI18n")
    private void setAccountData() {
        etName.setText(passedAccount.getName());
        etDescription.setText(passedAccount.getDescription());
        etBalance.setText(passedAccount.getBalance() + "");
        etAccountType.setText(passedAccount.getAccountType());
    }


    /**
     * inflate the UI elements from the xml file
     */
    private void inflateUI() {
        etName = binding.editEtName;
        etDescription = binding.editEtDescription;
        etBalance = binding.editEtBalance;
        etAccountType = binding.editEtAccountType;
        editBtn = binding.editBtnEditAccount;
        Toolbar toolbar = binding.EditAccountToolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(v -> finish());
    }
}