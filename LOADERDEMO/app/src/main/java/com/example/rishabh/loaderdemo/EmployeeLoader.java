package com.example.rishabh.loaderdemo;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rishabh on 1/17/2018.
 */

public class EmployeeLoader extends AsyncTaskLoader<List<Employee>> {

    public EmployeeLoader(Context context) {
        super(context);
    }

    @Override
    public List<Employee> loadInBackground() {
        List<Employee> list=new ArrayList<Employee>();
        list.add(new Employee("1","Rishabh"));
        list.add(new Employee("2","Sourav"));
        list.add(new Employee("3","Ayush"));
        return list;
    }
}
