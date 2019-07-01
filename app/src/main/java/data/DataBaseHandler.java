package data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.SyncStateContract;

import java.util.ArrayList;

import model.Food;

public class DataBaseHandler extends SQLiteOpenHelper {
    private final ArrayList<Food> foodList = new ArrayList<>();


    public DataBaseHandler(Context context) {
        super(context, Constants.DATABASE_NAME,null, Constants.DATABASE_VERSION );
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //create table
        String  CREATE_TABLE = "CREATE TABLE" + Constants.TABLE_NAME + "("
                 + Constants.KEY_ID + " INTEGER PRIMARY KEY, " + Constants.FOOD_NAME + " TEXT, "
                 + Constants.FOOD_CALORIES_NAME + " INT, " + Constants.DATE_NAME + " LONG):";

        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME );

            //create new table
        onCreate(sqLiteDatabase);
    }

    //Get total items saved
    public int getTotalItems() {
        int totalItems = 0;

        String query = "SELECT * FROM " + Constants.TABLE_NAME;
        SQLiteDatabase dba = this.getReadableDatabase();
        Cursor cursor = dba.rawQuery(query, null);

        totalItems = cursor.getCount();

        cursor.close();

        return totalItems;
    }

    //get total calories consumed
    public int totalCalories() {
        int cals = 0;

        SQLiteDatabase dba = this.getReadableDatabase();

        String query = "SELECT SUM( " + Constants.FOOD_CALORIES_NAME + ") " +
                        "FROM " + Constants.TABLE_NAME;

        Cursor cursor = dba.rawQuery(query,null);

        if (cursor.moveToFirst()) {
            cals = cursor.getInt(0);
        }
            cursor.close();
            dba.close();

        return cals;
    }

    //delete food item
    public void deleteFood(int id) {

        SQLiteDatabase dba = this.getWritableDatabase();
        dba.delete(Constants.TABLE_NAME, Constants.KEY_ID + " =?"),
                    new String[]{ String.valueOf(id)};

        dba.close();
    }


}//end DatabaseHandler
