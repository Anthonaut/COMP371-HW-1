package com.example.hw_1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class ThirdActivity extends AppCompatActivity {
    private TextView texView_story; // The text view for displaying the mad libs story
    private Button homeButton; // Button to go back to main screen

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        // Look up id for the story textview in third activity
        texView_story = findViewById(R.id.full_story);
        homeButton = findViewById(R.id.home_button);

        Intent intent = getIntent();
        try {
            JSONArray storyJSONArray = new JSONArray(getIntent().getStringExtra("story"));
            ArrayList<String> userInputList = intent.getStringArrayListExtra("blanks");
            for (int i = 0; i < userInputList.size(); i++) {
                if (userInputList.size() == storyJSONArray.length() - 1) {// for the special case "Hello______" api call - I presume the exclamation point was forgotten in list
                    texView_story.setText(storyJSONArray.get(i).toString());
                    texView_story.append(userInputList.get(i));

                }
                else if (i == userInputList.size() - 1) { // To add the last sentence where no user input is appended (excluding "Hello______" api call case)
                    texView_story.append(storyJSONArray.get(i).toString());
                    texView_story.append(userInputList.get(i));
                    texView_story.append(storyJSONArray.get(i + 1).toString());
                }
                else { // We append a string element from storyJSONArray and then append the user input string to build the mad libs
                    texView_story.append(storyJSONArray.get(i).toString());
                    texView_story.append(userInputList.get(i));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        homeButton.setOnClickListener(new View.OnClickListener() { // Button clicked sends user back to 1st activity
            @Override
            public void onClick(View v) {
                launchHomeScreen(v);

            }
        });

    }

    public void launchHomeScreen(View view) {
        Intent intent = new Intent(ThirdActivity.this, MainActivity.class);
        startActivity(intent);
    }


}
