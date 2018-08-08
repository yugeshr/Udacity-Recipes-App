package ralli.yugesh.com.recipesapp.network;

import java.util.List;
import ralli.yugesh.com.recipesapp.model.RecipeList;
import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipeService {
    @GET("baking.json")
    Call<List<RecipeList>> getRecipeList();
}