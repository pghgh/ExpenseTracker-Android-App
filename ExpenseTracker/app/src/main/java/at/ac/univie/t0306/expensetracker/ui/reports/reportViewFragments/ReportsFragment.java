package at.ac.univie.t0306.expensetracker.ui.reports.reportViewFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Arrays;

import at.ac.univie.t0306.expensetracker.R;
import at.ac.univie.t0306.expensetracker.databinding.FragmentReportsBinding;

/**
 * ReportsFragment support three tabs with one fragment each (TransactionListFragment, LineGraphFragment and BarGraphFragment)
 */
public class ReportsFragment extends Fragment {

    private FragmentReportsBinding binding;
    private TabLayout tabLayout;
    private ViewPager2 pager2;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentReportsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        bindUIElements();
        setTabLayout();


        return root;
    }

    private void setTabLayout() {
        ReportsViewPagerAdapter pagerAdapter = new ReportsViewPagerAdapter(getParentFragmentManager(), getLifecycle(),
                new ArrayList<>(Arrays.asList(TransactionListFragment.newInstance(), LineGraphFragment.newInstance(), BarGraphFragment.newInstance())));
        pager2.setAdapter(pagerAdapter);

        tabLayout.addTab(tabLayout.newTab().setText(R.string.list));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.line_graph));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.bar_graph));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        pager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

    }

    private void bindUIElements() {
        this.tabLayout = this.binding.reportsTab;
        this.pager2 = this.binding.reportsPager;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}