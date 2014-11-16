package com.example.inz;

import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.example.inz.model.*;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Created by dom on 05/11/14.
 */
public class AddFragment extends CommonFragment
{
    View rootView;
    Spinner categorySpinner;
    Spinner currencySpinner;
    SeekBar cashAmmountSeekBar;
    TextView cashAmmountTextView;
    EditText nameEditText;
    Button addButton;


    CashAmmount ammount = new CashAmmount(0,Currency.values()[0]);
    Category category = null;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        getActivity().getActionBar().setIcon(R.drawable.ic_home);
        getActivity().invalidateOptionsMenu();
        setHasOptionsMenu(true);
        rootView = inflater.inflate(R.layout.add_fragment,container,false);
        cashAmmountTextView = (TextView) rootView.findViewById(R.id.cashAmmountTextView);
        nameEditText = (EditText) rootView.findViewById(R.id.nameText);
        addButton = (Button) rootView.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                addExpense();

            }
        });


        prepareCategoriesList();
        prepareCurrenciesList();
        prepareCashAmmountSeekBar();

        updateViews();
        return rootView;
    }



    private void prepareCategoriesList()
    {

        try
        {

            final List<Category> categories = new MainData().getCategoryData().getCategories();

            final String categoriesArray[] = new String[categories.size()];

            int i = 0;
            for(Category single: categories)
            {
                categoriesArray[i++] = single.getName();
            }

            categorySpinner = (Spinner) rootView.findViewById(R.id.categorySpinner);
            ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(getActivity(),
            android.R.layout.simple_spinner_dropdown_item,categoriesArray);
            categorySpinner.setAdapter(spinnerArrayAdapter);
            categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
            {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                {
                    category = categories.get(position);
                }

                  @Override
                  public void onNothingSelected(AdapterView<?> parent)
                  {

                  }
          });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private void prepareCurrenciesList()
    {

        currencySpinner = (Spinner) rootView.findViewById(R.id.currencySpinner);
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, Currency.values());
        currencySpinner.setAdapter(spinnerArrayAdapter);
        currencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                ammount.setCurrency(Currency.values()[position]);
                updateViews();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

    }

    private void prepareCashAmmountSeekBar()
    {

        cashAmmountSeekBar = (SeekBar) rootView.findViewById(R.id.cashAmmountSeekBar);
        cashAmmountSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {

                int pennies = progress * 100;

                if(progress>100)
                {
                    pennies = (progress-50) * 200;
                }
                if(progress>200)
                {
                    pennies = (progress-140) * 500;
                }
                if(progress>300)
                {
                    pennies = (progress-220) * 1000;
                }
                Log.wtf("tag","progress "+progress + "pennies"+pennies);


                ammount.setPennies(pennies);
                updateViews();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });

    }

    private void updateViews()
    {
        cashAmmountTextView.setText(ammount.toString());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == android.R.id.home)
            getActivity().onBackPressed();

        return super.onOptionsItemSelected(item);
    }

    public void addExpense()
    {
        if(ammount.isZero())
        {
            Toast.makeText(getActivity(), R.string.cash_ammount_is_zero, Toast.LENGTH_SHORT).show();
            return;
        }

        Expense newExpense = new Expense();
        newExpense.setDate(new Date());
        newExpense.setCash(ammount);
        newExpense.setCategory(category);
        newExpense.setName(nameEditText.getText().toString());

        try
        {
            MainData dao = new MainData();
            dao.getExpenseData().storeExpense(newExpense);
            Toast.makeText(getActivity().getApplication(), R.string.add_success,Toast.LENGTH_LONG).show();

            getActivity().finish();
        }
        catch (Exception e)
        {
            Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}