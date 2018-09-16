package com.example.rishabh.loaderdemo;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<List<Employee>> {
    EmployeeAdapter employeeAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView=(ListView)findViewById(R.id.listView);
        listView.setAdapter(employeeAdapter);
        employeeAdapter=new EmployeeAdapter(this,new ArrayList<Employee>());
        getSupportLoaderManager().initLoader(1,null,this).forceLoad();
    }

    @Override
    public Loader<List<Employee>> onCreateLoader(int id, Bundle args) {
        return new EmployeeLoader(MainActivity.this);
    }

    @Override
    public void onLoadFinished(Loader<List<Employee>> loader, List<Employee> data) {
        employeeAdapter.setEmployees(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Employee>> loader) {
        employeeAdapter.setEmployees(new ArrayList<Employee>());
    }
}
