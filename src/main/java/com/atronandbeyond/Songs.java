package com.atronandbeyond;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Songs {
    int current = 0;
    class Song{
        private String filename;
        private long duration;
        private String artist;
        private String song;
        private String album;
        public Song(String filename, long duration) {
            this.filename = filename;
            this.duration = duration;
        }
        public Song(String filename, long duration, String artist, String song, String album) {
            this.filename = filename;
            this.duration = duration;
            this.artist = artist;
            this.song = song;
            this.album = album;
        }
        public String getFilename() {
            return filename;
        }

        public String getArtist() {
            return artist;
        }

        public String getSong() {
            return song;
        }

        public String getAlbum() {
            return album;
        }

        public long getDuration() {
            return duration;
        }
    }
    private List<Song> songs;

    public Songs() {
        songs = new ArrayList<>(Arrays.asList(
                new Song("around_the_world", 1000L*454,"Daft Punk","Around the World", "Homework"),
                new Song("milky", 1000L*214,"Milky","Just the Way You Are", "Star"),
                new Song("lady", 1000L*226, "Modjo", "Lady (Hear Me Tonight)", "Modjo"),
                new Song("put_em_high", 1000L*228,"StoneBridge", "Put 'Em High", "Can't Get Enough"),
                new Song("pjanoo", 1000L*193, "Eric Prydz","Pjanoo", "Eric Prydz Presents Pryda"),
                new Song("stardust", 1000L*420, "Stardust", "Music Sounds Better with You", "Stardust"),
                new Song("blue_song", 1000L*293,"Eiffel 65","Blue (Da Ba Dee)", "Europop"),
                new Song("kylie", 1000L*245, "Kylie Minogue","Love at First Sight", "Fever")));
    }

    public List<Song> getSongs() {
        return songs;
    }

    Songs.Song getNext() {
        return songs.get(++current % songs.size());
    }

    @Override
    public String toString() {
        return "Songs{" +
                "songs=" + songs +
                '}';
    }
}
