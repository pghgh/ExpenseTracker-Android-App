package at.ac.univie.t0306.expensetracker.ui.accounts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.util.StringUtil;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import at.ac.univie.t0306.expensetracker.database.data.Account;
import at.ac.univie.t0306.expensetracker.database.data.EAccountType;
import at.ac.univie.t0306.expensetracker.database.data_facade.DataRepo;
import at.ac.univie.t0306.expensetracker.databinding.ActivityAddAccountBinding;
import at.ac.univie.t0306.expensetracker.exceptions.InvalidInputDataException;
import at.ac.univie.t0306.expensetracker.managers.AccountManager;

public class AddAccountActivity extends AppCompatActivity {

    private ActivityAddAccountBinding binding;
    private EditText etName, etDescription, etBalance, etAccountType;
    private TextView tvNewAccountType;
    private Button addBtn;
    private Spinner spinnerAccountType;
    AccountManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        manager = new AccountManager(new DataRepo(getApplication()));
        inflateUI();
        setSpinnerData();
        handleOtherAccountTypeChosen();
        handleAddBtnClicked();
    }

    /**
     * to display/hide the field of account type and handle the user input
     */
    private void handleOtherAccountTypeChosen() {
        spinnerAccountType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spinnerAccountType.getSelectedItem().equals("Other")) {
                    etAccountType.setVisibility(View.VISIBLE);
                    tvNewAccountType.setVisibility(View.VISIBLE);
                } else {
                    etAccountType.setVisibility(View.INVISIBLE);
                    tvNewAccountType.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //ignore
            }
        });


    }

    /**
     * to set up the spinner view and fill it with the predefined account types
     */
    private void setSpinnerData() {
        List<String> accountTypes = Arrays.stream(EAccountType.values()).map(eAccountType -> eAccountType.getName()).collect(Collectors.toList());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, accountTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAccountType.setAdapter(adapter);
    }

    /**
     * to handle the event when the add button is clicked
     */
    private void handleAddBtnClicked() {
        addBtn.setOnClickListener((view) -> {
            // check data before adding
            try {
                checkInputData();


                String accountType = EAccountType.values()[spinnerAccountType.getSelectedItemPosition()].getName();
                if (accountType.equals(EAccountType.OTHER.getName()))
                    accountType = etAccountType.getText().toString();

                Account account = new Account(etName.getText().toString(), etDescription.getText().toString(), Double.parseDouble(etBalance.getText().toString()), accountType, OffsetDateTime.now());
                manager.addAccount(account);
                Toast.makeText(getApplicationContext(), "Account created", Toast.LENGTH_SHORT).show();
                finish();
            } catch (InvalidInputDataException e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("add account", e.getMessage());
            }
        });
    }

    /**
     * checks the input fields of the user
     *
     * @throws InvalidInputDataException
     */
    private void checkInputData() throws InvalidInputDataException {
        if (TextUtils.isEmpty(etName.getText()) || TextUtils.isEmpty(etDescription.getText()) || TextUtils.isEmpty(etBalance.getText()))
            throw new InvalidInputDataException("please fill all fields!!");
    }

    /**
     * inflate the UI elements from the xml file
     */
    private void inflateUI() {
        etName = binding.etName;
        etDescription = binding.etDescription;
        etBalance = binding.etBalance;
        etAccountType = binding.etAccountType;
        tvNewAccountType = binding.tvNewAccountType;
        addBtn = binding.addActivityBtnAddAccount;
        spinnerAccountType = binding.spinnerAccountType;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Toolbar toolbar = binding.addAccountToolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(v -> finish());

    }


}