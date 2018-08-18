package ralli.yugesh.com.recipesapp.utils;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import java.util.List;

import ralli.yugesh.com.recipesapp.model.Step;
import ralli.yugesh.com.recipesapp.ui.RecipeStepFragment;

public class StepStatePagerAdapter extends FragmentStatePagerAdapter {

    private List<Step> steps;
    private Bundle bundle;
    private RecipeStepFragment recipeStepFragment;

    public StepStatePagerAdapter(FragmentManager fm, List<Step> steps,Bundle bundle) {
        super(fm);
        this.steps = steps;
        this.bundle = bundle;
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("Adapter", String.valueOf(position));
        bundle.putSerializable("steps",steps.get(position));
        return RecipeStepFragment.newInstance(bundle);
    }

    @Override
    public int getCount() {
        return steps.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return "Step: "+steps.get(position).getId();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
