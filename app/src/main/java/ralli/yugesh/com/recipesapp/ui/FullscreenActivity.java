package ralli.yugesh.com.recipesapp.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import ralli.yugesh.com.recipesapp.R;

public class FullscreenActivity extends AppCompatActivity {

    private PlayerView mPlayerView;
    private SimpleExoPlayer mPlayer;

    private String stepVideoUrl;
    private long position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        Bundle bundle = getIntent().getBundleExtra("bundle");
        stepVideoUrl = bundle.getString("url");
        position = bundle.getLong("position");

        mPlayerView = findViewById(R.id.exoPlayerView);
        initializePlayer(stepVideoUrl,position);
        hideUi();
    }

    private void hideUi() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                |View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                |View.SYSTEM_UI_FLAG_LOW_PROFILE);
    }

    private void initializePlayer(String stepVideoUrl, long position) {
        if (mPlayer == null){
            TrackSelector trackSelector = new DefaultTrackSelector();
            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext(),
                    Util.getUserAgent(getApplicationContext(), "RecipesApp"), bandwidthMeter);

            Uri uri = Uri.parse(stepVideoUrl);
            mPlayer = ExoPlayerFactory.newSimpleInstance(getApplicationContext(),trackSelector);
            mPlayerView.setPlayer(mPlayer);
            //Prepare media source
            MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
            mPlayer.prepare(mediaSource);
            mPlayer.setPlayWhenReady(true);
            mPlayer.seekTo(position);
        }
    }

    private void releasePlayer(){
        mPlayer.stop();
        mPlayer.release();
        mPlayer = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!stepVideoUrl.equals("")) {
            releasePlayer();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}
