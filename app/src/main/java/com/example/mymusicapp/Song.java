package com.example.mymusicapp;

/**
 * {@link Song} represents a Song with a title and artist
 *
 */
public class Song {

    //Stores the title of song
    private String title;

    //Stores the artist of song
    private String artist;


    /**
     * Create a new Song object.
     *
     * @param currentTitle is the title of the song
     * @param currentArtist is artist of the song
     */
    public Song(String currentTitle, String currentArtist) {
        title=currentTitle;
        artist=currentArtist;
    }


    //Get the title of the song.

    String getTitle(){
        return title;


    // Get the artist of the song.
    }
    String getArtist(){
        return artist;
    }

}
