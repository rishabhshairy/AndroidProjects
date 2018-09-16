package com.example.rishabh.loaderdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rishabh on 1/17/2018.
 */

public class EmployeeAdapter extends BaseAdapter {

    public LayoutInflater layoutInflater;
    private List<Employee> employeeList =new ArrayList<Employee>();

    public void setEmployees(List<Employee> data)
    {
        employeeList.addAll(data);
        notifyDataSetChanged();
    }

    public EmployeeAdapter(Context context,List<Employee> employees)
    {
        this.employeeList =employees;
        layoutInflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return employeeList.size();
    }

    @Override
    public Object getItem(int i) {
        return employeeList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Employee emp=(Employee)getItem(i);
        if (view==null)
        {
            view=layoutInflater.inflate(R.layout.employee_data,null);
        }

        TextView empId=(TextView)view.findViewById(R.id.empID);
        empId.setText(emp.empid);
        TextView empName=(TextView)view.findViewById(R.id.name);
        empName.setText(emp.empName);


        return view;
    }
}
