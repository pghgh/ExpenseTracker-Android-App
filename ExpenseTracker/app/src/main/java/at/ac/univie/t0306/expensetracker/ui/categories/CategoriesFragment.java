package at.ac.univie.t0306.expensetracker.ui.categories;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import at.ac.univie.t0306.expensetracker.database.data.Category;
import at.ac.univie.t0306.expensetracker.database.data_facade.DataRepo;
import at.ac.univie.t0306.expensetracker.databinding.FragmentCategoriesBinding;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import at.ac.univie.t0306.expensetracker.managers.AccountManager;
import at.ac.univie.t0306.expensetracker.managers.CategoryManager;
import at.ac.univie.t0306.expensetracker.ui.recyclers.AccountRecyclerAdapter;
import at.ac.univie.t0306.expensetracker.ui.recyclers.CategoryRecyclerAdapter;

public class CategoriesFragment extends Fragment {

    private CategoriesViewModel categoriesViewModel;
    private FragmentCategoriesBinding binding;
    private Button btnAddCategory;

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
        categoriesViewModel =
                new ViewModelProvider(this).get(CategoriesViewModel.class);

        binding = FragmentCategoriesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        btnAddCategory = binding.btnAddCategory;

        setUpRecyclerView();
        handleAddCategoryClicked();

        return root;
    }

    /**
     * Handles the event where the add button is clicked
     */
    private void handleAddCategoryClicked() {
        btnAddCategory.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), AddCategoryActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Sets up the recycler view
     */
    private void setUpRecyclerView() {
        RecyclerView rvCategories = binding.rvCategories;
        DataRepo datarepo = new DataRepo(getActivity().getApplication());
        CategoryManager categoryManager = new CategoryManager(datarepo);
        CategoryRecyclerAdapter adapter = new CategoryRecyclerAdapter(getContext(), categoryManager);
        categoriesViewModel.getCategoriesList().observe(getViewLifecycleOwner(), categories -> adapter.setItemList(categories));
        rvCategories.setAdapter(adapter);
        rvCategories.setLayoutManager(new LinearLayoutManager(getContext()));
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