package com.example.android.pets.DataPackage;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import static com.example.android.pets.DataPackage.PetContract.CONTENT_AUTHORITY;
import static com.example.android.pets.DataPackage.PetContract.PATH_PETS;

/**
 * Created by Rishabh on 10/2/2017.
 */

public class PetProvider extends ContentProvider {


    public static final String LOG_TAG=PetProvider.class.getSimpleName();

    private PetDb mDbHelper;

    private static final int PETS=100;
    private static final int PET_ID=101;

    private static final UriMatcher sURI_MATCHER=new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURI_MATCHER.addURI(CONTENT_AUTHORITY,PATH_PETS,PETS);
        sURI_MATCHER.addURI(CONTENT_AUTHORITY,PATH_PETS + "/#",PET_ID);
    }


    @Override
    public boolean onCreate() {
        mDbHelper=new PetDb(getContext());
        return true;
    }


    @Override
    public Cursor query(Uri uri,  String[] projection,  String selection,  String[] selectionArgs,  String sortOrder) {
        SQLiteDatabase database=mDbHelper.getReadableDatabase();
        Cursor cursor=null;
        int match=sURI_MATCHER.match(uri);
        switch (match){
            case PETS:
                cursor=database.query(PetContract.PetEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case PET_ID:
                selection = PetContract.PetEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                cursor=database.query(PetContract.PetEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                 break;
            default:
                throw new  IllegalArgumentException("Cannot query with unknown URI"+uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }


    @Override
    public String getType( Uri uri) {
        final int match=sURI_MATCHER.match(uri);
        switch (match){
            case PETS:
                return PetContract.PetEntry.CONTENT_LIST_TYPE;
            case PET_ID:
                return PetContract.PetEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri + " with match " + match);
        }
    }


    @Override
    public Uri insert( Uri uri,  ContentValues values) {
        final int  match=sURI_MATCHER.match(uri);
        switch (match){
            case PETS: return insertPet(uri,values);
            default:
                throw new IllegalArgumentException("Insertion not supported"+uri);
        }


    }

    @Override
    public int delete( Uri uri,  String selection,  String[] selectionArgs) {
        SQLiteDatabase db=mDbHelper.getWritableDatabase();
        int rowsDeleted;

        final int match=sURI_MATCHER.match(uri);

        switch (match) {
            case PETS:
                rowsDeleted= db.delete(PetContract.PetEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case PET_ID:
                selection = PetContract.PetEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted= db.delete(PetContract.PetEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Delete not supported " + uri);

        }
        if(rowsDeleted!=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowsDeleted;
    }

    @Override
    public int update( Uri uri,  ContentValues values,  String selection,  String[] selectionArgs) {
        final int match=sURI_MATCHER.match(uri);

        switch (match){
            case PETS:
                return updatePet(uri,values,selection,selectionArgs);
            case  PET_ID:
                selection= PetContract.PetEntry._ID + "=?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updatePet(uri,values,selection,selectionArgs);
            default:
                throw new IllegalArgumentException("update not supported for "+uri);
        }
    }

    private int updatePet(Uri uri, ContentValues values, String selection,String[] strings) {
        SQLiteDatabase db=mDbHelper.getWritableDatabase();

        if(values.containsKey(PetContract.PetEntry.COLUMN_NAME)){
            String name=values.getAsString(PetContract.PetEntry.COLUMN_NAME);
            if (name==null){
                throw new IllegalArgumentException("Pet requires a name");
            }
        }
        if (values.containsKey(PetContract.PetEntry.COLUMN_GENDER)){
            Integer gender=values.getAsInteger(PetContract.PetEntry.COLUMN_GENDER);
            if (gender==null||!PetContract.PetEntry.isValidGender(gender)){
                throw new IllegalArgumentException("Pet requires a weight");
            }
        }
        if (values.containsKey(PetContract.PetEntry.COLUMN_WEIGHT)){
            Integer weight=values.getAsInteger(PetContract.PetEntry.COLUMN_WEIGHT);
            if (weight!=null&&weight<=0){
                throw new IllegalArgumentException("pet requires weight");
            }
        }
        if (values.size()==0){
            return  0;
        }

        int rowsAffected=db.update(PetContract.PetEntry.TABLE_NAME,values,selection,strings);

        if(rowsAffected!=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }


        return rowsAffected;

    }

    private Uri insertPet(Uri uri,ContentValues values){

        //check for empty name
        String name=values.getAsString(PetContract.PetEntry.COLUMN_NAME);
        if (name.isEmpty()){
            throw new IllegalArgumentException("Pet requires name");
        }
        //check for empty weight
        Integer weight=values.getAsInteger(PetContract.PetEntry.COLUMN_WEIGHT);
        if(weight==null||weight==0){
            throw new IllegalArgumentException("Pet need weight");
        }

        SQLiteDatabase db=mDbHelper.getWritableDatabase();

        long id=db.insert(PetContract.PetEntry.TABLE_NAME,null,values);

        if(id==-1){
            Log.e(LOG_TAG,"Failed to  insert row"+uri);
        }

        getContext().getContentResolver().notifyChange(uri,null);

        return ContentUris.withAppendedId(uri,id);
    }
}
