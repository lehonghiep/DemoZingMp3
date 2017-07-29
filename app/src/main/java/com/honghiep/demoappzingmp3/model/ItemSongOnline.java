package com.honghiep.demoappzingmp3.model;

import java.util.List;

/**
 * Created by ducnd on 7/26/17.
 */

public class ItemSongOnline {

    private String msg;

    private List<Data> data;

    public String getMsg() {
        return msg;
    }

    public List<Data> getData() {
        return data;
    }

    public static class Data{
        private String id;
        private String name;
        private String artist;
        private String cover;
        private List<String> source_list;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getArtist() {
            return artist;
        }

        public String getCover() {
            return cover;
        }

        public List<String> getSource_list() {
            return source_list;
        }
    }
}
