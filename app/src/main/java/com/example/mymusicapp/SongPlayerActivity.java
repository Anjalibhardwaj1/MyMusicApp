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

        /*gets the information from the intent that was passed and casts it to a Intent object*/
        Intent i = getIntent();

         //gets the strings from the intent
        String artist = i.getStringExtra("ArtistString");
        String title = i.getStringExtra("TitleString");

        TextView titleView = findViewById(R.id.title);
        titleView.setText(title);

        TextView artistView = findViewById(R.id.artist);
        artistView.setText(artist);
    }


}
