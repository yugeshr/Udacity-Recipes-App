package ralli.yugesh.com.recipesapp.ui;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ScrollView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ralli.yugesh.com.recipesapp.R;
import ralli.yugesh.com.recipesapp.RecipeWidgetProvider;
import ralli.yugesh.com.recipesapp.model.Ingredient;
import ralli.yugesh.com.recipesapp.model.Step;
import ralli.yugesh.com.recipesapp.utils.IngredientAdapter;
import ralli.yugesh.com.recipesapp.utils.StepAdapter;

public class RecipeDetailActivity extends AppCompatActivity implements StepAdapter.StepAdapterOnClickHandler{

    private static final String ACTION_RECIPEWIDGET = "ACTION_RECIPEWIDGET";

    @BindView(R.id.rv_recipeIngredientsView)
    RecyclerView recipeIngredientsRecyclerView;

    @BindView(R.id.rv_recipeStepsView)
    RecyclerView recipeStepsRecyclerView;

    ScrollView scrollView;

    private FragmentManager fragmentManager;
    private RecipeStepFragment  recipeStepFragment;

    private IngredientAdapter ingredientAdapter;
    private StepAdapter stepAdapter;

    private String mTitle;

    List<Ingredient> ingredientList;
    List<Step> stepList;

    Bundle bundle;

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

        //noinspection unchecked
        ingredientList = (List<Ingredient>) bundle.getSerializable("ingredients");
        //noinspection unchecked
        stepList = (List<Step>) bundle.getSerializable("steps");

        mTitle = bundle.getString("title");
        setTitle(mTitle);
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

        bundle = new Bundle();
        bundle.putSerializable("steps",selectedStep);
        bundle.putSerializable("stepsList", (Serializable) stepList);
        bundle.putString("title",mTitle);

        int orientation = getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_PORTRAIT ){
            bundle.putBoolean("flag",false);

            Intent intent = new Intent(this,RecipeStepActivity.class);
            intent.putExtra("bundle",bundle);
            startActivity(intent);

        }else {
            bundle.putBoolean("flag",true);
            recipeStepFragment = new RecipeStepFragment();
            recipeStepFragment.setArguments(bundle);
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.recipeStepContainer,recipeStepFragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add){
            bundle = new Bundle();
            bundle.putString("recipeName",mTitle);
            bundle.putSerializable("ingredients", (Serializable) ingredientList);

            Intent intent = new Intent(getApplicationContext(), RecipeWidgetProvider.class);
            intent.putExtra("bundle",bundle);
            intent.setAction(ACTION_RECIPEWIDGET);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            try {
                pendingIntent.send(getApplicationContext(),0,intent);
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }
}
