package barqsoft.footballscores.widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

/**
 * Created by dave on 12/10/2015.
 */
public class ScoresWidgetService extends RemoteViewsService {
    private ScoresWidgetListProvider listProvider;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.d(this.getClass().getSimpleName(),"onGetViewFactory call");
        int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,AppWidgetManager.INVALID_APPWIDGET_ID);
        listProvider = new ScoresWidgetListProvider(this.getApplicationContext(),intent);
        return listProvider;
    }
}
