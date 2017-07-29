package com.honghiep.demoappzingmp3.controller;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.honghiep.demoappzingmp3.R;
import com.honghiep.demoappzingmp3.model.ItemDataMusicExternal;

import java.text.SimpleDateFormat;

/**
 * Created by honghiep on 28/07/2017.
 */

public class SongOfflineAdapter extends BaseAdapter {
    private SimpleDateFormat formatDuration =
            new SimpleDateFormat("mm:ss");
    ISongAdapterOffline mInterf;
    public SongOfflineAdapter(ISongAdapterOffline mInterf){
        this.mInterf=mInterf;
    }
    @Override
    public int getCount() {
        return mInterf.getCount();
    }

    @Override
    public Object getItem(int i) {
        return mInterf.getData(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if(view==null){
            LayoutInflater inflater=LayoutInflater.from(viewGroup.getContext());
            view=inflater.inflate(R.layout.item_song,viewGroup,false);
            ViewHolder holder=new ViewHolder();
            holder.tvMusicName=view.findViewById(R.id.tv_music_name);
            holder.tvArtist=view.findViewById(R.id.tv_artist);
            holder.tvDuration=view.findViewById(R.id.tv_duration);
            view.setTag(holder);
        }
        ViewHolder holder= (ViewHolder) view.getTag();
        ItemDataMusicExternal musicExternal=mInterf.getData(i);
        holder.tvMusicName.setText(musicExternal.getMusicName());
        holder.tvArtist.setText(musicExternal.getArtist());
        holder.tvDuration.setText(formatDuration.format(musicExternal.getDuration()));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInterf.onClickItemMusicOffline(i);
            }
        });
        return view;
    }
    public interface ISongAdapterOffline{
        int getCount();
        ItemDataMusicExternal getData(int position);
        void onClickItemMusicOffline(int position);
    }
    class ViewHolder{
        TextView tvMusicName;
        TextView tvArtist;
        TextView tvDuration;
    }
}
