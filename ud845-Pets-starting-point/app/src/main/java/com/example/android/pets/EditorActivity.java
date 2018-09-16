/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.pets;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.pets.DataPackage.PetContract;
import com.example.android.pets.DataPackage.PetDb;

/**
 * Allows user to create a new pet or edit an existing one.
 */
public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /** EditText field to enter the pet's name */
    private EditText mNameEditText;

    /** EditText field to enter the pet's breed */
    private EditText mBreedEditText;

    /** EditText field to enter the pet's weight */
    private EditText mWeightEditText;

    /** EditText field to enter the pet's gender */
    private Spinner mGenderSpinner;

    private PetDb mDbHelper;
    /**
     * Gender of the pet. The possible values are:
     * 0 for unknown gender, 1 for male, 2 for female.
     */
    private int mGender = 0;

    private Uri mCurrrentpetUri;

    private static final int EXISTING_PET_LOADER=0;

    private boolean mPetChanged=false;

    private View.OnTouchListener mTouchListener=new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mPetChanged=true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Find all relevant views that we will need to read user input from
        mNameEditText = (EditText) findViewById(R.id.edit_pet_name);
        mBreedEditText = (EditText) findViewById(R.id.edit_pet_breed);
        mWeightEditText = (EditText) findViewById(R.id.edit_pet_weight);
        mGenderSpinner = (Spinner) findViewById(R.id.spinner_gender);
        mDbHelper=new PetDb(this);
        setupSpinner();

        mNameEditText.setOnTouchListener(mTouchListener);
        mBreedEditText.setOnTouchListener(mTouchListener);
        mWeightEditText.setOnTouchListener(mTouchListener);
        mGenderSpinner.setOnTouchListener(mTouchListener);

        Intent intent=getIntent();
        mCurrrentpetUri=intent.getData();

        if(mCurrrentpetUri==null){
            setTitle(getString(R.string.editor_activity_title_new_pet));
            invalidateOptionsMenu();
        }

        else {
            setTitle(getString(R.string.editor_activity_title_edit_pet));
            //initialize the loader
            getLoaderManager().initLoader(EXISTING_PET_LOADER,null,this);
        }


    }

    /**
     * Setup the dropdown spinner that allows the user to select the gender of the pet.
     */
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mGenderSpinner.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.gender_male))) {
                        mGender = PetContract.PetEntry.male; // Male
                    } else if (selection.equals(getString(R.string.gender_female))) {
                        mGender = PetContract.PetEntry.female; // Female
                    } else {
                        mGender = PetContract.PetEntry.unknown; // Unknown
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGender = 0; // Unknown
            }
        });
    }

    private void savePet(){
        String nameString=mNameEditText.getText().toString().trim();
        String breedName=mBreedEditText.getText().toString().trim();
        String weightString=mWeightEditText.getText().toString().trim();
        //int weight=Integer.parseInt(mWeightEditText.getText().toString().trim());
        //insert data from editor

        //check for new pet
        if(mCurrrentpetUri==null&&TextUtils.isEmpty(nameString)&&
                TextUtils.isEmpty(breedName)&&TextUtils.isEmpty(weightString)&&
                mGender== PetContract.PetEntry.unknown){
            return;

        }

        ContentValues values=new ContentValues();
        values.put(PetContract.PetEntry.COLUMN_NAME,nameString);
        values.put(PetContract.PetEntry.COLUMN_BREED,breedName);
        values.put(PetContract.PetEntry.COLUMN_GENDER, mGender);
        int weight=0;
        if(!TextUtils.isEmpty(weightString)){
            weight=Integer.parseInt(weightString);
        }
        values.put(PetContract.PetEntry.COLUMN_WEIGHT,weight);

       if (mCurrrentpetUri==null){
           Uri newUri=getContentResolver().insert(PetContract.PetEntry.CONTENT_URI,values);

           if (newUri==null){
               Toast.makeText(this, "Pet Entry failed", Toast.LENGTH_SHORT).show();
           }
           else {
               Toast.makeText(this, "Pet Entry Successfull", Toast.LENGTH_SHORT).show();
           }
       }
       else{
           //existing pets are there
           int rowsAffected=getContentResolver().update(mCurrrentpetUri,values,null,null);


           if (rowsAffected==0){
               Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
           }
           else {
               Toast.makeText(this, "Update successfull", Toast.LENGTH_SHORT).show();
           }
       }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Do nothing for now
                savePet();
                finish();

                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Do nothing for now
                showDeleteConfirmationDialog();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
               if (!mPetChanged){
                   NavUtils.navigateUpFromSameTask(this);
                   return true;
               }
                DialogInterface.OnClickListener discardButtonClickListener =new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    }
                };
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection={PetContract.PetEntry._ID, PetContract.PetEntry.COLUMN_NAME,
                PetContract.PetEntry.COLUMN_BREED, PetContract.PetEntry.COLUMN_GENDER, PetContract.PetEntry.COLUMN_WEIGHT};


        return new CursorLoader(this,mCurrrentpetUri,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if(cursor==null||cursor.getCount()<1){
            return;
        }

        if (cursor.moveToFirst()){
            int nameColumn=cursor.getColumnIndex(PetContract.PetEntry.COLUMN_NAME);
            int breedColumn=cursor.getColumnIndex(PetContract.PetEntry.COLUMN_BREED);
            int genderColumn=cursor.getColumnIndex(PetContract.PetEntry.COLUMN_GENDER);
            int weightColumn=cursor.getColumnIndex(PetContract.PetEntry.COLUMN_WEIGHT);

            String name=cursor.getString(nameColumn);
            String breed=cursor.getString(breedColumn);
            int gender=cursor.getInt(genderColumn);
            int weight=cursor.getInt(weightColumn);

            mNameEditText.setText(name);
            mBreedEditText.setText(breed);
            mWeightEditText.setText(Integer.toString(weight));
            switch (gender){
                case PetContract.PetEntry.male:
                    mGenderSpinner.setSelection(1);
                    break;
                case PetContract.PetEntry.female:
                    mGenderSpinner.setSelection(2);
                    break;
                default:
                    mGenderSpinner.setSelection(0);
                    break;
            }

        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNameEditText.setText("");
        mBreedEditText.setText("");
        mWeightEditText.setText("");
        mGenderSpinner.setSelection(0);

    }

    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener){

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard,discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    if(dialog!=null){
                        dialog.dismiss();
                    }
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        if(!mPetChanged) {

            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardButtonClickListener=new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        };

        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
         super.onPrepareOptionsMenu(menu);
        if (mCurrrentpetUri==null){
            MenuItem menuItem=menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);

        }
        return true;
    }
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deletePet();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Perform the deletion of the pet in the database.
     */
    private void deletePet() {
        // TODO: Implement this method
        if (mCurrrentpetUri!=null){
            int rowsAffected=getContentResolver().delete(mCurrrentpetUri,null,null);
            if (rowsAffected==0){
                Toast.makeText(this, getString(R.string.editor_delete_pet_failed),
                        Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(this, getString(R.string.editor_delete_pet_successful),
                        Toast.LENGTH_LONG).show();
            }
        }
        finish();
    }
}