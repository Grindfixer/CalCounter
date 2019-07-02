package com.jwn.calcounter;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import data.DataBaseHandler;
import model.Food;

public class FoodItemDetailsActivity extends AppCompatActivity {

    private TextView foodName, calories, dateTaken;
    private Button shareButton;
    private int foodId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_item_details);

        foodName = findViewById(R.id.detsFoodName);
        calories = findViewById(R.id.detsCaloriesValue);
        dateTaken = findViewById(R.id.detsDateText);
        shareButton = findViewById(R.id.detsShareButton);


        Food food = (Food) getIntent().getSerializableExtra("userObj");

        foodName.setText(food.getFoodName());
        calories.setText(String.valueOf(food.getCalories()));
        dateTaken.setText(food.getRecordDate());

        foodId = food.getFoodId();//this id will be used when we want to delete the item


        calories.setTextSize(34.9f);// float
        calories.setTextColor(Color.RED);

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareCals();
            }
        });

    }//end onCreate

    //setup email message
    public void shareCals() {

        StringBuilder dataString = new StringBuilder();

        String name = foodName.getText().toString();
        String cals = calories.getText().toString();
        String date = dateTaken.getText().toString();

        dataString.append(" Food: " + name + "\n");
        dataString.append(" Calories: " + cals + "\n");
        dataString.append(" Eaten on: " + date);

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");//properly formats the email and limits which apps can use it
        i.putExtra(Intent.EXTRA_SUBJECT, "My Caloric Intake");
        i.putExtra(Intent.EXTRA_EMAIL, new String[] {"recipient@example.com"});
        i.putExtra(Intent.EXTRA_TEXT, dataString.toString());

        try{

            startActivity(Intent.createChooser(i, "Send mail..."));

        }catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "Please install email client before sending",
                    Toast.LENGTH_LONG).show();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_food_item_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.deleteItem) {

        AlertDialog.Builder alert = new AlertDialog.Builder(FoodItemDetailsActivity.this);
            alert.setTitle("Delete?");
            alert.setMessage("Are you sure you want to delete this item?");
            alert.setNegativeButton("No", null);
            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    DataBaseHandler dba = new DataBaseHandler(getApplicationContext());
                    dba.deleteFood(foodId);

                    Toast.makeText(getApplicationContext(), "Food Item Deleted!", Toast.LENGTH_LONG)
                            .show();

                    startActivity(new Intent(FoodItemDetailsActivity.this, DisplayFoodsActivity.class));


                    //remove this activity from activity stack
                    FoodItemDetailsActivity.this.finish();

                }
            });
            alert.show();
        }

        return super.onOptionsItemSelected(item);
    }
}
