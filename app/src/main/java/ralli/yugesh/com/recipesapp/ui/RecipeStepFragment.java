package ralli.yugesh.com.recipesapp.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import timber.log.Timber;

import static android.app.Activity.RESULT_OK;

public class RecipeStepFragment extends Fragment {

    @Nullable
    @BindView(R.id.tv_stepDescription)
    TextView descriptionTextView;

    @BindView(R.id.tv_stepTitle)
    TextView titleTextView;

    @BindView(R.id.btn_previous)
    Button btnPrevious;

    @BindView(R.id.btn_next)
    Button btnNext;

    private String stepVideoUrl;
    private static final String CURRENT_POSITION = "current_position";
    private static final String PLAYER_READY="Player_Ready";

    private PlayerView mPlayerView;
    private SimpleExoPlayer mPlayer;

    private long position;
    private boolean playWhenReady;

    private Boolean flag;
    private View view;
    private String stepTitle;
    private int stepId;
    private boolean back;
    private int stepSize;

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

        assert getArguments() != null;
        Step step = (Step) getArguments().getSerializable("steps");
        assert step != null;
        stepVideoUrl = step.getVideoURL();
        stepTitle = step.getShortDescription();
        stepId = step.getId();

        stepSize = getArguments().getInt("size");
        flag = getArguments().getBoolean("flag");

        ButterKnife.bind(this,view);
        mPlayerView = view.findViewById(R.id.exoPlayerView);

        if (!stepVideoUrl.equals("")){
            mPlayerView.setVisibility(View.VISIBLE);
            initializePlayer();
        }else {
            //Video missing
            mPlayerView.setVisibility(View.GONE);
        }

        titleTextView.setText(stepId+". "+stepTitle);
        descriptionTextView.setText(step.getDescription());

        back = true;
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);

        if (stepId==stepSize-1){
            btnNext.setVisibility(View.GONE);
        }

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((RecipeStepActivity) getActivity()).setCurrentItem(stepId+1,true);
            }
        });

        if (stepId==0){
            btnPrevious.setVisibility(View.GONE);
        }

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((RecipeStepActivity) getActivity()).setCurrentItem(stepId-1,true);
            }
        });
        return view;
    }

    private void initializePlayer() {
        if (mPlayer == null){
            TrackSelector trackSelector = new DefaultTrackSelector();
            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                    Util.getUserAgent(getContext(), "RecipesApp"), bandwidthMeter);

            Uri uri = Uri.parse(stepVideoUrl);
            mPlayer = ExoPlayerFactory.newSimpleInstance(getContext(),trackSelector);
            mPlayerView.setPlayer(mPlayer);
            mPlayer.setPlayWhenReady(playWhenReady);
            mPlayer.seekTo(position);
            //Prepare media source
            MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
            mPlayer.prepare(mediaSource);
        }
    }

    private void releasePlayer() {
        if (mPlayer != null) {
            position = mPlayer.getCurrentPosition();
            playWhenReady=mPlayer.getPlayWhenReady();
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();

    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();

    }

    @Override
    public void onResume() {
        super.onResume();
        Timber.d("onResume()");
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ){
            Timber.d("Portrait");
        }
        if (!flag && back && getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }

                long position = mPlayer.getCurrentPosition();
                mPlayer.stop();

                Bundle bundle = new Bundle();
                bundle.putString("url",stepVideoUrl);
                bundle.putLong("position",position);

                Intent intent = new Intent(getContext(), FullscreenActivity.class);
                intent.putExtra("bundle",bundle);
                startActivityForResult(intent,1);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            if (resultCode == RESULT_OK){
                back = false;
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        }
    }
}
