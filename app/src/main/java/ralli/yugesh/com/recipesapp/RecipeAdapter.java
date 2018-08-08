package ralli.yugesh.com.recipesapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import ralli.yugesh.com.recipesapp.model.RecipeList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>{

    private List<RecipeList> dataList;
    private Context context;

    private final RecipeAdapterOnClickHandler mClickHandler;

    public interface RecipeAdapterOnClickHandler {
        void onClick(RecipeList selectedRecipe);
    }

    public RecipeAdapter(Context context, List<RecipeList> dataList,RecipeAdapterOnClickHandler mClickHandler){
        this.dataList = dataList;
        this.context = context;
        this.mClickHandler = mClickHandler;
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView recipeNameTextView;
        TextView recipeServingTextView;
        ImageView iconImageView;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            recipeNameTextView = itemView.findViewById(R.id.tv_recipeName);
            recipeServingTextView = itemView.findViewById(R.id.tv_recipeServing);
            iconImageView = itemView.findViewById(R.id.iv_icon);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(dataList.get(adapterPosition));
        }
    }

    @NonNull
    @Override
    public RecipeAdapter.RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.recipe_list_item,viewGroup,false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.RecipeViewHolder holder, final int position) {
        holder.recipeNameTextView.setText(dataList.get(position).getName());
        holder.recipeServingTextView.setText("Servings: "+dataList.get(position).getServings());
        Glide.with(holder.itemView).load(R.drawable.icon).into(holder.iconImageView);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}

