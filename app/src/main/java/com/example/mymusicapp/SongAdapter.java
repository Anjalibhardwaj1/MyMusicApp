package com.example.mymusicapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * {@link SongAdapter} is an {@link BaseAdapter} that can provide the layout for each list item
 * based on a data source, which is a list of {@link Song} objects.
 */

public class SongAdapter extends BaseAdapter {

    private ArrayList<Song> songs;
    private LayoutInflater songInf;

    /**
     * Create a new {@link SongAdapter} object.
     *
     * @param context is the current context (i.e. Activity) that the adapter is being created in.
     * @param theSongs is the list of {@link Song}s to be displayed.
     */

    SongAdapter(Context context, ArrayList<Song> theSongs){
        songs=theSongs;
        songInf=LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return songs.size();

    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //map the song to layout
        LinearLayout songLayout = (LinearLayout)songInf.inflate(R.layout.song, parent, false);

        //get song using position
        Song currentSong = songs.get(position);

        // Find the TextView in the song.xml layout with the ID song_title.
        TextView songView = songLayout.findViewById(R.id.song_title);

        // Get the song title from the currentSong object and set this text on
        // the songView TextView.
        songView.setText(currentSong.getTitle());

        // Find the TextView in the song.xml layout with the ID song_artist.
        TextView artistView = songLayout.findViewById(R.id.song_artist);

        // Get the song artist from the currentSong object and set this text on
        // the artistView TextView.
        artistView.setText(currentSong.getArtist());

        // Return the whole list item layout (containing 2 TextViews) so that it can be shown in
        // the ListView.
        //set position as tag
        songLayout.setTag(position);
        return songLayout;
    }

}