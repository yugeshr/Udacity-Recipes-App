package ralli.yugesh.com.recipesapp.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;

import ralli.yugesh.com.recipesapp.R;
import ralli.yugesh.com.recipesapp.utils.ExoPlayerVideoHandler;

public class FullscreenActivity extends AppCompatActivity {

    private PlayerView mPlayerView;
    private SimpleExoPlayer mPlayer;

    private String stepVideoUrl;
    private long position;

    private boolean destroyVideo = true;
    private boolean playerReady = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        Bundle bundle = getIntent().getBundleExtra("bundle");
        stepVideoUrl = bundle.getString("url");
        position = bundle.getLong("position");
        playerReady = bundle.getBoolean("state");

        mPlayerView = findViewById(R.id.exoPlayerView);
        initializePlayer();
        hideUi();
    }

    private void hideUi() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                |View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                |View.SYSTEM_UI_FLAG_LOW_PROFILE);
    }

    private void initializePlayer() {
        if (mPlayer == null){
            ExoPlayerVideoHandler.getInstance()
                    .prepareExoPlayerForUri(getApplicationContext(),
                            Uri.parse(stepVideoUrl), mPlayerView,playerReady);
            ExoPlayerVideoHandler.getInstance().goToForeground();
            ExoPlayerVideoHandler.getInstance().goToPosition(position);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ExoPlayerVideoHandler.getInstance()
                .prepareExoPlayerForUri(getApplicationContext(),
                        Uri.parse(stepVideoUrl), mPlayerView,playerReady);
        ExoPlayerVideoHandler.getInstance().goToForeground();
        ExoPlayerVideoHandler.getInstance().goToPosition(position);
        (findViewById(R.id.exo_fullscreen)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                destroyVideo = false;
                finish();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        ExoPlayerVideoHandler.getInstance().goToBackground();
    }

    @Override
    public void onBackPressed() {
        destroyVideo = false;
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(destroyVideo){
            ExoPlayerVideoHandler.getInstance().releaseVideoPlayer();
        }
    }
}
