package ralli.yugesh.com.recipesapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ralli.yugesh.com.recipesapp.model.Step;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {

    private List<Step> dataList;
    private Context context;

    public StepAdapter(Context context, List<Step> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    public class StepViewHolder extends RecyclerView.ViewHolder {
        TextView stepShortDescriptionTextView;
        public StepViewHolder(View itemView) {
            super(itemView);
            stepShortDescriptionTextView = itemView.findViewById(R.id.tv_stepShortDescription);
        }
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.step_list_item,viewGroup,false);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
        String stepShortDescriptionString = dataList.get(position).getId() +"."+ dataList.get(position).getShortDescription();
        holder.stepShortDescriptionTextView.setText(stepShortDescriptionString);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
