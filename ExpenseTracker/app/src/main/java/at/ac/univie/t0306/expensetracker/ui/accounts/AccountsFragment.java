package at.ac.univie.t0306.expensetracker.ui.accounts;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import at.ac.univie.t0306.expensetracker.R;
import at.ac.univie.t0306.expensetracker.database.data.Account;
import at.ac.univie.t0306.expensetracker.database.data_facade.DataRepo;
import at.ac.univie.t0306.expensetracker.databinding.FragmentAccountsBinding;
import at.ac.univie.t0306.expensetracker.managers.AccountManager;
import at.ac.univie.t0306.expensetracker.ui.recyclers.AccountRecyclerAdapter;

public class AccountsFragment extends Fragment {

    private AccountsViewModel accountsViewModel;
    private FragmentAccountsBinding binding;
    private Button btnAddAccount;
    private TextView totalBalanceView;

    private AccountManager accountManager;

    public static final String ACCOUNT_PARAMETER = "account";

    /**
     * {@inheritDoc}
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        accountsViewModel =
                new ViewModelProvider(this).get(AccountsViewModel.class);
        binding = FragmentAccountsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        btnAddAccount = binding.btnAddAccount;
        DataRepo datarepo = new DataRepo(getActivity().getApplication());
        accountManager = new AccountManager(datarepo);
        setUpRecyclerView();
        handleAddAccountClicked();

        totalBalanceView = binding.tvTotalBalanceValue;
        return root;
    }

    /**
     * to handle the event when the add account button clicked
     */
    private void handleAddAccountClicked() {
        btnAddAccount.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), AddAccountActivity.class);
            startActivity(intent);
        });
    }

    /**
     * set up the recycler view , e.g. adapter
     */
    private void setUpRecyclerView() {
        RecyclerView rvAccounts = binding.rvAccounts;
        AccountRecyclerAdapter adapter = new AccountRecyclerAdapter(OnMoreOptionsClicked);
        rvAccounts.setAdapter(adapter);
        rvAccounts.setLayoutManager(new LinearLayoutManager(getContext()));
        listenToDataChanges(adapter);
    }

    /**
     * handle the event of option button clicked
     * can call the method to edit an account or delete it
     */
    private OnMoreOptionsButtonListener<Account> OnMoreOptionsClicked = (view, account, postion) -> {
        PopupMenu popup = new PopupMenu(getContext(), view);
        popup.inflate(R.menu.account_options_menu);
        popup.setOnMenuItemClickListener((item) -> {
                    switch (item.getItemId()) {
                        case R.id.account_delete:
                            accountManager.deleteAccount(account);
                            Toast.makeText(getContext(), "Account deleted", Toast.LENGTH_SHORT).show();
                            return true;
                        case R.id.account_update:
                            doChangeAccountOperation(account);
                            return true;
                        default:
                            return false;
                    }
                }
        );
        popup.show();
    };

    /**
     * to navigate to the edit activity
     *
     * @param account
     */
    private void doChangeAccountOperation(Account account) {
        Intent intent = new Intent(getActivity(), EditAccountActivity.class);
        intent.putExtra(ACCOUNT_PARAMETER, account);
        startActivity(intent);

    }

    /**
     * listening on the changes of the accounts and update the list accordingly
     *
     * @param adapter
     */
    @SuppressLint("SetTextI18n")
    private void listenToDataChanges(AccountRecyclerAdapter adapter) {
        accountsViewModel.getListLiveData().observe(getViewLifecycleOwner(), list -> {
            adapter.setItemList(list);
            double totalBalance = list.stream().map(account -> account.getBalance()).reduce(0.0, (sum, value) -> sum + value);
            totalBalanceView.setText(totalBalance + " €");
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}