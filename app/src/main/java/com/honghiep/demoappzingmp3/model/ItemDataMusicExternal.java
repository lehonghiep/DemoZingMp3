package com.honghiep.demoappzingmp3.model;

import java.util.Date;

/**
 * Created by ducnd on 7/22/17.
 */

public class ItemDataMusicExternal {
    private String musicName;
    private String artist;
    private long duration;
    private Date dataModifier;
    private String musicPath;

    public ItemDataMusicExternal(String musicPath,
                                 String musicName, String artist, long duration, Date dataModifier) {
        this.musicPath=musicPath;
        this.musicName = musicName;
        this.artist = artist;
        this.duration = duration;
        this.dataModifier = dataModifier;
    }

    public String getMusicName() {
        return musicName;
    }

    public String getArtist() {
        return artist;
    }

    public long getDuration() {
        return duration;
    }

    public Date getDataModifier() {
        return dataModifier;
    }

    public String getMusicPath() {
        return musicPath;
    }
}
