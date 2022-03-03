package at.ac.univie.t0306.expensetracker.ui.categories;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import at.ac.univie.t0306.expensetracker.database.data.Account;
import at.ac.univie.t0306.expensetracker.database.data.Category;
import at.ac.univie.t0306.expensetracker.database.data_facade.DataRepo;
import at.ac.univie.t0306.expensetracker.databinding.ActivityEditCategoryBinding;
import at.ac.univie.t0306.expensetracker.exceptions.InvalidInputDataException;
import at.ac.univie.t0306.expensetracker.managers.CategoryManager;
import at.ac.univie.t0306.expensetracker.ui.recyclers.AccountRecyclerAdapter;
import at.ac.univie.t0306.expensetracker.ui.recyclers.CategoryRecyclerAdapter;

public class EditCategoryActivity extends AppCompatActivity {


    private ActivityEditCategoryBinding binding;
    private EditText etName;
    private Button editBtn;
    CategoryManager manager;
    private Category oldCategory;

    /**
     * {@inheritDoc}
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        manager = new CategoryManager(new DataRepo(getApplication()));
        inflateUI();
        setCategoryData();
        handleEditClicked();

    }

    /**
     * Handles the event where the edit button is clicked.
     */
    private void handleEditClicked() {

        editBtn.setOnClickListener(view -> {
            try {
                checkInputData();
                Category newCategory = new Category(etName.getText().toString());
                newCategory.setId(oldCategory.getId());
                manager.updateCategory(oldCategory, newCategory);
                Toast.makeText(getApplicationContext(), "Category edited", Toast.LENGTH_SHORT).show();
                finish();
            } catch (InvalidInputDataException e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("add category", e.getMessage());
            }

        });
    }

    /**
     * Prepares the category data to be edited
     */
    private void setCategoryData() {
        oldCategory = (Category) getIntent().getSerializableExtra(CategoryRecyclerAdapter.CATEGORY_PARAMETER);
        etName.setText(oldCategory.getName());
    }

    /**
     * Checks input data (if the text which should be given by the user is actually empty)
     *
     * @throws InvalidInputDataException
     */
    private void checkInputData() throws InvalidInputDataException {
        if (TextUtils.isEmpty(etName.getText()))
            throw new InvalidInputDataException("Please fill in all the fields");
    }


    /**
     * Inflates the UI elements from the XML file
     */
    private void inflateUI() {
        etName = binding.etCatName;
        editBtn = binding.editCategoryBtnEditCategory;
        Toolbar toolbar = binding.editCategoryToolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(v -> finish());
    }
}