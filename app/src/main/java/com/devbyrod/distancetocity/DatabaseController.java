package com.devbyrod.distancetocity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.sql.SQLException;

/**
 * Created by Rodrigo on 10/31/2014.
 */
public class DatabaseController{

    public DatabaseHelper mDatabaseHelper;

    public DatabaseController( Context context ){

        mDatabaseHelper = new DatabaseHelper( context );
    }

    public boolean insertQueryResult( String query, String result ){

        SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.DB_TABLE_RESULTS_COL_CITY, query);
        contentValues.put( Constants.DB_TABLE_RESULTS_COL_RESULT, result );

        if( this.recordExists( query ) ){

            String[] args = {query};

            return ( database.update( Constants.DB_TABLE_RESULTS, contentValues, Constants.DB_TABLE_RESULTS_COL_CITY + "=?", args ) != -1 );
        }

        return ( database.insert( Constants.DB_TABLE_RESULTS, null, contentValues ) != -1 );
    }

    public String getSavedQueryResult( String query ){

        SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();

        String q = "SELECT " + Constants.DB_TABLE_RESULTS_COL_RESULT + " FROM " +
                Constants.DB_TABLE_RESULTS + " WHERE " + Constants.DB_TABLE_RESULTS_COL_CITY + "=?;";

        Cursor cursor = database.rawQuery( q, new String[]{query} );

        String result = "";

        while( cursor.moveToNext() ) {

            result = cursor.getString(cursor.getColumnIndex(Constants.DB_TABLE_RESULTS_COL_RESULT));
            break;   //just the first result, thank you :)
        }

        return result == null ? "" : result;

        //return "{'meta':{'code':200},'response':{venues:[{'name':'Rigos place','location':{'lat':10.00236,'lng':-84.11651,'formattedAddress':['Heredia','Costa Rica']},'categories':[{'name':'some category'}]}]}}";
    }

    public boolean recordExists( String query ){

        SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();

        String q = "SELECT " + Constants.DB_TABLE_RESULTS_COL_ID + " FROM " +
                Constants.DB_TABLE_RESULTS + " WHERE " + Constants.DB_TABLE_RESULTS_COL_CITY + "=?;";

        Cursor cursor = database.rawQuery( q, new String[]{query} );

        boolean result = cursor.getCount() > 0;

        cursor.close();

        return result;
    }

    private class DatabaseHelper extends SQLiteOpenHelper {

        private Context mContext;

        public DatabaseHelper(Context context) {
            super( context, Constants.DB_NAME, null, Constants.DB_VERSION );

            this.mContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL( "CREATE TABLE " + Constants.DB_TABLE_RESULTS + " (" +
                    Constants.DB_TABLE_RESULTS_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Constants.DB_TABLE_RESULTS_COL_CITY + " TEXT, " +
                    Constants.DB_TABLE_RESULTS_COL_RESULT + " TEXT);");
            Toast.makeText( mContext, "Database onCreate()", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL( "DROP TABLE IF EXISTS " + Constants.DB_TABLE_RESULTS + ";" );
            this.onCreate( db );
            Toast.makeText( mContext, "Database onUpdate()", Toast.LENGTH_LONG).show();
        }
    }
}
