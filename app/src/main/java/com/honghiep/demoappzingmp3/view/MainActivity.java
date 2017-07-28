package com.honghiep.demoappzingmp3.view;

import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.honghiep.demoappzingmp3.R;
import com.honghiep.demoappzingmp3.controller.MusicExternalManager;
import com.honghiep.demoappzingmp3.model.ItemDataMusicExternal;
import com.honghiep.demoappzingmp3.util.Util;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, MediaPlayer.OnCompletionListener {
    private static final String TAG=MainActivity.class.getSimpleName();

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
        mediaPlayer = new MediaPlayer();
        musicExternalManager = new MusicExternalManager(this);
        initToolbar();
        initViews();
        mediaPlayer.setOnCompletionListener(this);
        playSong(currentSongIndex);
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
            case R.id.list_songs:
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
        Log.d(TAG,"int i="+i);
        Log.d(TAG,"boolen b="+b);
        if(b){
            seekBar.setMax(100);
            seekBar.setProgress(i);
            int totalDuration = mediaPlayer.getDuration();
            int currentDuration = Util.progressToTimer(i,  totalDuration);
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
}
