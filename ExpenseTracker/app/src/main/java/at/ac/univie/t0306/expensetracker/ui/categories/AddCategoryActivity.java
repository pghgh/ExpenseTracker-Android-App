package at.ac.univie.t0306.expensetracker.ui.categories;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.time.OffsetDateTime;

import at.ac.univie.t0306.expensetracker.database.data.Account;
import at.ac.univie.t0306.expensetracker.database.data.Category;
import at.ac.univie.t0306.expensetracker.database.data.EAccountType;
import at.ac.univie.t0306.expensetracker.database.data_facade.DataRepo;
import at.ac.univie.t0306.expensetracker.databinding.ActivityAddCategoryBinding;
import at.ac.univie.t0306.expensetracker.exceptions.InvalidInputDataException;
import at.ac.univie.t0306.expensetracker.managers.CategoryManager;

public class AddCategoryActivity extends AppCompatActivity {

    private ActivityAddCategoryBinding binding;
    private EditText etName;
    private Button addBtn;

    CategoryManager categoryManager;

    /**
     * {@inheritDoc}
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        categoryManager = new CategoryManager(new DataRepo(getApplication()));
        inflateUI();
        handleAddCategoryBtnClicked();
    }

    /**
     * Handles the event where the add button is clicked.
     */
    private void handleAddCategoryBtnClicked() {
        addBtn.setOnClickListener((view) -> {
            try {
                checkInputData();
                Category category = new Category(etName.getText().toString());
                categoryManager.addCategory(category);
                Toast.makeText(getApplicationContext(), "Category created", Toast.LENGTH_SHORT).show();
                finish();
            } catch (InvalidInputDataException e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("add category", e.getMessage());
            }
        });
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
        etName = binding.etName;
        addBtn = binding.addCategoryBtnAddCategory;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Toolbar toolbar = binding.addCategoryToolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(v -> finish());

    }

}
