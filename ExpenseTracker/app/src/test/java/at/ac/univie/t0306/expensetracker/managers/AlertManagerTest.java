package at.ac.univie.t0306.expensetracker.managers;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import at.ac.univie.t0306.expensetracker.database.data.Alert;
import at.ac.univie.t0306.expensetracker.database.data_facade.DataRepo;

/**
 * class to test Alert Manager
 */
public class AlertManagerTest {

    private DataRepo dataRepoMocked;
    private AlertManager alertManager;

    @Before
    public void setUp() {
        dataRepoMocked = Mockito.mock(DataRepo.class);
        alertManager = new AlertManager(dataRepoMocked);
    }

    /**
     * test for the method set Alert
     */
    @Test
    public void testSetAlert(){
        alertManager.setNewAlert(50,20);
        Mockito.verify(dataRepoMocked, Mockito.times(1)).addAlert(Mockito.any(Alert.class));
    }
    /**
     * test for the method update Alert
     */
    @Test
    public void testUpdateAlert(){
        Alert oldAlert= new Alert(50,1);
        Alert new_Alert= new Alert(200,1);

        alertManager.updateAlert(oldAlert, new_Alert);
        Mockito.verify(dataRepoMocked, Mockito.times(1)).updateAlert(oldAlert, new_Alert);
    }
    /**
     * test for the method remove Alert
     */
    @Test
    public void testRemoveAlert(){
        Mockito.when(dataRepoMocked.getAlertForAccount(Mockito.anyInt())).thenReturn(new Alert(150.0,20));
        alertManager.removeAlertOfAccount(20);
        Mockito.verify(dataRepoMocked, Mockito.times(2)).getAlertForAccount(20);
        Mockito.verify(dataRepoMocked, Mockito.times(1)).deleteAlert(Mockito.any(Alert.class));
    }
    /**
     * test for the method HasAlert
     */
    @Test
    public void testHasAlert(){
        alertManager.accountHasAlert(20);
        Mockito.verify(dataRepoMocked, Mockito.times(1)).getAlertForAccount(20);
    }



}