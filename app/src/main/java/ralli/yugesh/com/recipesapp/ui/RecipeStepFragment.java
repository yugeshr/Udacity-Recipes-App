package ralli.yugesh.com.recipesapp.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
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
    ImageButton fullscreen;

    private String stepVideoUrl;
    private static final String KEY_WINDOW = "window";
    private static final String KEY_POSITION = "position";
    private static final String KEY_AUTO_PLAY = "auto_play";

    private PlayerView mPlayerView;

    private Boolean flag;
    private int stepId;
    private boolean back;
    private String TAG = "FragmentStep";
    private boolean startAutoPlay;
    private int startWindow;
    private long startPosition;

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
        View view = inflater.inflate(R.layout.fragment_step, container, false);

        assert getArguments() != null;
        Step step = (Step) getArguments().getSerializable("steps");
        assert step != null;
        stepVideoUrl = step.getVideoURL();
        String stepTitle = step.getShortDescription();
        stepId = step.getId();

        int stepSize = getArguments().getInt("size");
        flag = getArguments().getBoolean("flag");

        ButterKnife.bind(this, view);
        mPlayerView = (PlayerView) view.findViewById(R.id.playerView);

        if (!stepVideoUrl.equals("")){
            mPlayerView.setVisibility(View.VISIBLE);
            initializePlayer();
        }else {
            //Video missing
            mPlayerView.setVisibility(View.GONE);
        }

        titleTextView.setText(stepId+". "+ stepTitle);
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

            if (stepId== stepSize -1){
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
                bundle.putLong("position",startPosition);
                bundle.putBoolean("state",startAutoPlay);

                Intent intent = new Intent(getContext(), FullscreenActivity.class);
                intent.putExtra("bundle",bundle);
                startActivity(intent);
            }
        });

        if (savedInstanceState != null) {
            startAutoPlay = savedInstanceState.getBoolean(KEY_AUTO_PLAY);
            startWindow = savedInstanceState.getInt(KEY_WINDOW);
            startPosition = savedInstanceState.getLong(KEY_POSITION);
        }else {
            clearStartPosition();
        }

        return view;
    }

    private void clearStartPosition() {
        startAutoPlay = true;
        startWindow = C.INDEX_UNSET;
        startPosition = C.TIME_UNSET;
    }

    private void initializePlayer() {
        if (ExoPlayerVideoHandler.getInstance() == null){
            ExoPlayerVideoHandler.getInstance()
                    .prepareExoPlayerForUri(getContext(),
                            Uri.parse(stepVideoUrl), mPlayerView, startAutoPlay);
            ExoPlayerVideoHandler.getInstance().goToForeground();
            ExoPlayerVideoHandler.getInstance().goToPosition(startPosition);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT<=23){
            ExoPlayerVideoHandler.getInstance().releaseVideoPlayer();
            ExoPlayerVideoHandler.getInstance().goToBackground();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Build.VERSION.SDK_INT > 23){
            ExoPlayerVideoHandler.getInstance().releaseVideoPlayer();
            ExoPlayerVideoHandler.getInstance().goToBackground();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Build.VERSION.SDK_INT <= 23 || ExoPlayerVideoHandler.getInstance().getPlayer() == null)) {
            ExoPlayerVideoHandler.getInstance()
                    .prepareExoPlayerForUri(getContext(),
                            Uri.parse(stepVideoUrl), mPlayerView, startAutoPlay);
            ExoPlayerVideoHandler.getInstance().goToPosition(startPosition);
            ExoPlayerVideoHandler.getInstance().goToForeground();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(Build.VERSION.SDK_INT > 23){
            initializePlayer();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        updateStartPosition();
        outState.putBoolean(KEY_AUTO_PLAY, startAutoPlay);
        outState.putInt(KEY_WINDOW, startWindow);
        outState.putLong(KEY_POSITION, startPosition);
    }

    private void updateStartPosition() {
        if (ExoPlayerVideoHandler.getInstance().getPlayer() != null) {
            startAutoPlay = ExoPlayerVideoHandler.getInstance().getPlayer().getPlayWhenReady();
            startWindow = ExoPlayerVideoHandler.getInstance().getPlayer().getCurrentWindowIndex();
            startPosition = Math.max(0, ExoPlayerVideoHandler.getInstance().getPlayer().getContentPosition());
        }
    }
}
