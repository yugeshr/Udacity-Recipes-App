package ralli.yugesh.com.recipesapp.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ralli.yugesh.com.recipesapp.IngredientAdapter;
import ralli.yugesh.com.recipesapp.R;
import ralli.yugesh.com.recipesapp.StepAdapter;
import ralli.yugesh.com.recipesapp.model.Ingredient;
import ralli.yugesh.com.recipesapp.model.Step;

public class RecipeDetailFragment extends Fragment {
    @BindView(R.id.rv_recipeIngredientsView)
    RecyclerView recipeIngredientsRecyclerView;

    @BindView(R.id.rv_recipeStepsView)
    RecyclerView recipeStepsRecyclerView;

    private IngredientAdapter ingredientAdapter;
    private StepAdapter stepAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipedetail,container,false);
        ButterKnife.bind(this,view);

        List<Ingredient> ingredientList = (List<Ingredient>) getArguments().getSerializable("ingredients");
        List<Step> stepList = (List<Step>) getArguments().getSerializable("steps");
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
        stepAdapter = new StepAdapter(getContext(),stepList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recipeStepsRecyclerView.setLayoutManager(layoutManager);
        recipeStepsRecyclerView.setAdapter(stepAdapter);
    }
}
