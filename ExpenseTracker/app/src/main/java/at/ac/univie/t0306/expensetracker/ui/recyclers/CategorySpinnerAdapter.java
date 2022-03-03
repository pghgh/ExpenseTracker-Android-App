package at.ac.univie.t0306.expensetracker.ui.recyclers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import at.ac.univie.t0306.expensetracker.database.data.Category;

public class CategorySpinnerAdapter extends BaseAdapter {


    private Context context;
    private List<Category> categoryList;

    public CategorySpinnerAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<Category> categoryList) {
        this.categoryList = categoryList;
        notifyDataSetChanged();
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    @Override
    public int getCount() {
        return categoryList == null ? 0 : categoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return categoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);

        //       convertView = view.inflate(android.R.layout.simple_spinner_dropdown_item,null);

        ((TextView) view).setText(categoryList.get(position).getName());

        // TextView CategoryView = view.findViewById(android.R.layout.s);
        // TextView accountNameView = view.findViewById(R.id.accountName);

        Category category = categoryList.get(position);

        // accountIdView.setText(account.getId() +"");
        // accountNameView.setText(account.getName());


        return view;
    }
}
