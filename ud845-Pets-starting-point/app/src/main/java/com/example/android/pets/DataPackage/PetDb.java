package com.example.android.pets.DataPackage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.pets.DataPackage.PetContract.PetEntry;

import static java.sql.Types.INTEGER;

/**
 * Created by Rishabh on 9/28/2017.
 */

public  class PetDb extends SQLiteOpenHelper{
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "PetDetail.db";

   public  PetDb(Context context){
       super(context,DATABASE_NAME,null,DATABASE_VERSION);

   }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE="CREATE TABLE " + PetEntry.TABLE_NAME + " ("+
                PetEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PetEntry.COLUMN_NAME + " TEXT NOT NULL, "+
                PetEntry.COLUMN_BREED + " TEXT, "+
                PetEntry.COLUMN_GENDER + " INTEGER NOT NULL, "+
                PetEntry.COLUMN_WEIGHT + " INTEGER NOT NULL DEFAULT 0);";
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String SQL_DELETE="DROP TABLE IF EXISTS" + PetEntry.TABLE_NAME;
        db.execSQL(SQL_DELETE);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String SQL_DELETE="DROP TABLE IF EXISTS" + PetEntry.TABLE_NAME;
        db.execSQL(SQL_DELETE);
        onCreate(db);
    }
}
