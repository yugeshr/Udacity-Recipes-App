package ralli.yugesh.com.recipesapp.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ralli.yugesh.com.recipesapp.R;
import ralli.yugesh.com.recipesapp.model.Step;
import ralli.yugesh.com.recipesapp.utils.StepStatePagerAdapter;
import timber.log.Timber;

public class RecipeStepActivity extends FragmentActivity {

    private static final String BUNDLE_TEXT = "bundle";
    private PagerAdapter pagerAdapter;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

/*    @BindView(R.id.tabs)
    TabLayout tabLayout;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);

        ButterKnife.bind(this);
        Bundle bundle;
        if (getIntent().hasExtra(BUNDLE_TEXT)) {
            bundle = getIntent().getBundleExtra(BUNDLE_TEXT);
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras bundle");
        }

        Step selectedStep = (Step) bundle.getSerializable("steps");
        if (selectedStep != null) {
            Timber.d(selectedStep.getShortDescription());
        }

        Boolean flag = bundle.getBoolean("flag");
        Timber.d(String.valueOf(flag));

        //noinspection unchecked
        List<Step> stepList = (List<Step>) bundle.getSerializable("stepsList");
        setTitle(bundle.getString("title"));

        pagerAdapter = new StepStatePagerAdapter(getSupportFragmentManager(),stepList,flag);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(selectedStep.getId());
        viewPager.setOffscreenPageLimit(3);
        //tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            //tabLayout.setVisibility(View.GONE);
        }
    }

    public void setCurrentItem (int item, boolean smoothScroll) {
        viewPager.setCurrentItem(item, smoothScroll);
    }
}
