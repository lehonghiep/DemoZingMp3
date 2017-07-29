package com.honghiep.demoappzingmp3.view;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.honghiep.demoappzingmp3.R;
import com.honghiep.demoappzingmp3.controller.MusicExternalManager;
import com.honghiep.demoappzingmp3.controller.SongOfflineAdapter;
import com.honghiep.demoappzingmp3.controller.SongOnlineAdapter;
import com.honghiep.demoappzingmp3.model.ItemDataMusicExternal;
import com.honghiep.demoappzingmp3.model.ItemSongOnline;
import com.honghiep.demoappzingmp3.util.Util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, MediaPlayer.OnCompletionListener, SongOfflineAdapter.ISongAdapterOffline, SongOnlineAdapter.ISongAdapterOnline {
    private static final String TAG = MainActivity.class.getSimpleName();
    private List<ItemSongOnline> itemSongOnlines;
    private SeekBar seekBar;
    private Toolbar toolbar;
    private int seekForwardAndBackwardTime = 1000;
    private MediaPlayer mediaPlayer;
    private MusicExternalManager musicExternalManager;
    private ImageButton btnPlay;
    private Handler mHandler = new Handler();
    private TextView tvCurrent, tvTotal;
    private int currentSongIndex = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        itemSongOnlines = new ArrayList<>();
        mediaPlayer = new MediaPlayer();
        musicExternalManager = new MusicExternalManager(this);
        initToolbar();
        initViews();
        mediaPlayer.setOnCompletionListener(this);

    }

    private void playSong(int songIndex) {
        ItemDataMusicExternal musicExternal = musicExternalManager.getExternals().get(songIndex);
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(musicExternal.getMusicPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        toolbar.setTitle(musicExternal.getMusicName());
        btnPlay.setBackgroundResource(R.drawable.exo_controls_pause);

        seekBar.setProgress(0);
        seekBar.setMax(100);

        updateProgressBar();
    }

    private void updateProgressBar() {
        mHandler.postDelayed(updateTimeTask, 100);
    }

    private Runnable updateTimeTask = new Runnable() {
        @Override
        public void run() {
            long totalDuration = mediaPlayer.getDuration();
            long currentDuration = mediaPlayer.getCurrentPosition();

            tvTotal.setText(Util.milliSecondsToTimer(totalDuration));
            tvCurrent.setText(Util.milliSecondsToTimer(currentDuration));

            int progress = Util.getProgressPerentage(currentDuration, totalDuration);
            seekBar.setProgress(progress);
            mHandler.postDelayed(this, 100);
        }
    };

    private void initViews() {
        btnPlay = (ImageButton) findViewById(R.id.btn_play);
        btnPlay.setOnClickListener(this);
        tvCurrent = (TextView) findViewById(R.id.tv_current);
        tvTotal = (TextView) findViewById(R.id.tv_total);
        findViewById(R.id.btn_forward).setOnClickListener(this);
        findViewById(R.id.btn_next).setOnClickListener(this);
        findViewById(R.id.btn_previous).setOnClickListener(this);
        findViewById(R.id.btn_rewind).setOnClickListener(this);
        seekBar = (SeekBar) findViewById(R.id.seek_bar);
        seekBar.setOnSeekBarChangeListener(this);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.setTitle("My Toolbar");
        toolbar.setBackgroundResource(R.color.colorPrimary);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.header_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.list_songs_offline:
                openListSongOffline();
                break;
            case R.id.list_songs_online:
                openListSongOnline();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_play:
                playAndPauseSong();
                break;
            case R.id.btn_forward:
                forwardSong();
                break;
            case R.id.btn_rewind:
                rewindSong();
                break;
            case R.id.btn_next:
                nextSong();
                break;
            case R.id.btn_previous:
                previousSong();
                break;
            default:
                break;
        }
    }

    private void previousSong() {
        if (currentSongIndex > 0) {
            playSong(currentSongIndex - 1);
            currentSongIndex = currentSongIndex - 1;
        } else {
            playSong(musicExternalManager.getExternals().size() - 1);
            currentSongIndex = musicExternalManager.getExternals().size() - 1;
        }
    }

    private void rewindSong() {
        int backward = mediaPlayer.getCurrentPosition() - seekForwardAndBackwardTime;
        if (backward >= 0) {
            mediaPlayer.seekTo(backward);
        } else {
            mediaPlayer.seekTo(0);
        }
    }


    private void playAndPauseSong() {
        if (mediaPlayer.isPlaying()) {
            if (mediaPlayer != null) {
                mediaPlayer.pause();
                btnPlay.setBackgroundResource(R.drawable.exo_controls_play);
            }
        } else {
            if (mediaPlayer != null) {
                mediaPlayer.start();
                btnPlay.setBackgroundResource(R.drawable.exo_controls_pause);
            }
        }
    }

    private void forwardSong() {
        int forward = mediaPlayer.getCurrentPosition() + seekForwardAndBackwardTime;
        if (forward <= mediaPlayer.getDuration()) {
            mediaPlayer.seekTo(forward);
        } else {
            mediaPlayer.seekTo(mediaPlayer.getDuration());
        }
    }

    private void nextSong() {
        if (currentSongIndex < musicExternalManager.getExternals().size() - 1) {
            playSong(currentSongIndex + 1);
            currentSongIndex = currentSongIndex + 1;
        } else {
            playSong(0);
            currentSongIndex = 0;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        Log.d(TAG, "int i=" + i);
        Log.d(TAG, "boolen b=" + b);
        if (b) {
            seekBar.setMax(100);
            seekBar.setProgress(i);
            int totalDuration = mediaPlayer.getDuration();
            int currentDuration = Util.progressToTimer(i, totalDuration);
            mediaPlayer.seekTo(currentDuration);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        nextSong();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(updateTimeTask);
        mediaPlayer.release();

    }

    @Override
    public int getCount() {
        return musicExternalManager.getExternals().size();
    }

    @Override
    public ItemDataMusicExternal getData(int position) {
        return musicExternalManager.getExternals().get(position);
    }

    @Override
    public void onClickItemMusicOffline(int position) {
        playSong(position);
        currentSongIndex = position;
    }

    public void openListSongOffline() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager manager = getSupportFragmentManager();
        //kiem tra fragment login da ton tai trong fragmentmanager chua
        Fragment fragment =
                manager.findFragmentByTag(FragmentListSongOffLine.class.getName());
        //if fragment == null thi fragment chua co trong fragmentmanager
        if (fragment != null) {
            if (fragment.isVisible()) {

            } else {
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.content, fragment, FragmentListSongOffLine.class.getName());
                transaction.addToBackStack(FragmentListSongOffLine.class.getName());
                transaction.commit();
            }
            return;
        }

        //tao ra login fragment va them vao
        fragment = new FragmentListSongOffLine();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.content, fragment, FragmentListSongOffLine.class.getName());
        transaction.addToBackStack(FragmentListSongOffLine.class.getName());
        transaction.commit();
    }

    public void getSongDetail(final List<String> dataCodes, final SongOnlineAdapter adapter) {
        itemSongOnlines=new ArrayList<>();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                for (String dataCode : dataCodes) {
                    String link = "http://mp3.zing.vn/html5xml/song-xml/" + dataCode;
                    try {
                        URL url = new URL(link);
                        String content = "";
                        InputStream in = url.openStream();
                        byte b[] = new byte[1024];
                        int le = in.read(b);
                        while (le >= 0) {
                            content = content + new String(b, 0, le);
                            le = in.read(b);
                        }
                        in.close();
                        Gson gson = new Gson();
                        //chuyen chuoi json sand mo do tuong
                        ItemSongOnline itemSong = gson.fromJson(content, ItemSongOnline.class);
                        if (itemSong != null) {
                            itemSongOnlines.add(itemSong);
                        }

                        Log.d(TAG, "getSongDetail content: " + content);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                adapter.notifyDataSetChanged();
            }
        }.execute();

    }


    public void searchSong(String songName, final SongOnlineAdapter adapter) {
        final String linkRoot = "http://mp3.zing.vn/tim-kiem/bai-hat.html?q="
                + songName.replaceAll(" ", "+");
        new AsyncTask<Void, Void, Void>() {
            private List<String> dataCodes;

            @Override
            protected void onPreExecute() {
                dataCodes = new ArrayList<String>();
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    Document document = Jsoup.connect(linkRoot).get();
                    Elements elements = document.select("div.item-song");

                    for (Element element : elements) {
                        String dataCode = element.attr("data-code");
                        String title = element.select("h3")
                                .select("a").attr("title");
                        String numberHear = element.select("div.info-meta").first()
                                .select("span.fn-number").text();

                        Log.d(TAG, "onClick dataCode: " + dataCode);
                        Log.d(TAG, "onClick title: " + title);
                        Log.d(TAG, "onClick numberHear: " + numberHear);
                        Log.d(TAG, "onCLick=================");
                        dataCodes.add(dataCode);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                getSongDetail(dataCodes, adapter);
            }
        }.execute();
    }

    @Override
    public void onBackPressed() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        super.onBackPressed();
    }

    public void openListSongOnline() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager manager = getSupportFragmentManager();

        Fragment fragment =
                manager.findFragmentByTag(FragmentSongOnline.class.getName());

        if (fragment != null) {
            if (fragment.isVisible()) {

            } else {
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.content, fragment, FragmentSongOnline.class.getName());
                transaction.addToBackStack(FragmentSongOnline.class.getName());
                transaction.commit();
            }
            return;
        }


        fragment = new FragmentSongOnline();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.content, fragment, FragmentSongOnline.class.getName());
        transaction.addToBackStack(FragmentSongOnline.class.getName());
        transaction.commit();
    }

    @Override
    public int getCountSongOnline() {
        return itemSongOnlines.size();
    }

    @Override
    public ItemSongOnline getDataOnline(int position) {
        return itemSongOnlines.get(position);
    }

    @Override
    public void onClickItemMusicOnline(int position) {
        playSongOnline(position);
    }

    public void playSongOnline(int position) {
        if(position>=itemSongOnlines.size()){
            return;
        }
        ItemSongOnline songOnline = itemSongOnlines.get(position);
        try {
            mediaPlayer.reset();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            String url=songOnline.getData().get(0).getSource_list().get(0);
            mediaPlayer.setDataSource(url);
            Log.d(TAG, "PlaySongOnline: " + url);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });
            toolbar.setTitle(songOnline.getData().get(0).getName());
            btnPlay.setBackgroundResource(R.drawable.exo_controls_pause);

            seekBar.setProgress(0);
            seekBar.setMax(100);

            updateProgressBar();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}