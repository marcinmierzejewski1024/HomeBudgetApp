package com.example.inz;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by dom on 05/11/14.
 */
public class DetailActivity extends Activity
{
    public static final String CATEGORY_FRAGMENT = "categoryFragment";
    public static final String EXPENSE_DETAILS_FRAGMENT = "expenseDetailsFragment";
    public static final String ADD_INCOME_FRAGMENT = "addincomeFragment";
    public static final String ADD_EXPENSE_FRAGMENT = "addExpenseFragment";




    public static final String TAG = "DetailActivity";
    public static final String FRAGMENT_KEY = "fragmentKey";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        getActionBar().setDisplayHomeAsUpEnabled(true);
        //getActionBar().setIcon(R.drawable.ic_launcher);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        try
        {
            String fragmentToShow = getIntent().getStringExtra(FRAGMENT_KEY);
            if(fragmentToShow.equals(CATEGORY_FRAGMENT))
            {
                setFragment(new CategoryFragment());
            }
            else if(fragmentToShow.equals(ADD_EXPENSE_FRAGMENT))
            {
                setFragment(new AddFragment());
            }
        } catch (Exception e)
        {

            Log.e(TAG, "cos sie popsulo", e);
        }
    }


    public void setFragment(Fragment fragment)
    {
        if (fragment != null)
        {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();
        }
    }
}
