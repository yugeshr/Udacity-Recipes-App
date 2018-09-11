package ralli.yugesh.com.recipesapp.ui;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ralli.yugesh.com.recipesapp.R;
import ralli.yugesh.com.recipesapp.model.Step;
import ralli.yugesh.com.recipesapp.utils.StepStatePagerAdapter;
import timber.log.Timber;

public class RecipeStepActivity extends AppCompatActivity {

    private static final String BUNDLE_TEXT = "bundle";
    private PagerAdapter pagerAdapter;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @SuppressLint("ResourceType")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
        viewPager.setOffscreenPageLimit(1);
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
