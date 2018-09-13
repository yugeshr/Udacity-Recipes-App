package ralli.yugesh.com.recipesapp.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class RecipeWidgetRemoteViewsService extends RemoteViewsService{

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return (new RecipeWidgetRemoteViewsFactory(this.getApplicationContext(),intent));
    }
}
