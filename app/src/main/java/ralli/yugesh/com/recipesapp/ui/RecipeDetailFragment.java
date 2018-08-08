package ralli.yugesh.com.recipesapp.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import  android.support.v7.widget.Toolbar;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import ralli.yugesh.com.recipesapp.utils.IngredientAdapter;
import ralli.yugesh.com.recipesapp.R;
import ralli.yugesh.com.recipesapp.utils.StepAdapter;
import ralli.yugesh.com.recipesapp.model.Ingredient;
import ralli.yugesh.com.recipesapp.model.Step;

public class RecipeDetailFragment extends Fragment implements StepAdapter.StepAdapterOnClickHandler{
    @BindView(R.id.rv_recipeIngredientsView)
    RecyclerView recipeIngredientsRecyclerView;

    @BindView(R.id.rv_recipeStepsView)
    RecyclerView recipeStepsRecyclerView;

    private IngredientAdapter ingredientAdapter;
    private StepAdapter stepAdapter;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipedetail,container,false);
        ButterKnife.bind(this,view);

        List<Ingredient> ingredientList = (List<Ingredient>) getArguments().getSerializable("ingredients");
        List<Step> stepList = (List<Step>) getArguments().getSerializable("steps");
        String title = getArguments().getString("title");
        Objects.requireNonNull(getActivity()).setTitle(title);

        setIngredients(ingredientList);
        setSteps(stepList);
        return view;
    }

    private void setIngredients(List<Ingredient> ingredientList) {
        ingredientAdapter = new IngredientAdapter(getContext(),ingredientList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recipeIngredientsRecyclerView.setLayoutManager(layoutManager);
        recipeIngredientsRecyclerView.setAdapter(ingredientAdapter);
    }

    private void setSteps(List<Step> stepList) {
        stepAdapter = new StepAdapter(getContext(),stepList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recipeStepsRecyclerView.setLayoutManager(layoutManager);
        recipeStepsRecyclerView.setAdapter(stepAdapter);
    }

    @Override
    public void onClick(Step selectedStep) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("steps",selectedStep);
        RecipeStepFragment  recipeStepFragment = new RecipeStepFragment();
        recipeStepFragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container,recipeStepFragment)
                .addToBackStack(null)
                .commit();
    }
}
