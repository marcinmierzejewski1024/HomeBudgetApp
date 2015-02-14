package com.example.inz;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.inz.common.CommonQuestionDialog;
import com.example.inz.common.CommonQuestionDialogFragment;
import com.example.inz.model.Expense;
import com.example.inz.model.MainData;

import java.io.Serializable;
import java.sql.SQLException;

/**
 * Created by dom on 16/11/14.
 */
public class ExpenseDetailsFragment extends AddFragment
{
    public static final String EXPENSE_ID_KEY = "expenseIdkey";

    LinearLayout detailsContainer;
    Switch editingSwitch;
    boolean editingMode = false;
    Expense expense;

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

    }

    public void updateViews()
    {
        if(expense!=null)
            loadExpense(expense.getExpenseId());

        if(editingMode)
        {
            LayoutInflater layoutInflater = (LayoutInflater)
                    getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            detailsContainer.removeAllViews();
            detailsContainer.addView(layoutInflater.inflate(R.layout.add_fragment,detailsContainer,false), 0);

            prepareAddingLayout(detailsContainer);


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
                    }
                    catch (SQLException e)
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

            Button deleteButton = (Button) detailsContainer.findViewById(R.id.deleteButton);
            deleteButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    delete();
                }
            });

            name.setText(expense.getName());

            if(expense.getCash()!=null)
                cash.setText(expense.getCash().toString());

            category.setText(getString(R.string.category) +":"+ expense.getCategory());
        }
    }

    private void save() throws SQLException
    {

        expense.setCash(ammount);
        expense.setCategory(category);
        expense.setName(nameEditText.getText().toString());

        MainData dao = new MainData();
        dao.getExpenseData().updateExpense(expense);

    }

    private void delete()
    {

        CommonQuestionDialog.show(getActivity(),getString(R.string.delete_expense_question),new CommonQuestionDialogFragment.EventsListener(){

            @Override
            public void onQuestionDialogClick(CommonQuestionDialogFragment.Answer answer, Serializable data)
            {
                if(answer == CommonQuestionDialogFragment.Answer.YES)
                {
                    MainData dao = new MainData();
                    boolean result = dao.getExpenseData().deleteExpense(expense.getExpenseId());
                    if(result)
                    {
                        Toast.makeText(getActivity(),R.string.delete_expense_success,Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    }
                    else
                    {
                        Toast.makeText(getActivity(),R.string.delete_expense_failed,Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }

}
