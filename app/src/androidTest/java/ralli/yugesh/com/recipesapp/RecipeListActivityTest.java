package ralli.yugesh.com.recipesapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ralli.yugesh.com.recipesapp.ui.RecipeListActivity;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.anything;

@RunWith(AndroidJUnit4.class)
public class RecipeListActivityTest {
    @Rule
    public ActivityTestRule<RecipeListActivity> activityTestRule = new ActivityTestRule<>(RecipeListActivity.class);

    @Test
    public void clickRecipe(){
        Espresso.onData(anything()).inAdapterView(withId(R.id.rv_recipesView)).atPosition(1).perform(click());
        Espresso.onView(withId(R.id.tv_ingre_text)).check(ViewAssertions.matches(ViewMatchers.withText("Ingredients:")));
    }
}
