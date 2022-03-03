package at.ac.univie.t0306.expensetracker.ui.categories;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import at.ac.univie.t0306.expensetracker.database.data.Category;
import at.ac.univie.t0306.expensetracker.database.data_facade.DataRepo;


/**
 * The view-model class for the category fragment
 */
public class CategoriesViewModel extends AndroidViewModel {

    private LiveData<List<Category>> categoriesList;

    public CategoriesViewModel(Application application) {

        super(application);

        DataRepo repo = new DataRepo(application);
        categoriesList = repo.getCategoriesLiveData();

    }

    /**
     * returns the liveData list of the categories
     *
     * @return
     */
    public LiveData<List<Category>> getCategoriesList() {
        return categoriesList;
    }


}