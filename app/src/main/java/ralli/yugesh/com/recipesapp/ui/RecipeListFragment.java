package ralli.yugesh.com.recipesapp.ui;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.annotation.GlideModule;

import java.io.Serializable;
import java.util.List;
import ralli.yugesh.com.recipesapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import ralli.yugesh.com.recipesapp.model.Ingredient;
import ralli.yugesh.com.recipesapp.model.Step;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import ralli.yugesh.com.recipesapp.RecipeAdapter;
import ralli.yugesh.com.recipesapp.RecipeApi;
import ralli.yugesh.com.recipesapp.RecipeService;
import ralli.yugesh.com.recipesapp.model.RecipeList;


public class RecipeListFragment extends Fragment implements RecipeAdapter.RecipeAdapterOnClickHandler {

    @BindView(R.id.rv_recipesView)
    RecyclerView recipesRecyclerView;

    private RecipeAdapter recipeAdapter;
    private String TAG = "Log";

    public RecipeListFragment(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipelist,container,false);
        ButterKnife.bind(this,view);

        getRecipes();

        return view;
    }

    private void getRecipes() {

        final RecipeService recipeService = RecipeApi.getRecipeService().create(RecipeService.class);
        Call<List<RecipeList>> call = recipeService.getRecipeList();
        call.enqueue(new Callback<List<RecipeList>>() {
            @Override
            public void onResponse(@NonNull Call<List<RecipeList>> call, @NonNull Response<List<RecipeList>> response) {
                generateRecipeList(response.body());
            }

            @Override
            public void onFailure(Call<List<RecipeList>> call, Throwable t) {
                Log.d(TAG,t.getMessage());
                Toast.makeText(getContext(),"Error Occurred, Try again later!",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void generateRecipeList(List<RecipeList> recipeList) {
        recipeAdapter = new RecipeAdapter(getContext(),recipeList,this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recipesRecyclerView.setLayoutManager(layoutManager);
        recipesRecyclerView.setAdapter(recipeAdapter);
    }

    @Override
    public void onClick(RecipeList selectedRecipe) {
        List<Ingredient> ingredientList = selectedRecipe.getIngredients();
        List<Step> stepList = selectedRecipe.getSteps();

        Bundle bundle = new Bundle();
        bundle.putSerializable("ingredients", (Serializable) ingredientList);
        bundle.putSerializable("steps",(Serializable) stepList);
        RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
        recipeDetailFragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.container,recipeDetailFragment)
                .addToBackStack(null)
                .commit();
    }
}
