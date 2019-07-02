package com.jwn.calcounter;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import data.CustomListViewAdapter;
import data.DataBaseHandler;
import model.Food;
import util.Utils;

public class DisplayFoodsActivity extends AppCompatActivity {

    private DataBaseHandler dba;
    private ArrayList<Food> dbFoods = new ArrayList<>();
    private  CustomListViewAdapter foodAdapter;
    private ListView listView;

    private Food myFood;
    private TextView totalCals, totalFoods;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_foods);

        listView = findViewById(R.id.list);
        totalCals = findViewById(R.id.totalAmountTextView);
        totalFoods = findViewById(R.id.totalItemsTextView);

        refreshData();
    }

    private void refreshData() {
        dbFoods.clear();

        dba = new DataBaseHandler(getApplicationContext());

        ArrayList<Food> foodsFromDB = dba.getFoods();

        int calsValue = dba.totalCalories();
        int totalItems = dba.getTotalItems();

        String formattedValue = Utils.formatNumber(calsValue);
        String formattedItems = Utils.formatNumber(totalItems);

        totalCals.setText("Total Calories: " + formattedValue);
        totalFoods.setText("Total Foods: " + formattedItems);

        for (int i = 0; i < foodsFromDB.size(); i++){

            String name = foodsFromDB.get(i).getFoodName();
            String dateText = foodsFromDB.get(i).getRecordDate();
            int cals = foodsFromDB.get(i).getCalories();
            int foodId = foodsFromDB.get(i).getFoodId();

            Log.v("FOOD IDS: ", String.valueOf(foodId));


            myFood = new Food();
            myFood.setFoodName(name);
            myFood.setRecordDate(dateText);
            myFood.setCalories(cals);
            myFood.setFoodId(foodId);

            dbFoods.add(myFood);

        }
        dba.close();

        //create adapter
        foodAdapter = new CustomListViewAdapter(DisplayFoodsActivity.this, R.layout.list_row, dbFoods);
        listView.setAdapter(foodAdapter);
        foodAdapter.notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_foods, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
