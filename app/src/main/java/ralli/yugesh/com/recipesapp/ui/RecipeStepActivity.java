package ralli.yugesh.com.recipesapp.ui;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ralli.yugesh.com.recipesapp.R;
import ralli.yugesh.com.recipesapp.model.Step;
import ralli.yugesh.com.recipesapp.utils.StepStatePagerAdapter;

public class RecipeStepActivity extends FragmentActivity {

    private FragmentManager fragmentManager;
    private RecipeStepFragment  recipeStepFragment;
    private PagerAdapter pagerAdapter;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.tabs)
    TabLayout tabLayout;

    private String TAG = "RecipeStepActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);

        ButterKnife.bind(this);
        Bundle bundle;
        if (getIntent().hasExtra("bundle")) {
            bundle = getIntent().getBundleExtra("bundle");
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras bundle");
        }

        List<Step> stepList = (List<Step>) bundle.getSerializable("stepsList");
        setTitle(bundle.getString("title"));

        pagerAdapter = new StepStatePagerAdapter(getSupportFragmentManager(),stepList,bundle);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setCurrentItem(0);
        tabLayout.setupWithViewPager(viewPager);
    }
}
