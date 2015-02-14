package com.example.inz.common;

import android.app.Activity;
import android.content.Intent;
import com.example.inz.AddFragment;
import com.example.inz.CategoryFragment;
import com.example.inz.DetailActivity;
import com.example.inz.ExpenseDetailsFragment;

/**
 * Created by dom on 16/11/14.
 */
public abstract class CommonActivity extends Activity
{
    public void openCategory(long categoryId)
    {
        Intent i = new Intent(this,DetailActivity.class);
        i.putExtra(DetailActivity.FRAGMENT_KEY,DetailActivity.CATEGORY_FRAGMENT);
        i.putExtra(CategoryFragment.CATEGORY_ID_KEY,categoryId);
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

}
