package com.example.inz;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.example.inz.CommonFragment;
import com.example.inz.R;

/**
 * Created by dom on 05/11/14.
 */
public class AddFragment extends CommonFragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        getActivity().getActionBar().setIcon(R.drawable.ic_launcher);
        getActivity().invalidateOptionsMenu();
        //setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.add_fragment,container,false);
        return rootView;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == android.R.id.home)
            getActivity().onBackPressed();

        return super.onOptionsItemSelected(item);
    }
}