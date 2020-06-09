package com.example.mymusicapp;

public class Song {
    private String title;
    private String artist;

    public Song(String currentTitle, String currentArtist) {
        title=currentTitle;
        artist=currentArtist;
    }

    public String getTitle(){
        return title;
    }
    public String getArtist(){
        return artist;
    }

}
