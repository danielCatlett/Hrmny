package com.daniel_catlett.hrmny;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by daniel on 4/29/2017.
 */

public class MusicDatabase
{
    ArrayList<HashMap<String, String>> songs;
    ArrayList<String> songTitles;
    ArrayList<String> artists;
    ArrayList<String> albums;
    ArrayList<String> genres;

    public MusicDatabase(Context context)
    {
        songs = getSongs(context);
        setSongTitles();
        setArtists();
        setAlbums();
        setGenres();
    }

    public void setSongTitles()
    {
        for(int i = 0; i < songs.size(); i++)
        {
            String title = songs.get(i).get("songName"); //Get song title
            songTitles.add(title); //add to list
        }
        Collections.sort(songTitles, String.CASE_INSENSITIVE_ORDER); //sort alphabetically
    }

    public ArrayList<String> getSongTitles()
    {
        return songTitles;
    }

    public void setArtists()
    {
        for(int i = 0; i < songs.size(); i++)
        {
            if(!artists.contains(songs.get(i).get("artistName")))
                artists.add(songs.get(i).get("artistName"));
        }
        Collections.sort(artists, String.CASE_INSENSITIVE_ORDER);
    }

    public ArrayList<String> getArtists()
    {
        return artists;
    }

    public void setAlbums()
    {
        for(int i = 0; i < songs.size(); i++)
        {
            if(!albums.contains(songs.get(i).get("albumName")))
                albums.add(songs.get(i).get("albumName"));
        }
        Collections.sort(albums, String.CASE_INSENSITIVE_ORDER);
    }

    public ArrayList<String> getAlbums()
    {
        return albums;
    }

    public void setGenres()
    {
        for(int i = 0; i < songs.size(); i++)
        {
            if(!genres.contains(songs.get(i).get("genre")))
                genres.add(songs.get(i).get("genre"));
        }
        Collections.sort(genres, String.CASE_INSENSITIVE_ORDER);
    }

    public ArrayList<String> getGenres()
    {
        return genres;
    }

    public ArrayList<String> getSongsByArtist(String artist)
    {
        ArrayList<String> songsByArtist = new ArrayList<String>();

        //for each song found
        for(int i = 0; i < songs.size(); i++)
        {
            //if the current song being checked is by the artist being tested
            if(songs.get(i).get("artistName").equals(artist))
                songsByArtist.add(songs.get(i).get("songName"));
        }
        Collections.sort(songsByArtist, String.CASE_INSENSITIVE_ORDER);

        return songsByArtist;
    }

    public ArrayList<String> getAlbumsByArtist(String artist)
    {
        ArrayList<String> albumsByArtist = new ArrayList<String>();

        //for each song found
        for(int i = 0; i < songs.size(); i++)
        {
            //if the current song being checked is by the artist being tested, and the album that
            //song is in has not already been added to the list
            if(songs.get(i).get("artistName").equals(artist) && !albumsByArtist.contains(songs.get(i).get("albumName")))
                albumsByArtist.add(songs.get(i).get("albumName"));
        }
        Collections.sort(albumsByArtist, String.CASE_INSENSITIVE_ORDER);

        return albumsByArtist;
    }

    public ArrayList<String> getSongsInAlbum(String album)
    {
        ArrayList<String> songsInAlbum = new ArrayList<String>();
        ArrayList<Integer> trackNumbers = new ArrayList<Integer>();

        //for each song
        for(int i = 0; i < songs.size(); i++)
        {
            //if the album name is the same as the given album name
            if(songs.get(i).get("albumName").equals(album))
            {
                //add the song name to the songs ArrayList
                songsInAlbum.add(songs.get(i).get("songName"));
                //add the track name to the corresponding trackNumber ArrayList
                trackNumbers.add(Integer.parseInt(songs.get(i).get("trackNumber")));
            }
        }

        //sorting songs in track order
        for(int i = 0; i < songsInAlbum.size(); i++)
        {
            int currentLowest = findCurrentLowest(trackNumbers, i); //find lowest track number (or second lowest, or third lowest...)
            //save those values, so that they can be removed from the ArrayList
            String trackName = songsInAlbum.get(currentLowest);
            int trackNumber = trackNumbers.get(currentLowest);
            //remove those values from the arraylist
            songsInAlbum.remove(currentLowest);
            trackNumbers.remove(currentLowest);
            //put the values in the sorted order
            songsInAlbum.add(currentLowest, trackName);
            trackNumbers.add(currentLowest, trackNumber);
        }

        return songsInAlbum;
    }

    public ArrayList<String> getSongsInGenre(String genre)
    {
        ArrayList<String> songsInGenre = new ArrayList<String>();

        //for each song found
        for(int i = 0; i < songs.size(); i++)
        {
            //if the current song being checked is by the artist being tested
            if(songs.get(i).get("genreName").equals(genre))
                songsInGenre.add(songs.get(i).get("songName"));
        }
        Collections.sort(songsInGenre, String.CASE_INSENSITIVE_ORDER);

        return songsInGenre;
    }

    /*
    Finds and returns the lowest track number first time called, second lowest second time
    called, ect. Returns the index of that value found back to the getSongsInAlbum method, so that
    it knows which song to move to the front
     */
    private int findCurrentLowest(ArrayList<Integer> trackNumbers, int currentIndex)
    {
        int currentLowest = trackNumbers.get(currentIndex); //lowest track number found so far
        int indexOfLowest = currentIndex; //index of lowest track number

        for(int i = currentIndex; i < trackNumbers.size(); i++)
        {
            if(trackNumbers.get(i) < currentLowest)
            {
                currentLowest = trackNumbers.get(i);
                indexOfLowest = i;
            }
        }

        return indexOfLowest;
    }

    private ArrayList<HashMap<String, String>> getSongs(Context context)
    {
        ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
        String[] STAR = { "*" };

        Cursor cursor;
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"; //this is weird


        cursor = context.getContentResolver().query(uri, STAR, selection, null, null);

        if (cursor != null)
        {
            if (cursor.moveToFirst())
            {
                //Retrieve the songs and all metadata in this loop, add to returning arraylist
                do
                {
                    String songName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));

                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));

                    String albumName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));

                    String artistName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));

                    String genre = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Genres.NAME));

                    int albumId = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                    String albumIdString = String.valueOf(albumId);

                    int trackNumber = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.TRACK));
                    String trackNumberString = String.valueOf(trackNumber);

                    //Each song gets their own Hashmap, containing all metadata
                    HashMap<String, String> song = new HashMap<String, String>();
                    song.put("songName", songName);
                    song.put("albumName", albumName);
                    song.put("genreName", genre);
                    song.put("albumId", albumIdString);
                    song.put("artistName", artistName);
                    song.put("trackNumber", trackNumberString);
                    song.put("songPath", path);
                    songsList.add(song);

                } while (cursor.moveToNext());
                return songsList;
            }
        }
        return null;
    }
}
