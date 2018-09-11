package ralli.yugesh.com.recipesapp.utils;

import android.content.Context;
import android.net.Uri;
import android.view.SurfaceView;

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

public class ExoPlayerVideoHandler {

    private static ExoPlayerVideoHandler instance;

    public static ExoPlayerVideoHandler getInstance(){
        if(instance == null){
            instance = new ExoPlayerVideoHandler();
        }
        return instance;
    }

    private SimpleExoPlayer player;
    private Uri playerUri;
    private boolean isPlayerPlaying;

    private ExoPlayerVideoHandler(){}

    public void prepareExoPlayerForUri(Context context, Uri uri,
                                       PlayerView exoPlayerView, Boolean playerReady){
        if(context != null && uri != null && exoPlayerView != null){
            if(!uri.equals(playerUri) || player == null){
                // Create a new player if the player is null or
                TrackSelector trackSelector = new DefaultTrackSelector();
                DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                        Util.getUserAgent(context, "RecipesApp"), bandwidthMeter);
                // we want to play a new video
                playerUri = uri;
                // Do all the standard ExoPlayer code here...
                player = ExoPlayerFactory.newSimpleInstance(context,trackSelector);
                exoPlayerView.setPlayer(player);
                player.setPlayWhenReady(playerReady);
                //Prepare media source
                MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
                // Prepare the player with the source.
                player.prepare(mediaSource);
            }
            player.clearVideoSurface();
            player.setVideoSurfaceView(
                    (SurfaceView)exoPlayerView.getVideoSurfaceView());
            player.seekTo(player.getCurrentPosition() + 1);
            exoPlayerView.setPlayer(player);
        }
    }

    public void releaseVideoPlayer(){
        if(player != null)
        {
            player.release();
        }
        player = null;
    }

    public void goToBackground(){
        if(player != null){
            isPlayerPlaying = player.getPlayWhenReady();
            player.setPlayWhenReady(false);
        }
    }

    public void goToForeground(){
        if(player != null){
            player.setPlayWhenReady(isPlayerPlaying);
        }
    }

    public void goToPosition(Long position){
        player.seekTo(position);
    }

    public long getCurrentPosition(){
        return player.getCurrentPosition();
    }

    public Boolean getPlayWhenReady(){
        return player.getPlayWhenReady();
    }

    public SimpleExoPlayer getPlayer(){
        return player;
    }
}
