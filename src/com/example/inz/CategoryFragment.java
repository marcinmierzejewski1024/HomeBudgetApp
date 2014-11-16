package com.example.inz;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.example.inz.model.Category;
import com.example.inz.model.Expense;
import com.example.inz.model.MainData;

import java.sql.SQLException;
import java.util.List;
import java.util.SimpleTimeZone;

/**
 * Created by dom on 11/11/14.
 */
public class CategoryFragment extends CommonFragment
{
    public static final String CATEGORY_ID_KEY = "categoryIdKey";


    View rootView;
    TextView categoryName;
    ListView categoryExpenses;

    Category data;
    List<Expense> expenses;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        setHasOptionsMenu(true);
        rootView = inflater.inflate(R.layout.category_fragment, container, false);
        categoryName = (TextView) rootView.findViewById(R.id.categoryName);
        categoryExpenses = (ListView) rootView.findViewById(R.id.categoryExpenses);

        Long categoryId = getActivity().getIntent().getExtras().getLong(CATEGORY_ID_KEY);
        loadData(categoryId);
        updateViews();

        return rootView;
    }

    private void updateViews()
    {


        categoryName.setText(data.getName());
        categoryName.setBackground(new ColorDrawable(Color.parseColor(data.getHexColor())));

        Expense[] arr = {};
        categoryExpenses.setAdapter(new ExpenseAdapter(getActivity(), expenses.toArray(arr)));
        categoryExpenses.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Long expenseId = (Long) view.getTag();
                ((CommonActivity)getActivity()).openExpense(expenseId);
            }
        });
    }

    public void loadData(Long categoryId)
    {
        MainData dao = new MainData();
        try
        {
            data = dao.getCategoryData().getCategoryById(categoryId);
            expenses = dao.getExpenseData().getExpensesByCategoryId(categoryId);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
