package ralli.yugesh.com.recipesapp.ui;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import ralli.yugesh.com.recipesapp.R;

public class RecipeStepActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);

        Bundle bundle;
        if (getIntent().hasExtra("bundle")) {
            bundle = getIntent().getBundleExtra("bundle");
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras bundle");
        }

        RecipeStepFragment  recipeStepFragment = new RecipeStepFragment();
        recipeStepFragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.recipeStepContainer,recipeStepFragment)
                .addToBackStack(null)
                .commit();
    }
}
