package ralli.yugesh.com.recipesapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.Snackbar;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ralli.yugesh.com.recipesapp.R;
import ralli.yugesh.com.recipesapp.idlingresource.RecipeIdlingResource;
import ralli.yugesh.com.recipesapp.model.Ingredient;
import ralli.yugesh.com.recipesapp.model.RecipeList;
import ralli.yugesh.com.recipesapp.model.Step;
import ralli.yugesh.com.recipesapp.network.RecipeApi;
import ralli.yugesh.com.recipesapp.network.RecipeService;
import ralli.yugesh.com.recipesapp.utils.RecipeAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;


public class RecipeListActivity extends AppCompatActivity implements RecipeAdapter.RecipeAdapterOnClickHandler{

    @BindView(R.id.rv_recipesView)
    RecyclerView recipesRecyclerView;

    private RecipeAdapter recipeAdapter;

    private static final String EXTRA_RECIPE_ID_KEY = "RECIPE_ID";
    private static final String EXTRA_RECIPE_ID_VALUE = "RECIPE";

    Bundle bundle;
    private RelativeLayout relativeLayout;

    @Nullable
    private RecipeIdlingResource idlingResource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        ButterKnife.bind(this);
        relativeLayout = findViewById(R.id.relativeLayout);

        getRecipes();
    }

    private void getRecipes() {

        final RecipeService recipeService = RecipeApi.getRecipeService().create(RecipeService.class);
        Call<List<RecipeList>> call = recipeService.getRecipeList();
        call.enqueue(new Callback<List<RecipeList>>() {
            @Override
            public void onResponse(@NonNull Call<List<RecipeList>> call, @NonNull Response<List<RecipeList>> response) {
                generateRecipeList(response.body());
            }

            @Override
            public void onFailure(Call<List<RecipeList>> call, Throwable t) {
                Timber.d(t);
                Snackbar snackbar = Snackbar.make(relativeLayout,"Internet not working!",Snackbar.LENGTH_INDEFINITE)
                        .setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(RecipeListActivity.this,RecipeListActivity.class));
                                finish();
                            }
                        });
                snackbar.show();
            }
        });
    }

    private void generateRecipeList(List<RecipeList> recipeList) {
        recipeAdapter = new RecipeAdapter(getApplicationContext(),recipeList,this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recipesRecyclerView.setLayoutManager(layoutManager);
        recipesRecyclerView.setAdapter(recipeAdapter);
    }

    @Override
    public void onClick(RecipeList selectedRecipe) {
        String title = selectedRecipe.getName();
        List<Ingredient> ingredientList = selectedRecipe.getIngredients();
        List<Step> stepList = selectedRecipe.getSteps();

        bundle = new Bundle();
        bundle.putSerializable("ingredients", (Serializable) ingredientList);
        bundle.putSerializable("steps",(Serializable) stepList);
        bundle.putString("title",title);

        Intent intent = new Intent(getApplicationContext(),RecipeDetailActivity.class);
        intent.putExtra("bundle",bundle);
        intent.putExtra(EXTRA_RECIPE_ID_KEY,EXTRA_RECIPE_ID_VALUE);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle currentState) {
        super.onSaveInstanceState(currentState);
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (idlingResource == null) {
            idlingResource = new RecipeIdlingResource();
        }
        return idlingResource;
    }
}
