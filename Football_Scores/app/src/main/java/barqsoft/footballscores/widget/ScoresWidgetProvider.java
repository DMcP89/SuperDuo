package barqsoft.footballscores.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import barqsoft.footballscores.R;
import barqsoft.footballscores.service.myFetchService;

/**
 * Created by dave on 10/23/2015.
 */
public class ScoresWidgetProvider extends AppWidgetProvider {

    private RemoteViews updateWidgetListView(Context con, int widgetId){
        Log.d(this.getClass().getSimpleName(), "updateWidgetListView called");
        RemoteViews remoteViews = new RemoteViews(con.getPackageName(), R.layout.widget_layout);
        Intent svcIntent = new Intent(con,ScoresWidgetService.class);
        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
        remoteViews.setRemoteAdapter(R.id.widget_scores_list,svcIntent);
        remoteViews.setEmptyView(R.id.widget_scores_list,R.id.emptyview);
        return remoteViews;
    }

    private void updateScoresDB(Context con){
        Log.d(this.getClass().getSimpleName(), "updateScoresDB");
        Intent service_start = new Intent(con, myFetchService.class);
        con.startService(service_start);
    }


    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(this.getClass().getSimpleName(),"Widget Updated");
        updateScoresDB(context);
        ComponentName thisWidget = new ComponentName(context,ScoresWidgetProvider.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        for (int widgetId : allWidgetIds){
            Log.d(this.getClass().getSimpleName(),"WidgetId: "+widgetId);
            RemoteViews remoteViews = updateWidgetListView(context,widgetId);
            appWidgetManager.updateAppWidget(widgetId,remoteViews);
        }
        super.onUpdate(context,appWidgetManager,appWidgetIds);
    }


    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }
}
