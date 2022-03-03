package at.ac.univie.t0306.expensetracker.ui.alert;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import at.ac.univie.t0306.expensetracker.database.data.Account;
import at.ac.univie.t0306.expensetracker.database.data.AccountAndAlert;
import at.ac.univie.t0306.expensetracker.database.data_facade.DataRepo;

public class AlertViewModel extends AndroidViewModel {

    private LiveData<List<Account>> AccountsListLiveData;
    private LiveData<List<AccountAndAlert>> AccountAndAlertListLiveData;


    public AlertViewModel(Application application) {
        super(application);

        DataRepo repo = new DataRepo(application);
        AccountsListLiveData = repo.getAccountsLiveData();
        AccountAndAlertListLiveData = repo.getAccountAndAlerts();
    }

    public LiveData<List<Account>> getAccountListLiveData() {
        return AccountsListLiveData;
    }

    public LiveData<List<AccountAndAlert>> getAccountAndAlertListLiveData() {
        return AccountAndAlertListLiveData;
    }

}