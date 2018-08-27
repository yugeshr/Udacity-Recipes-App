package ralli.yugesh.com.recipesapp.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ralli.yugesh.com.recipesapp.R;
import ralli.yugesh.com.recipesapp.model.Step;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {

    private final List<Step> dataList;
    private final Context context;

    private final StepAdapterOnClickHandler mClickHandler;

    public interface StepAdapterOnClickHandler{
        void onClick(Step selectedStep);
    }

    public StepAdapter(Context context, List<Step> dataList, StepAdapterOnClickHandler mClickHandler) {
        this.context = context;
        this.dataList = dataList;
        this.mClickHandler = mClickHandler;
    }

    public class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final TextView stepShortDescriptionTextView;
        final TextView stepIdTextView;
        final ImageView stepOpenView;

        public StepViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            stepShortDescriptionTextView = itemView.findViewById(R.id.tv_stepShortDescription);
            stepIdTextView = itemView.findViewById(R.id.tv_stepId);
            stepOpenView = itemView.findViewById(R.id.iv_stepOpen);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(dataList.get(adapterPosition));
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
        String stepIdString = dataList.get(position).getId() +".";
        String stepShortDescriptionString = dataList.get(position).getShortDescription();
        holder.stepIdTextView.setText(stepIdString);
        holder.stepShortDescriptionTextView.setText(stepShortDescriptionString);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
