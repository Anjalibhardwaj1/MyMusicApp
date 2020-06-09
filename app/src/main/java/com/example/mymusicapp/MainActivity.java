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

    //ArrayList will store the song and artist from the Song Class
    ArrayList<Song> songList;

    //Displays song and artist on a listView
    ListView listView;

    //This value will help us identify our storage permission request
    private int STORAGE_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //This code will check if the permission is already granted or not
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(MainActivity.this, "You Have Granted Access to Storage!", Toast.LENGTH_LONG).show();

            //if the permission is granted this method will be used and the ListView will appear
            doStuff();

        } else {

            //if the permission is not granted then we must request storage permission via this method
            requestStoragePermission();
        }
    }

    private void requestStoragePermission() {

        //This code will explain to the user why we need to access their storage.
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed to access your music files.")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
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


        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    //check the result of our permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
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


    //this will get the mp3 files from storage and add the title and artist of each song to the songList
    public void getMusic() {
        ContentResolver contentResolver = getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null, null, null, null);

        if (songCursor != null && songCursor.moveToFirst()) {
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);

            do {
                String currentTitle = songCursor.getString((songTitle));
                String currentArtist = songCursor.getString((songArtist));
                songList.add(new Song(currentTitle, currentArtist));

            } while (songCursor.moveToNext());
        }
    }

    //this is will give the data to the listView and when an item is clicked on the ListView it will move to the SongPlayActivity
    public void doStuff() {
        listView = (ListView) findViewById(R.id.list_View);
        songList = new ArrayList<>();
        getMusic();

        final SongAdapter songAdt = new SongAdapter(this, songList);
        listView.setAdapter(songAdt);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this, SongPlayerActivity.class);

                Song currSong = songList.get(position);
                currentTitle = currSong.getTitle();
                currentArtist = currSong.getArtist();


                i.putExtra("TitleString", currentTitle);
                i.putExtra("ArtistString", currentArtist);
                startActivity(i);

            }
        });

        //This will order the songs alphabetically 
        Collections.sort(songList, new Comparator<Song>() {
            public int compare(Song a, Song b) {
                return a.getTitle().compareTo(b.getTitle());
            }
        });
    }

}
