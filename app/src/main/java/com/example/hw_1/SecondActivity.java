package com.example.hw_1;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {

    private TextView story_title;
    private LinearLayout linearLayout;
    private Button button_createStory;
    private String template_story;
    private String blankField;
    private ArrayList<String> userInputList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);


        // Look up the ids of linear layout and button
        linearLayout = findViewById(R.id.fields_linear_layout);
        button_createStory = findViewById(R.id.generate_story);

        story_title = findViewById(R.id.textView_title); // Find the id of title textview created
        Intent intent = getIntent(); // Retrieve intents from Main activity
        story_title.setText(intent.getStringExtra("title")); // Display title
        story_title.setTextSize(20); // Configure text size of title
        template_story = intent.getStringExtra("story"); // Save the story as string to put in the next intent to convert to JSON array in third activity

        // First convert the string back into the JSONArray, then loop through JSONArray to add EditText/Textviews to linear layout in order to make input fields
        try {
            JSONArray json = new JSONArray(intent.getStringExtra("blanks")); // A way to convert string back into JSON type
            for (int i = 0; i < json.length(); i++) {
                blankField = json.get(i).toString(); // Like an array, get element at index, convert to string
                EditText fieldInput = new EditText(this); // Create the edit text widget for input
                fieldInput.setHint(blankField); // For preference, display hint if nothing has been entered yet
                linearLayout.addView(fieldInput); // Add the edit text field input to linear layout
                TextView wordType = new TextView(this); // Display the word type (i.e noun, verb etc.)
                wordType.setTextColor(Color.parseColor("#ffc0cb")); // Set the color using hex
                wordType.setText(json.get(i).toString()); // Convert JSON element to string
                wordType.setPadding(0, 0, 0, 10); // Format padding in Java, since text views are dynamically created
                linearLayout.addView(wordType); // Add text view to linear layout, underneath the edit text widget
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // add button to generate story in third activity
        button_createStory.setOnClickListener(new View.OnClickListener() { // 1st method - Implementation in Java
            @Override
            public void onClick(View v) {
                launchNextActivity(v);
            }
        });

    }

    public void launchNextActivity(View view) {
        addUserInputs();
        checkIfFieldsEmpty();
        if (checkIfFieldsEmpty() == true) {
            missingInputMessage(view);
        }
        else {
            Intent intent = new Intent(this, ThirdActivity.class);
            intent.putExtra("story", template_story);
            // Pack userInputList with filled in fields
            intent.putExtra("blanks", userInputList);
            startActivity(intent);
        }


    }

    public void addUserInputs() { // Add the inputs of user from each edit text field - can include blank responses - to array list of user inputs
        userInputList = new ArrayList<>();
        for (int i = 0; i < linearLayout.getChildCount(); i+=2) {
            EditText inputFilled = (EditText) linearLayout.getChildAt(i);
            String input = inputFilled.getText().toString().trim();
            userInputList.add(input);
        }
    }

    public boolean checkIfFieldsEmpty(){ // Method to check for missing edit text fields
        for (int i = 0; i < linearLayout.getChildCount(); i+=2) {
            EditText inputFilled = (EditText) linearLayout.getChildAt(i);
            String input = inputFilled.getText().toString().trim(); // For preference, it would be easier to delete whitespace after input
            if (input.equals(""))
                return true;
        }
        return false;
    }

    public void missingInputMessage(View view) {
        Toast missingInput = Toast.makeText(this, "missing input(s)", Toast.LENGTH_SHORT);
        missingInput.show();
    }

}
