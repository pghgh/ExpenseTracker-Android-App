package at.ac.univie.t0306.expensetracker.managers;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import at.ac.univie.t0306.expensetracker.database.data.Category;
import at.ac.univie.t0306.expensetracker.database.data_facade.DataRepo;

public class CategoryManagerTest {

    private DataRepo dataRepoMocked;
    private CategoryManager categoryManager;

    @Before
    public void setUp() {
        dataRepoMocked = Mockito.mock(DataRepo.class);
        categoryManager = new CategoryManager(dataRepoMocked);
    }


    @Test
    public void CategoryInsertionTest() {
        Category categoryShouldBeInserted = new Category("Name");
        categoryManager.addCategory(categoryShouldBeInserted);
        Mockito.verify(dataRepoMocked, Mockito.times(1)).addCategories(categoryShouldBeInserted);
    }

    @Test
    public void CategoryUpdateTest() {
        Category categoryShouldBeUpdated = new Category("Name1");
        Category categoryToBeUpdated = new Category("Name2");
        categoryManager.updateCategory(categoryShouldBeUpdated, categoryToBeUpdated);
        Mockito.verify(dataRepoMocked, Mockito.times(1)).updateCategory(categoryShouldBeUpdated, categoryToBeUpdated);
    }


    @Test
    public void CategoryDeleteTest() {
        Category categoryShouldBeDeleted = new Category("Name");
        categoryManager.addCategory(categoryShouldBeDeleted);
        categoryManager.deleteCategory(categoryShouldBeDeleted);
        Mockito.verify(dataRepoMocked, Mockito.times(1)).deleteCategory(categoryShouldBeDeleted);
    }

}
