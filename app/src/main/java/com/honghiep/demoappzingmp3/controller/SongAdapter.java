package com.honghiep.demoappzingmp3.controller;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.honghiep.demoappzingmp3.R;
import com.honghiep.demoappzingmp3.model.ItemDataMusicExternal;

/**
 * Created by honghiep on 26/07/2017.
 */

public class SongAdapter extends BaseAdapter {
    private ISongAdapter mInterf;

    public SongAdapter(ISongAdapter mInterf) {
        this.mInterf = mInterf;
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
            view.setTag(holder);
        }
        ViewHolder holder= (ViewHolder) view.getTag();
        ItemDataMusicExternal musicExternal=mInterf.getData(i);
        holder.tvMusicName.setText(musicExternal.getMusicName());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInterf.onClickItem(i);
            }
        });
        return view;
    }

    public interface ISongAdapter {
        int getCount();

        ItemDataMusicExternal getData(int position);

        void onClickItem(int position);
    }

    class ViewHolder {
        TextView tvMusicName;
    }
}
