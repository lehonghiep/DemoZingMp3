package com.honghiep.demoappzingmp3.controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.honghiep.demoappzingmp3.R;
import com.honghiep.demoappzingmp3.model.ItemDataMusicExternal;
import com.honghiep.demoappzingmp3.model.ItemSongOnline;

import java.text.SimpleDateFormat;

/**
 * Created by honghiep on 28/07/2017.
 */

public class SongOnlineAdapter extends BaseAdapter {
    private SimpleDateFormat formatDuration =
            new SimpleDateFormat("mm:ss");
    private ISongAdapterOnline mInterf;

    public SongOnlineAdapter(ISongAdapterOnline mInterf) {
        this.mInterf = mInterf;
    }

    @Override
    public int getCount() {
        return mInterf.getCountSongOnline();
    }

    @Override
    public Object getItem(int i) {
        return mInterf.getDataOnline(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            view = inflater.inflate(R.layout.item_song, viewGroup, false);
            ViewHolder holder = new ViewHolder();
            holder.tvMusicName = view.findViewById(R.id.tv_music_name);
            holder.tvArtist = view.findViewById(R.id.tv_artist);
            holder.tvDuration = view.findViewById(R.id.tv_duration);
            view.setTag(holder);
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        ItemSongOnline dataOnline = mInterf.getDataOnline(i);
        holder.tvMusicName.setText(dataOnline.getData().get(0).getName());
        holder.tvArtist.setText(dataOnline.getData().get(0).getArtist());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInterf.onClickItemMusicOnline(i);
            }
        });
        return view;
    }

    public interface ISongAdapterOnline {
        int getCountSongOnline();

        ItemSongOnline getDataOnline(int position);

        void onClickItemMusicOnline(int position);
    }

    class ViewHolder {
        TextView tvMusicName;
        TextView tvArtist;
        TextView tvDuration;
    }
}
