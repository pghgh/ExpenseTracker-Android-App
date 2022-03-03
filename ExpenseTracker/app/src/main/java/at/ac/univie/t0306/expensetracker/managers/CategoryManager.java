package at.ac.univie.t0306.expensetracker.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import at.ac.univie.t0306.expensetracker.database.AppDatabase;
import at.ac.univie.t0306.expensetracker.database.data.Category;
import at.ac.univie.t0306.expensetracker.database.data_facade.DataRepo;

public class CategoryManager {

    private DataRepo dataRepo;
    List<Category> categoriesList = new ArrayList<>();

    public CategoryManager(DataRepo dataRepo) {
        this.dataRepo = dataRepo;
    }

    public void deleteCategory(Category category) {
        dataRepo.deleteCategory(category);

    }

    public void addCategory(Category category) {
        dataRepo.addCategories(category);

    }

    public List<Category> getAllCategories() {
        return categoriesList;
    }

    public void updateCategory(Category old_category, Category new_category) {
        dataRepo.updateCategory(old_category, new_category);
    }


}
