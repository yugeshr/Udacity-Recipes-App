package ralli.yugesh.com.recipesapp.ui;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlayer;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Optional;
import ralli.yugesh.com.recipesapp.R;
import ralli.yugesh.com.recipesapp.model.Step;

import static ralli.yugesh.com.recipesapp.ui.FullscreenDialog.*;
import static ralli.yugesh.com.recipesapp.ui.FullscreenDialog.TAG;

public class RecipeStepFragment extends Fragment {

    @Nullable
    @BindView(R.id.tv_stepDescription)
    TextView descriptionTextView;

    private String stepVideoUrl;
    private String TAG = "RecipeStepFragment";

    private PlayerView mPlayerView;
    private SimpleExoPlayer mPlayer;

    private Boolean flag = false;
    private View view;

    private boolean isPlayerPlaying;

    public RecipeStepFragment(){

    }

    public static RecipeStepFragment newInstance(Bundle bundle){
        RecipeStepFragment recipeStepFragment = new RecipeStepFragment();
        recipeStepFragment.setArguments(bundle);
        return recipeStepFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_step,container,false);

        Step step = (Step) getArguments().getSerializable("steps");
        stepVideoUrl = step.getVideoURL();
        flag = getArguments().getBoolean("flag");

        ButterKnife.bind(this,view);
        mPlayerView = view.findViewById(R.id.exoPlayerView);

        if (!stepVideoUrl.equals("")){
            mPlayerView.setVisibility(View.VISIBLE);
            initializePlayer(stepVideoUrl);
        }else {
            mPlayerView.setVisibility(View.GONE);
        }

        descriptionTextView.setText(step.getDescription());

        return view;
    }

    private void initializePlayer(String stepVideoUrl) {
        if (mPlayer == null){
            TrackSelector trackSelector = new DefaultTrackSelector();
            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                    Util.getUserAgent(getContext(), "RecipesApp"), bandwidthMeter);

            Uri uri = Uri.parse(stepVideoUrl);
            mPlayer = ExoPlayerFactory.newSimpleInstance(getContext(),trackSelector);
            mPlayerView.setPlayer(mPlayer);
            //Prepare media source
            MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
            mPlayer.prepare(mediaSource);
            mPlayer.setPlayWhenReady(true);
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

    public void goToBackground(){
        if(mPlayer != null){
            isPlayerPlaying = mPlayer.getPlayWhenReady();
            mPlayer.setPlayWhenReady(false);
        }
    }

    public void goToForeground(){
        if(mPlayer != null){
           mPlayer.setPlayWhenReady(isPlayerPlaying);
        }
    }

    private void hideSystemUi(){
        mPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                |View.SYSTEM_UI_FLAG_LOW_PROFILE
                |View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }
    /*private void initFullscreenDialog() {

        mFullScreenDialog = new Dialog(getActivity().getApplicationContext()) {
            public void onBackPressed() {
                if (mExoPlayerFullscreen)
                    closeFullscreenDialog();
                super.onBackPressed();
            }
        };
    }

    private void openFullscreenDialog() {
        ((ViewGroup)mPlayerView.getParent()).removeView(mPlayerView);
        mFullScreenDialog.addContentView(mPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mExoPlayerFullscreen = true;
        mFullScreenDialog.show();
    }

    private void closeFullscreenDialog() {
        ((ViewGroup)mPlayerView.getParent()).removeView(mPlayerView);
        ((FrameLayout) view.findViewById(R.id.frame)).addView(mPlayerView);
        mExoPlayerFullscreen = false;
        mFullScreenDialog.dismiss();
    }*/
}
