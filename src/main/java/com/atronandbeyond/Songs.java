package com.atronandbeyond;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Songs {
    int current = 0;
    class Song{
        private String filename;
        private long duration;
        public Song(String filename, long duration) {
            this.filename = filename;
            this.duration = duration;
        }

        public String getFilename() {
            return filename;
        }

        public long getDuration() {
            return duration;
        }
    }
    private List<Song> songs;

    public Songs() {
        songs = new ArrayList<>(Arrays.asList(
                new Song("around_the_world", 1000L*454),
                new Song("lady", 1000L*226),
                new Song("put_em_high", 1000L*228)));
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
