package ralli.yugesh.com.recipesapp.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ralli.yugesh.com.recipesapp.R;
import ralli.yugesh.com.recipesapp.model.Step;

public class RecipeStepFragment extends Fragment {

    @BindView(R.id.tv_stepDescription)
    TextView descriptionTextView;

    public RecipeStepFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step,container,false);
        Step step = (Step) getArguments().getSerializable("steps");

        ButterKnife.bind(this,view);

        descriptionTextView.setText(step.getDescription());
        return view;
    }
}
