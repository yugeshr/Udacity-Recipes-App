package ralli.yugesh.com.recipesapp;

import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ralli.yugesh.com.recipesapp.ui.RecipeListActivity;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;

@RunWith(AndroidJUnit4.class)
public class RecipeListActivityUITest {
    @Rule
    public ActivityTestRule<RecipeListActivity> activityTestRule = new ActivityTestRule<RecipeListActivity>(RecipeListActivity.class);

    @Test
    public void hasData(){
        Espresso.onData(hasEntry(equalTo(R.id.tv_recipeName),"Nutella Pie"));
    }
}
