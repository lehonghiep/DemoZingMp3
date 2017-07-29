package com.honghiep.demoappzingmp3.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.honghiep.demoappzingmp3.R;
import com.honghiep.demoappzingmp3.controller.SongOnlineAdapter;

/**
 * Created by honghiep on 28/07/2017.
 */

public class FragmentSongOnline extends Fragment implements View.OnClickListener {
    private ListView lvSongOnline;
    private EditText edtSearch;
private SongOnlineAdapter adapter;

    private SongOnlineAdapter.ISongAdapterOnline songAdapterOnline;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        songAdapterOnline = (SongOnlineAdapter.ISongAdapterOnline) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song_online, container, false);
        lvSongOnline = view.findViewById(R.id.lv_song_online);
        edtSearch = view.findViewById(R.id.edt_search);
        view.findViewById(R.id.btn_search).setOnClickListener(this);
        adapter=new SongOnlineAdapter(songAdapterOnline);
        lvSongOnline.setAdapter(adapter);
        return view;
    }

    @Override
    public void onClick(View view) {
        ((MainActivity) getActivity()).searchSong(edtSearch.getText().toString(),adapter);
    }
}
