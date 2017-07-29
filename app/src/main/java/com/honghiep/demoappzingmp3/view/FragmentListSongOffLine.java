package com.honghiep.demoappzingmp3.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SeekBar;

import com.honghiep.demoappzingmp3.R;
import com.honghiep.demoappzingmp3.controller.SongOfflineAdapter;

import java.util.List;

/**
 * Created by honghiep on 28/07/2017.
 */

public class FragmentListSongOffLine extends Fragment{
    private ListView lvSongOfline;
    private SongOfflineAdapter.ISongAdapterOffline songAdapterOffline;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        songAdapterOffline= (SongOfflineAdapter.ISongAdapterOffline) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_list_song_offline,container,false);
        lvSongOfline=view.findViewById(R.id.lv_song_offline);
        SongOfflineAdapter adapter=new SongOfflineAdapter(songAdapterOffline);
        lvSongOfline.setAdapter(adapter);
        return view;
    }
}
