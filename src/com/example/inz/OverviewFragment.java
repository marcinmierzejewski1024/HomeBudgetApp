package com.example.inz;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.PopupMenu;
import com.example.inz.common.CommonActivity;
import com.example.inz.common.CommonFragment;

/**
 * Created by dom on 04/11/14.
 */
public class OverviewFragment extends CommonFragment
{
    private static final String TAG = "OverviewFragment";

    private View rootView;


    private enum AddCategories{
        EXPENSE,INCOME

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        rootView = inflater.inflate(R.layout.overview, container, false);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.add_expense,menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.add:
                createAddPopupMenu();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void createAddPopupMenu()
    {
        PopupMenu popup = new PopupMenu(getActivity(), getActivity().findViewById(R.id.add));
        Menu menu = popup.getMenu();


        menu.add(0,AddCategories.INCOME.ordinal(),0,getString(R.string.income));
        menu.add(0,AddCategories.EXPENSE.ordinal(),0,getString(R.string.expense));

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                onAddMenuItemClick(item);
                return true;
            }
        });

        popup.show();
    }

    private void onAddMenuItemClick(MenuItem item)
    {

        if(item.getItemId()==AddCategories.INCOME.ordinal())
        {
            ((CommonActivity)getActivity()).openAddIncome();
        }
        else if(item.getItemId() == AddCategories.EXPENSE.ordinal())
        {
            ((CommonActivity)getActivity()).openAddExpense();

        }

    }
}
