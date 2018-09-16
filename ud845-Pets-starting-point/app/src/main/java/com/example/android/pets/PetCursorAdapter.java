package com.example.android.pets;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.pets.DataPackage.PetContract;

/**
 * Created by Rishabh on 10/5/2017.
 */

public class PetCursorAdapter extends CursorAdapter
{

    public PetCursorAdapter(Context context, Cursor c) {
        super(context, c,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {


        return LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView nameTextView=(TextView) view.findViewById(R.id.name);
        TextView summaryTextView=(TextView) view.findViewById(R.id.summary);

        int nameColumn=cursor.getColumnIndex(PetContract.PetEntry.COLUMN_NAME);
        int breedColumn=cursor.getColumnIndex(PetContract.PetEntry.COLUMN_BREED);

        String petName=cursor.getString(nameColumn);
        String breedName=cursor.getString(breedColumn);

        if(TextUtils.isEmpty(breedName)){
            breedName=context.getString(R.string.unknown_breed);
        }

        nameTextView.setText(petName);
        summaryTextView.setText(breedName);

    }
}
