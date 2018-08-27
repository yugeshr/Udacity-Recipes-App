package ralli.yugesh.com.recipesapp.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

import ralli.yugesh.com.recipesapp.R;
import ralli.yugesh.com.recipesapp.model.Ingredient;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private final List<Ingredient> dataList;
    private final Context context;

    public IngredientAdapter( Context context,List<Ingredient> dataList) {
        this.dataList = dataList;
        this.context = context;
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {
        final TextView ingredientTextView;
        public IngredientViewHolder(View itemView) {
            super(itemView);
            ingredientTextView = itemView.findViewById(R.id.tv_ingredient);

        }
    }
    @NonNull
    @Override
    public IngredientAdapter.IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.ingredient_list_item,viewGroup,false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientAdapter.IngredientViewHolder holder, int position) {
        String ingredientString = dataList.get(position).getIngredient()
                +"("+dataList.get(position).getQuantity()+" "+dataList.get(position).getMeasure()+")";
        holder.ingredientTextView.setText(ingredientString);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
