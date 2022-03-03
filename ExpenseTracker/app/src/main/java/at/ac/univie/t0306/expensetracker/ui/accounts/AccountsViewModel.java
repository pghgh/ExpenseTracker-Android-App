package at.ac.univie.t0306.expensetracker.ui.accounts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import at.ac.univie.t0306.expensetracker.database.data.Account;
import at.ac.univie.t0306.expensetracker.ui.observer.CacheObserver;
import at.ac.univie.t0306.expensetracker.ui.observer.DataCacheProxy;

/**
 * the view-model class for the account fragment
 */
public class AccountsViewModel extends ViewModel implements CacheObserver {

    private MutableLiveData<List<Account>> listLiveData;
    private DataCacheProxy dataCacheProxy;

    public AccountsViewModel() {
        super();
        dataCacheProxy = DataCacheProxy.getInstance();
        listLiveData = new MutableLiveData<>();
        dataCacheProxy.attachObserver(this);
    }

    /**
     * returns the liveData list of the accounts
     *
     * @return
     */
    public LiveData<List<Account>> getListLiveData() {
        return listLiveData;
    }

    @Override
    public void update() {
        if (dataCacheProxy.getAccountList() != null)
            listLiveData.setValue(dataCacheProxy.getAccountList());
    }
}