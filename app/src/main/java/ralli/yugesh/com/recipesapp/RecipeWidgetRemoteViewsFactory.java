package ralli.yugesh.com.recipesapp;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.List;

import ralli.yugesh.com.recipesapp.model.Ingredient;

public class RecipeWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private final String TAG = "RecipeWidgetRemoteViews";
    private Context mContext;
    private List<Ingredient> list;
    private Bundle bundle;
    private int appWidgetId;

    public RecipeWidgetRemoteViewsFactory(Context mContext, Intent intent) {

        appWidgetId=intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

        this.mContext = mContext;
         bundle = intent.getBundleExtra("bundle");
         list = (List<Ingredient>) bundle.getSerializable("ingredients");

    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        String ingredientString = list.get(position).getIngredient()
                +"("+list.get(position).getQuantity()+" "+list.get(position).getMeasure()+")";

        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(),R.layout.widget_ingredients_list_item);
        remoteViews.setTextViewText(R.id.widget_ingredient_name,ingredientString);

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
