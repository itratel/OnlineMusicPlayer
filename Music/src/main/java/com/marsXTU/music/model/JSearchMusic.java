package com.marsXTU.music.model;

/**
 * 搜索
 * Created by whd on 2016/3/17.
 */
public class JSearchMusic {
    JSong[] song;

    public JSong[] getSong() {
        return song;
    }

    public void setSong(JSong[] song) {
        this.song = song;
    }

    public static class JSong {
        String songname;
        String artistname;
        String songid;

        public String getSongname() {
            return songname;
        }

        public void setSongname(String songname) {
            this.songname = songname;
        }

        public String getArtistname() {
            return artistname;
        }

        public void setArtistname(String artistname) {
            this.artistname = artistname;
        }

        public String getSongid() {
            return songid;
        }

        public void setSongid(String songid) {
            this.songid = songid;
        }
    }
}
