package ralli.yugesh.com.recipesapp.ui;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import ralli.yugesh.com.recipesapp.R;
import ralli.yugesh.com.recipesapp.model.Ingredient;
import ralli.yugesh.com.recipesapp.model.Step;
import ralli.yugesh.com.recipesapp.utils.IngredientAdapter;
import ralli.yugesh.com.recipesapp.utils.StepAdapter;

public class RecipeDetailActivity extends AppCompatActivity implements StepAdapter.StepAdapterOnClickHandler{

    @BindView(R.id.rv_recipeIngredientsView)
    RecyclerView recipeIngredientsRecyclerView;

    @BindView(R.id.rv_recipeStepsView)
    RecyclerView recipeStepsRecyclerView;

    private IngredientAdapter ingredientAdapter;
    private StepAdapter stepAdapter;

    List<Ingredient> ingredientList;
    List<Step> stepList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        ButterKnife.bind(this);

        Bundle bundle;
        if (getIntent().hasExtra("bundle")) {
           bundle = getIntent().getBundleExtra("bundle");
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras bundle");
        }

        ingredientList = (List<Ingredient>) bundle.getSerializable("ingredients");
        stepList = (List<Step>) bundle.getSerializable("steps");
/*        String title = bundle.getString("title");
        setTitle(title);*/

        setIngredients(ingredientList);
        setSteps(stepList);
    }

    private void setIngredients(List<Ingredient> ingredientList) {
        ingredientAdapter = new IngredientAdapter(getApplicationContext(),ingredientList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recipeIngredientsRecyclerView.setLayoutManager(layoutManager);
        recipeIngredientsRecyclerView.setAdapter(ingredientAdapter);
    }

    private void setSteps(List<Step> stepList) {
        stepAdapter = new StepAdapter(getApplicationContext(),stepList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recipeStepsRecyclerView.setLayoutManager(layoutManager);
        recipeStepsRecyclerView.setAdapter(stepAdapter);
    }

    @Override
    public void onClick(Step selectedStep) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("steps",selectedStep);

        Intent intent = new Intent(getApplicationContext(),RecipeStepActivity.class);
        intent.putExtra("bundle",bundle);
        startActivity(intent);
    }
}
