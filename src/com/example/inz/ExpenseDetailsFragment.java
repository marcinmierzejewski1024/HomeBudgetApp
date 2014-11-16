package com.example.inz;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.inz.model.Expense;
import com.example.inz.model.MainData;

import java.sql.SQLException;
import java.util.Date;

/**
 * Created by dom on 16/11/14.
 */
public class ExpenseDetailsFragment extends CommonFragment
{
    public static final String EXPENSE_ID_KEY = "expenseIdkey";

    View rootView;
    LinearLayout detailsContainer;
    Switch editingSwitch;

    boolean editingMode = false;


    Expense expense;
    private EditText nameEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        rootView = inflater.inflate(R.layout.expense_details_fragment, container, false);
        detailsContainer = (LinearLayout) rootView.findViewById(R.id.content);
        editingSwitch = (Switch) rootView.findViewById(R.id.editionMode);
        editingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                editingMode = isChecked;
                updateViews();
            }
        });


        long expenseId = getActivity().getIntent().getLongExtra(EXPENSE_ID_KEY,0);
        loadExpense(expenseId);

        return rootView;
    }

    private void loadExpense(long expenseId)
    {
        MainData dao = new MainData();
        try
        {
            expense = dao.getExpenseData().getExpenseById(expenseId);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        updateViews();
    }

    public void updateViews()
    {
        if(editingMode)
        {
            LayoutInflater layoutInflater = (LayoutInflater)
                    getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            detailsContainer.removeAllViews();
            detailsContainer.addView(layoutInflater.inflate(R.layout.add_fragment,detailsContainer,false), 0);
            nameEditText = (EditText) detailsContainer.findViewById(R.id.nameText);
            nameEditText.setText(expense.getName());

            Button saveButton = (Button) detailsContainer.findViewById(R.id.addButton);
            saveButton.setText(R.string.save);
            saveButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    try
                    {
                        save();
                        Toast.makeText(getActivity(),R.string.changes_saved,Toast.LENGTH_SHORT).show();
                        loadExpense(expense.getExpenseId());
                        editingSwitch.setChecked(false);
                    } catch (SQLException e)
                    {
                        Toast.makeText(getActivity(),R.string.changes_saved_failed,Toast.LENGTH_SHORT).show();
                    }

                }
            });


        }
        else
        {
            LayoutInflater layoutInflater = (LayoutInflater)
                    getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            detailsContainer.removeAllViews();
            detailsContainer.addView(layoutInflater.inflate(R.layout.expense_details,detailsContainer,false), 0);

            TextView name = (TextView) detailsContainer.findViewById(R.id.name);
            TextView category = (TextView) detailsContainer.findViewById(R.id.category);
            TextView cash = (TextView) detailsContainer.findViewById(R.id.cash);

            name.setText(expense.getName());
            cash.setText(expense.getCash().toString());
            category.setText(getString(R.string.category) +":"+ expense.getCategory().getName());
        }
    }

    private void save() throws SQLException
    {
        //expense.setDate(new Date());
        //expense.setCash(ammount);
        //expense.setCategory(category);
        expense.setName(nameEditText.getText().toString());

        MainData dao = new MainData();
        dao.getExpenseData().updateExpense(expense);

    }


}
