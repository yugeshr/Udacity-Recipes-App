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

import com.google.android.exoplayer2.ui.PlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ralli.yugesh.com.recipesapp.R;
import ralli.yugesh.com.recipesapp.model.Step;
import ralli.yugesh.com.recipesapp.utils.ExoPlayerVideoHandler;

public class RecipeStepFragment extends Fragment {

    @Nullable
    @BindView(R.id.tv_stepDescription)
    TextView descriptionTextView;

    @BindView(R.id.tv_stepTitle)
    TextView titleTextView;

    @BindView(R.id.exo_fullscreen)
    Button fullscreen;

    private String stepVideoUrl;
    private static final String CURRENT_POSITION = "current_position";
    private static final String PLAYER_READY="Player_Ready";

    private PlayerView mPlayerView;

    private long position;
    private boolean playWhenReady;
    private boolean destroyVideo = true;

    private Boolean flag;
    private View view;
    private String stepTitle;
    private int stepId;
    private boolean back;
    private int stepSize;
    private long playerPosition;
    private boolean getPlayerWhenReady;

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
        mPlayerView = (PlayerView) view.findViewById(R.id.playerView);

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

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            Button btnPrevious = view.findViewById(R.id.btn_previous);
            Button btnNext = view.findViewById(R.id.btn_next);

            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((RecipeStepActivity) getActivity()).setCurrentItem(stepId+1,true);
                }
            });

            if (stepId==stepSize-1){
                btnNext.setVisibility(View.GONE);
            }

            if (stepId==0){
                btnPrevious.setVisibility(View.GONE);
            }

            btnPrevious.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((RecipeStepActivity) getActivity()).setCurrentItem(stepId-1,true);
                }
            });
        }

        fullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("url",stepVideoUrl);
                bundle.putLong("position",playerPosition);
                bundle.putBoolean("state",getPlayerWhenReady);

                Intent intent = new Intent(getContext(), FullscreenActivity.class);
                intent.putExtra("bundle",bundle);
                startActivity(intent);
            }
        });

        return view;
    }

    private void initializePlayer() {
        if (ExoPlayerVideoHandler.getInstance() == null){
            ExoPlayerVideoHandler.getInstance()
                    .prepareExoPlayerForUri(getContext(),
                            Uri.parse(stepVideoUrl), mPlayerView, getPlayerWhenReady);
            ExoPlayerVideoHandler.getInstance().goToForeground();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        ExoPlayerVideoHandler.getInstance().goToBackground();
    }

    @Override
    public void onDestroyView() {
        ExoPlayerVideoHandler.getInstance().releaseVideoPlayer();
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        ExoPlayerVideoHandler.getInstance()
                .prepareExoPlayerForUri(getContext(),
                        Uri.parse(stepVideoUrl), mPlayerView,getPlayerWhenReady);
        ExoPlayerVideoHandler.getInstance().goToPosition(playerPosition);
        ExoPlayerVideoHandler.getInstance().goToForeground();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        playerPosition = ExoPlayerVideoHandler.getInstance().getCurrentPosition();
        outState.putLong(CURRENT_POSITION, playerPosition );

        getPlayerWhenReady = ExoPlayerVideoHandler.getInstance().getPlayWhenReady();
        outState.putBoolean(PLAYER_READY, getPlayerWhenReady);

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null){
            playerPosition = savedInstanceState.getLong(CURRENT_POSITION);
            getPlayerWhenReady = savedInstanceState.getBoolean(PLAYER_READY);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            playerPosition = savedInstanceState.getLong(CURRENT_POSITION);
            getPlayerWhenReady = savedInstanceState.getBoolean(PLAYER_READY);
        }
    }

}
