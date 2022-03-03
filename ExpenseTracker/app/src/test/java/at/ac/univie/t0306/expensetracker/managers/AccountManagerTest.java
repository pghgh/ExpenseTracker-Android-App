package at.ac.univie.t0306.expensetracker.managers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import at.ac.univie.t0306.expensetracker.database.data.Account;
import at.ac.univie.t0306.expensetracker.database.data_facade.DataRepo;

public class AccountManagerTest {

    private DataRepo dataRepoMocked;
    private AccountManager accountManager;

    @Before
    public void setUp() {
        dataRepoMocked = Mockito.mock(DataRepo.class);
        accountManager = new AccountManager(dataRepoMocked);
    }


    @Test
    public void AccountInsertionTest() {
        Account accountShouldBeInserted = new Account("name", "desc", 100.0, "type", OffsetDateTime.now());
        accountManager.addAccount(accountShouldBeInserted);
        Mockito.verify(dataRepoMocked, Mockito.times(1)).addAccounts(accountShouldBeInserted);
    }

    @Test
    public void AccountUpdateTest() {
        Account accountShouldBeUpdated = new Account("name", "desc", 100.0, "type", OffsetDateTime.now());
        Account accountToBeUpdated = new Account("New name", "desc", 100.0, "type", OffsetDateTime.now());
        accountManager.updateAccount(accountShouldBeUpdated, accountToBeUpdated);
        Mockito.verify(dataRepoMocked, Mockito.times(1)).updateAccount(accountShouldBeUpdated, accountToBeUpdated);
    }


    @Test
    public void AccountDeleteTest() {
        Account accountShouldDeleted = new Account("name", "desc", 100.0, "type", OffsetDateTime.now());
        accountManager.deleteAccount(accountShouldDeleted);
        Mockito.verify(dataRepoMocked, Mockito.times(1)).deleteAccount(accountShouldDeleted);
    }

}
