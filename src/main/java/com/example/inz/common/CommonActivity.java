package com.example.inz.common;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.Toast;
import com.example.inz.*;
import com.example.inz.model.Category;
import com.example.inz.model.MainData;

import java.io.Serializable;

/**
 * Created by dom on 16/11/14.
 */
public abstract class CommonActivity extends Activity
{

    private enum AddItems
    {
        EXPENSE,INCOME,CATEGORY

    }

    public abstract Fragment getFragment();


    public void openCategory(Category category)
    {
        Intent i = new Intent(this,DetailActivity.class);
        i.putExtra(DetailActivity.FRAGMENT_KEY,DetailActivity.CATEGORY_FRAGMENT);
        i.putExtra(CategoryFragment.CATEGORY_ID_KEY,category.getCategoryId());
        startActivity(i);
    }
    public void openExpense(long expenseId)
    {
        Intent i = new Intent(this,DetailActivity.class);
        i.putExtra(DetailActivity.FRAGMENT_KEY,DetailActivity.EXPENSE_DETAILS_FRAGMENT);
        i.putExtra(ExpenseDetailsFragment.EXPENSE_ID_KEY, expenseId);
        startActivity(i);
    }
    public void openAddIncome()
    {

        Intent i = new Intent(this,DetailActivity.class);
        i.putExtra(AddFragment.TYPE_OF_ADD_KEY, AddFragment.INCOME);
        i.putExtra(DetailActivity.FRAGMENT_KEY,DetailActivity.ADD_INCOME_FRAGMENT);
        startActivity(i);
    }

    public void openAddExpense()
    {

        Intent i = new Intent(this,DetailActivity.class);

        i.putExtra(AddFragment.TYPE_OF_ADD_KEY, AddFragment.EXPENSE);
        i.putExtra(DetailActivity.FRAGMENT_KEY,DetailActivity.ADD_EXPENSE_FRAGMENT);
        startActivity(i);
    }

    public void openAddCategory()
    {

        Intent i = new Intent(this,DetailActivity.class);

        i.putExtra(DetailActivity.FRAGMENT_KEY,DetailActivity.ADD_CATEGORY_FRAGMENT);
        startActivity(i);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }

    private void createAddPopupMenu()
    {
        PopupMenu popup = new PopupMenu(this, findViewById(R.id.add));
        Menu menu = popup.getMenu();


        menu.add(0, AddItems.INCOME.ordinal(),0,getString(R.string.income));
        menu.add(0, AddItems.EXPENSE.ordinal(),0,getString(R.string.expense));
        menu.add(0,AddItems.CATEGORY.ordinal(),0,getString(R.string.category));


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

        if(item.getItemId() == AddItems.INCOME.ordinal())
        {
            openAddIncome();
        }
        else if(item.getItemId() == AddItems.EXPENSE.ordinal())
        {
            openAddExpense();

        }

        else if(item.getItemId() == AddItems.CATEGORY.ordinal())
        {
            openAddCategory();

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        if(!(getFragment() instanceof AddFragment))
            getMenuInflater().inflate(R.menu.add_expense, menu);

        return super.onCreateOptionsMenu(menu);
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

    public void deleteCategory(final long categoryId)
    {
        CommonQuestionDialog.show(this, getString(R.string.delete_category_question), new CommonQuestionDialogFragment.EventsListener()
        {

            @Override
            public void onQuestionDialogClick(CommonQuestionDialogFragment.Answer answer, Serializable data)
            {
                if (answer == CommonQuestionDialogFragment.Answer.YES)
                {
                    MainData dao = new MainData();
                    boolean result = dao.getCategoryData().deleteCategory(categoryId);
                    if (result)
                    {
                        Toast.makeText(CommonActivity.this, R.string.delete_category_success, Toast.LENGTH_SHORT).show();
                        sendBroadcast(new Intent(MainApp.CHANGE_EXPENSE_BROADCAST));

                    } else
                    {
                        Toast.makeText(CommonActivity.this, R.string.delete_category_failed, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }


}
