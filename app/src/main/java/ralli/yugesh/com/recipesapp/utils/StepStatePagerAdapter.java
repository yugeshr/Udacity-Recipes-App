package ralli.yugesh.com.recipesapp.utils;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.util.List;

import ralli.yugesh.com.recipesapp.model.Step;
import ralli.yugesh.com.recipesapp.ui.RecipeStepFragment;

public class StepStatePagerAdapter extends FragmentStatePagerAdapter {

    private final List<Step> steps;
    private final Boolean flag;
    private SparseArray<Fragment> sparseArray = new SparseArray<>();

    public StepStatePagerAdapter(FragmentManager fm, List<Step> steps,Boolean flag) {
        super(fm);
        this.steps = steps;
        this.flag = flag;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("steps",steps.get(position));
        bundle.putBoolean("flag",flag);
        bundle.putInt("size",steps.size());
        Log.d("Adapter--", String.valueOf(position));
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

    @NonNull
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container,position);
        sparseArray.put(position,fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        sparseArray.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return sparseArray.get(position);
    }
}
