package com.example.mymusicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;


public class PlaySongActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);

        /*gets the information from the intent that was passed and casts it to a Intent object*/
        Intent intent = getIntent();

         //gets the strings from the intent
        String artist = intent.getStringExtra("ArtistString");
        String title = intent.getStringExtra("TitleString");

        TextView titleView = findViewById(R.id.title);
        titleView.setText(title);

        TextView artistView = findViewById(R.id.artist);
        artistView.setText(artist);
    }


}
