package ralli.yugesh.com.recipesapp.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import ralli.yugesh.com.recipesapp.R;
import ralli.yugesh.com.recipesapp.model.Step;

public class RecipeStepFragment extends Fragment {

    @BindView(R.id.tv_stepDescription)
    TextView descriptionTextView;

    private String stepVideoUrl;
    private String TAG = "RecipeStepFragment";

    private PlayerView mPlayerView;
    private SimpleExoPlayer mPlayer;


    public RecipeStepFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step,container,false);
        Step step = (Step) getArguments().getSerializable("steps");
        stepVideoUrl = step.getVideoURL();
        ButterKnife.bind(this,view);
        mPlayerView = view.findViewById(R.id.exoPlayerView);

        if (!stepVideoUrl.equals("")){
            mPlayerView.setVisibility(View.VISIBLE);
            initializePlayer(stepVideoUrl);
        }else {
            Log.d(TAG,"Video missing");
            mPlayerView.setVisibility(View.GONE);
        }

        getActivity().setTitle(step.getShortDescription());
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
}
