package com.example.mymusicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;


public class SongPlayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);

        //This gets the data from the intent that was passed and brings it to an Intent object
        Intent i = getIntent();

        //Gets the strings from the intent
        String artist = i.getStringExtra("ArtistString");
        String title = i.getStringExtra("TitleString");

        //Gets the titleView on the activity_play_song.xml
        TextView titleView = findViewById(R.id.titleView);

        //Sets the titleView to the title retrieved from the intent
        titleView.setText(title);

        //Gets the artistView on the activity_play_song.xml
        TextView artistView = findViewById(R.id.artistView);

        //Sets the artistView to the artist retrieved from the intent
        artistView.setText(artist);
    }


}
