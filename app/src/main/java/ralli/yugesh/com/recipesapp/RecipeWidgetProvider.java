package ralli.yugesh.com.recipesapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.List;
import java.util.Random;

import ralli.yugesh.com.recipesapp.model.Ingredient;
import ralli.yugesh.com.recipesapp.ui.RecipeDetailActivity;
import ralli.yugesh.com.recipesapp.ui.RecipeListActivity;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    private static String ACTION_RECIPEWIDGET = "ACTION_RECIPEWIDGET";
    private String TAG = "RecipeWidgetProvider";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);
        RemoteViews widget=new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
        appWidgetManager.updateAppWidget(appWidgetId,widget);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context,intent);
        if (ACTION_RECIPEWIDGET.equals(intent.getAction())){
            Bundle bundle = intent.getBundleExtra("bundle");
            String recipeName = bundle.getString("recipeName");

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);
            views.setTextViewText(R.id.widget_recipe_name,recipeName);

            Intent listIntent =  new Intent(context, RecipeWidgetRemoteViewsService.class);
            listIntent.putExtra("bundle",bundle);

            Random random = new Random();
            listIntent.setType(String.valueOf(random.nextInt(1000)));

            RemoteViews widget=new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);
            widget.setRemoteAdapter(R.id.widget_ingredients_container, listIntent);

            // This time we dont have widgetId. Reaching our widget with that way.
            ComponentName appWidget = new ComponentName(context, RecipeWidgetProvider.class);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidget, views);
            appWidgetManager.updateAppWidget(appWidget,widget);
        }
    }
}

