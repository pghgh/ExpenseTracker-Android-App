package at.ac.univie.t0306.expensetracker.ui.alert;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import at.ac.univie.t0306.expensetracker.database.data.Account;
import at.ac.univie.t0306.expensetracker.database.data.AccountAndAlert;
import at.ac.univie.t0306.expensetracker.database.data.Alert;
import at.ac.univie.t0306.expensetracker.database.data_facade.DataRepo;
import at.ac.univie.t0306.expensetracker.databinding.FragmentAlertBinding;
import at.ac.univie.t0306.expensetracker.exceptions.InvalidInputDataException;
import at.ac.univie.t0306.expensetracker.managers.AlertManager;
import at.ac.univie.t0306.expensetracker.ui.recyclers.AccountAndAlertRecyclerAdapter;
import at.ac.univie.t0306.expensetracker.ui.recyclers.AlertsSpinnerAdapter;

public class AlertFragment extends Fragment {

    private AlertViewModel alertViewModel;
    private FragmentAlertBinding binding;

    private Spinner accountsSpinneriew;
    private EditText thresholdEditText;
    private RecyclerView alertsRv;
    private Button setAlertButton;

    private AlertManager alertManager;

    private AccountAndAlertRecyclerAdapter alertRecyclerAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        alertViewModel =
                new ViewModelProvider(this).get(AlertViewModel.class);

        binding = FragmentAlertBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        alertManager = new AlertManager(new DataRepo(getActivity().getApplication()));

        inflateUI();
        setupAccountSpinner();
        setupRecyclerView();
        handleSetButtonClicked();


        return root;
    }

    private void handleSetButtonClicked() {
        setAlertButton.setOnClickListener(v -> {
            Account accountSelected = (Account) accountsSpinneriew.getSelectedItem();
            int accountId = 0;
            boolean accountExists = true;
            try {
                accountId = accountSelected.getId();
            } catch (NullPointerException e) {
                accountExists = false;
                Toast.makeText(getContext(), "An account must be created beforehand", Toast.LENGTH_SHORT).show();
            }
            if (accountExists) {
                try {
                    checkInputData();
                    double thresholdValue = Double.parseDouble(thresholdEditText.getText().toString());
                    int finalAccountId = accountId;
                    Optional<AccountAndAlert> accountAndAlert = alertRecyclerAdapter.getItemList().stream().filter(accountAndAlert1 -> accountAndAlert1.getAccount().getId() == finalAccountId).findFirst();
                    if (accountAndAlert.isPresent() && accountAndAlert.get().getAlert() != null) {
                        Alert old_alert = accountAndAlert.get().getAlert();
                        Alert new_alert = new Alert(thresholdValue, accountId);
                        new_alert.setId(old_alert.getId());
                        alertManager.updateAlert(old_alert, new_alert);
                    } else
                        alertManager.setNewAlert(thresholdValue, accountId);
                } catch (InvalidInputDataException e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("add account", e.getMessage());
                }
            }
        });

    }

    private void setupRecyclerView() {
        alertRecyclerAdapter = new AccountAndAlertRecyclerAdapter(getContext(), alertManager);
        alertsRv.setAdapter(alertRecyclerAdapter);
        alertsRv.setLayoutManager(new LinearLayoutManager(getContext()));

        alertViewModel.getAccountAndAlertListLiveData().observe(getViewLifecycleOwner(), accountAndAlertList -> {

            List<AccountAndAlert> OnlyAccountWithAlertList = accountAndAlertList.stream().filter(accountAndAlert -> accountAndAlert.getAlert() != null).collect(Collectors.toList());
            alertRecyclerAdapter.setItemList(OnlyAccountWithAlertList);
        });
    }

    private void setupAccountSpinner() {

        AlertsSpinnerAdapter spinnerAdapter = new AlertsSpinnerAdapter(getContext());
        accountsSpinneriew.setAdapter(spinnerAdapter);
        alertViewModel.getAccountListLiveData().observe(getViewLifecycleOwner(), (accounts -> {
            spinnerAdapter.setList(accounts);
        }));
    }

    private void inflateUI() {
        accountsSpinneriew = binding.spinnerAccounts;
        thresholdEditText = binding.editTextThreshold;
        alertsRv = binding.rvAlerts;
        setAlertButton = binding.buttonSetAlert;
    }


    /**
     * checks the input fields of the user
     *
     * @throws InvalidInputDataException
     */
    private void checkInputData() throws InvalidInputDataException {
        if (TextUtils.isEmpty(thresholdEditText.getText()))
            throw new InvalidInputDataException("please fill all fields!!");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}