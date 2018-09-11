package ralli.yugesh.com.recipesapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ralli.yugesh.com.recipesapp.ui.RecipeListActivity;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;

@RunWith(AndroidJUnit4.class)
public class RecipeListActivityIntentTest {

    private static final String EXTRA_RECIPE_ID_KEY = "RECIPE_ID";
    private static final String EXTRA_RECIPE_ID_VALUE = "RECIPE";

    @Rule
    public IntentsTestRule<RecipeListActivity> intentsTestRule
            = new IntentsTestRule<>(RecipeListActivity.class);

    @Test
    public void clickRecipeDetailsActivityIntent() {

        Espresso.onView(ViewMatchers.withId(R.id.rv_recipesView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        intended(
                hasExtra(EXTRA_RECIPE_ID_KEY, EXTRA_RECIPE_ID_VALUE)
        );
    }
}
