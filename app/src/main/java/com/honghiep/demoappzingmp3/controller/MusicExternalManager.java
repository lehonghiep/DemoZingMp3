package com.honghiep.demoappzingmp3.controller;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.honghiep.demoappzingmp3.model.ItemDataMusicExternal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ducnd on 7/22/17.
 */

public class MusicExternalManager {
    private List<ItemDataMusicExternal> externals;
    private static final String TAG = MusicExternalManager.class.getSimpleName();

    public MusicExternalManager(Context context) {
        externals = new ArrayList<>();
        queyAllMusic(context);
    }

    private void queyAllMusic(Context context) {
        //dia chi cua mang music trong dien thoai
        Uri uri =
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = new String[]{
                "_id",
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ARTIST_ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATE_MODIFIED
        };
        //tao ra cusor tro den bang tu uri

        Cursor cursor =
                context.getContentResolver()
                        .query(uri, projection, null, null, null);
//        String names[] = cursor.getColumnNames();
//        for (String name : names) {
//            Log.d(TAG, "queyAllMusic name: " + name);
//        }
        if (cursor == null) {
            return;
        }

        int indexPath = cursor.getColumnIndex(
                MediaStore.Audio.Media.DATA);
        int indexName = cursor.getColumnIndex(
                MediaStore.Audio.Media.DISPLAY_NAME);
        int indexDuration = cursor.getColumnIndex(
                MediaStore.Audio.Media.DURATION);
        int indexArtist = cursor.getColumnIndex(
                MediaStore.Audio.Media.ARTIST);
        int indexDateModifier = cursor.getColumnIndex(
                MediaStore.Audio.Media.DATE_MODIFIED);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String path = cursor.getString(indexPath);
            String name = cursor.getString(indexName);
            long duration = cursor.getLong(indexDuration);
            String artist = cursor.getString(indexArtist);
            long dateModify = cursor.getLong(indexDateModifier);
            externals.add(new ItemDataMusicExternal(path, name, artist, duration,
                    new Date(dateModify * 1000)));

            Log.d(TAG, "path: " + path);
            Log.d(TAG, "name: " + name);
            Log.d(TAG, "duration: " + duration);
            Log.d(TAG, "artist: " + artist);
            Log.d(TAG, "=================");
            cursor.moveToNext();
        }
        cursor.close();
    }

    public List<ItemDataMusicExternal> getExternals() {
        return externals;
    }
}
