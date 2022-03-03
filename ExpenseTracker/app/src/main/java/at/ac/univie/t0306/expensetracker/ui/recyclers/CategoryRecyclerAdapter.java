package at.ac.univie.t0306.expensetracker.ui.recyclers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import at.ac.univie.t0306.expensetracker.R;
import at.ac.univie.t0306.expensetracker.database.data.Account;
import at.ac.univie.t0306.expensetracker.database.data.Category;
import at.ac.univie.t0306.expensetracker.managers.AccountManager;
import at.ac.univie.t0306.expensetracker.managers.CategoryManager;
import at.ac.univie.t0306.expensetracker.ui.accounts.EditAccountActivity;
import at.ac.univie.t0306.expensetracker.ui.categories.EditCategoryActivity;

// CODE TAKEN FROM START <1>

/*
Ideas about implementing a RecyclerAdapter were used from the following source: Android Documentation
Source link: https://developer.android.com/guide/topics/ui/layout/recyclerview
*/

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.ItemViewHolder> {


    private List<Category> itemList = new ArrayList<>();
    private Context context;
    private CategoryManager categoryManager;
    public static final String CATEGORY_PARAMETER = "category";

    public CategoryRecyclerAdapter(Context context, CategoryManager categoryManager) {
        this.context = context;
        this.categoryManager = categoryManager;
    }

    public void setItemList(List<Category> itemList) {
        Log.i("data_refreshed", itemList.toString());
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        Category category;
        private TextView nameView;
        private ImageButton btnMoreOptionsCategory;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            inflateViews(itemView);
            setOnMoreOptionsClicked(btnMoreOptionsCategory, categoryManager);
        }

        private void inflateViews(@NonNull View itemView) {
            nameView = itemView.findViewById(R.id.tvItemCategoryTitle);
            btnMoreOptionsCategory = itemView.findViewById(R.id.btnMoreOptionsCategory);
        }


        private void setOnMoreOptionsClicked(@NonNull ImageButton btnMoreOptionsCategory, CategoryManager categoryManager) {
            btnMoreOptionsCategory.setOnClickListener((view) -> {
                PopupMenu popup = new PopupMenu(context, btnMoreOptionsCategory);
                popup.inflate(R.menu.category_options_menu);
                popup.setOnMenuItemClickListener((item) -> {
                            switch (item.getItemId()) {
                                case R.id.category_delete:
                                    categoryManager.deleteCategory(category);
                                    Toast.makeText(context, "Category deleted", Toast.LENGTH_SHORT).show();
                                    return true;
                                case R.id.category_update:
                                    doChangeCategoryOperation(category);
                                    return true;
                                default:
                                    return false;
                            }
                        }
                );
                popup.show();

            });
        }


        private void doChangeCategoryOperation(Category category) {
            Intent intent = new Intent(context, EditCategoryActivity.class);
            intent.putExtra(CATEGORY_PARAMETER, category);
            context.startActivity(intent);

        }


        public void bind(Category category) {
            this.category = category;
            nameView.setText(category.getName());

        }

    }

    @NonNull
    @Override
    public CategoryRecyclerAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_category_item, parent, false);
        return new CategoryRecyclerAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryRecyclerAdapter.ItemViewHolder holder, int position) {
        holder.bind(itemList.get(position));

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

}

// CODE TAKEN FROM END <1>
