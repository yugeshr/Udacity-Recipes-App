package ralli.yugesh.com.recipesapp;

import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ralli.yugesh.com.recipesapp.model.RecipeList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeAdapterOnClickHandler{

    @BindView(R.id.rv_recipesView)
    RecyclerView recipesRecyclerView;

    private RecipeAdapter recipeAdapter;
    private String TAG = "Log";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        getRecipes();
    }

    private void getRecipes() {

        final RecipeService recipeService = RecipeApi.getRecipeService().create(RecipeService.class);
        Call<List<RecipeList>> call = recipeService.getRecipeList();
        call.enqueue(new Callback<List<RecipeList>>() {
            @Override
            public void onResponse(@NonNull Call<List<RecipeList>> call, @NonNull Response<List<RecipeList>> response) {
                generateRecipeList(response.body());
                Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<RecipeList>> call, Throwable t) {
                Log.d(TAG,t.getMessage());
                Toast.makeText(getApplicationContext(),"Error Occurred, Try again later!",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void generateRecipeList(List<RecipeList> recipeList) {
        recipeAdapter = new RecipeAdapter(getApplicationContext(),recipeList,this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recipesRecyclerView.setLayoutManager(layoutManager);
        recipesRecyclerView.setAdapter(recipeAdapter);
    }

    @Override
    public void onClick(RecipeList selectedRecipe) {
        Toast.makeText(getApplicationContext(),"Clicked "+selectedRecipe.getName(),Toast.LENGTH_SHORT).show();
    }
}
