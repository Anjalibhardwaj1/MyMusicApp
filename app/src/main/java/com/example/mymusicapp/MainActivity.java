package com.example.mymusicapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    //These global strings are used to display Title and Artist on SongPlayerActivity
    String currentTitle;
    String currentArtist;

    //{@ songList} is an ArrayList that will store songs that contain the title and artist
    ArrayList<Song> songList;

    //Displays song and artist on a listView
    ListView listView;

    //This value will help identify the storage permission request
    private int STORAGE_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //This code will check if the permission is already granted or not
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(MainActivity.this, "You Have Granted Access to Storage!", Toast.LENGTH_LONG).show();

            //if the permission is granted this method will be used and the ListView will appear
            listItems();

        } else {

            //if the permission is not granted then we must request storage permission using the requestStoragePermission() method
            requestStoragePermission();
        }
    }

    private void requestStoragePermission() {

        //This code will explain to the user why we need to access their storage.
        //If shouldShowRequestPermissionRationale returned true then the permission rationale is prompted explaining why we need to access the storage data
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed to access your music files.")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //This code will show the permission system dialog if "ok" is clicked
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @SuppressLint("NewApi")
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            endTask();
                        }
                    })
                    .create().show();

          //If shouldShowRequestPermissionRationale returned false then the request permission prompt is shown without explanation
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    //checks if permission was granted or denied
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
                endTask();
            }
        }
    }

    //this will close the app if the permission is denied
    public void endTask() {
        if (Build.VERSION.SDK_INT >= 21) {
            finishAndRemoveTask();
        } else {
            finish();
        }
    }


    //The method gets the mp3 files from storage and stores title and artist values in songList
    public void getSongs() {

        //To get data from the content provider we call the {@link ContentResolver} which is obtained by the getContentResolver() method.
        ContentResolver contentResolver = getContentResolver();

        //Get content provider URI
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        //the query method will return a cursor object
        Cursor songCursor = contentResolver.query(songUri, null, null, null, null);

        //Get the title and artist data; if the songCursor object is not empty and the songCursor has moved to first song
        if (songCursor != null && songCursor.moveToFirst()) {
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);

            //loop to get each title and artist and store it in the songList
            do {
                String currentTitle = songCursor.getString((songTitle));
                String currentArtist = songCursor.getString((songArtist));
                songList.add(new Song(currentTitle, currentArtist));

                //Moves to next song
            } while (songCursor.moveToNext());
        }
    }

    //This is will give the song data to the listView and when an item is clicked on the ListView an intent will move to the SongPlayActivity.java
    public void listItems() {

        //create new song array
        songList = new ArrayList<>();

        //get all mp3 storage data
        getSongs();

        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list_View, which is declared in the
        // activity_main.xml layout file.
        listView = findViewById(R.id.list_View);

        // Create an {@link SongAdapter}, whose data source is a list of {@link Song}s. The
        // adapter knows how to create list items for each item in the list.
        SongAdapter songAdapter = new SongAdapter(this, songList);

        // Make the {@link ListView} use the {@link songAdapter} we created above, so that the
        // {@link ListView} will display list items for each {@link Song} in the list.
        listView.setAdapter(songAdapter);

        // Set a click listener on listView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            // The code in this method will be executed when an item in the listView is clicked on.
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // A new intent to open the {@link SongPlayerActivity}
                Intent i = new Intent(MainActivity.this, SongPlayerActivity.class);

                //Gets the position of the song on the item clicked
                Song currentSong = songList.get(position);

                //Gets the title of the song on the item clicked and stores it in the String variable currentTitle
                currentTitle = currentSong.getTitle();

                //Gets the artist of the song on the item clicked and stores it in the String variable currentArtist
                currentArtist = currentSong.getArtist();

               //This will store the title and author strings so it can be accessed in the SongPlayerActivity
                i.putExtra("TitleString", currentTitle);
                i.putExtra("ArtistString", currentArtist);

                // Start the new activity
                startActivity(i);

            }
        });

        //This will order the songs alphabetically
        Collections.sort(songList, new Comparator<Song>() {

            //Here the strings of two songs are compared in order to position alphabetically
            public int compare(Song a, Song b) {
                return a.getTitle().compareTo(b.getTitle());
            }
        });
    }

}
