package barqsoft.footballscores.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilies;

/**
 * Created by dave on 12/10/2015.
 */
public class ScoresWidgetListProvider implements RemoteViewsService.RemoteViewsFactory, Loader.OnLoadCompleteListener<Cursor> {

    public static final int COL_HOME = 3;
    public static final int COL_AWAY = 4;
    public static final int COL_HOME_GOALS = 6;
    public static final int COL_AWAY_GOALS = 7;
    public static final int COL_LEAGUE = 5;
    public static final int COL_MATCHDAY = 9;
    public static final int COL_ID = 8;
    public static final int COL_MATCHTIME = 2;

    private ArrayList testList = new ArrayList();
    private ArrayList<ListItem> itemList = new ArrayList<ListItem>();
    private Context context;
    private int appWidgetId;
    private Date displayDate;
    private boolean scoresLoaded = false;

    public ScoresWidgetListProvider(Context con, Intent intent){
        Log.d(this.getClass().getSimpleName(),"Constructor call");
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        this.context = con;
        displayDate = new Date(System.currentTimeMillis());
        SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
        Log.d(this.getClass().getSimpleName(), mformat.format(displayDate));
        CursorLoader loader = new CursorLoader(context, DatabaseContract.scores_table.buildScoreWithDate(),
                null,null,new String[]{mformat.format(displayDate)},null);
        loader.registerListener(0, this);
        loader.startLoading();
        //add content to test list
        for(int i = 0; i <10 ;i++){
            testList.add("Test Content");
        }
    }

    private void populateListItem(String h, String a, String d, String s, int hc, int ac){
        Log.d(this.getClass().getSimpleName(),"populateListItem()");
        ListItem item = new ListItem(h,a,d,s,hc,ac);
        itemList.add(item);
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
        Log.d(this.getClass().getSimpleName(), "getCount call");
        while(!scoresLoaded){
            Log.d(this.getClass().getSimpleName(),"Waiting for cursor to load");
        }
        Log.d(this.getClass().getSimpleName(), "count: "+itemList.size());
        return itemList.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        Log.d(this.getClass().getSimpleName(),"getViewAt call i:"+i);
        final RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.widget_list_item);

        //remoteView.setTextViewText(R.id.widget_scores_text, testList.get(i).toString());
        remoteView.setTextViewText(R.id.widget_home_name,itemList.get(i).home);
        remoteView.setTextViewText(R.id.widget_away_name, itemList.get(i).away);
        remoteView.setTextViewText(R.id.widget_data_textview, itemList.get(i).date);
        remoteView.setTextViewText(R.id.widget_score_textview,itemList.get(i).score);
        remoteView.setImageViewResource(R.id.widget_home_crest, itemList.get(0).home_crest);
        remoteView.setImageViewResource(R.id.widget_away_crest, itemList.get(0).away_crest);

        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        Log.d(this.getClass().getSimpleName(),"getViewTypeCount call");
        return 1;
    }

    @Override
    public long getItemId(int i) {
        Log.d(this.getClass().getSimpleName(),"getItemId call");
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public void onLoadComplete(Loader<Cursor> loader, Cursor data) {
        Log.d(this.getClass().getSimpleName(),"load complete");
        Log.d(this.getClass().getSimpleName(), "Cursor size: " + data.getCount());
        loadCursorIntoList(data);
    }

    private void loadCursorIntoList(Cursor data){
        data.moveToFirst();
        while(!data.isAfterLast()){
            populateListItem(
                    data.getString(COL_HOME),
                    data.getString(COL_AWAY),
                    data.getString(COL_MATCHTIME),
                    Utilies.getScores(data.getInt(COL_HOME_GOALS), data.getInt(COL_AWAY_GOALS)),
                    Utilies.getTeamCrestByTeamName(data.getString(COL_HOME)),
                    Utilies.getTeamCrestByTeamName(data.getString(COL_AWAY))
            );
            data.moveToNext();
        }
        Log.d(this.getClass().getSimpleName(),"itemList: "+itemList.size());
        data.close();
        scoresLoaded = true;
    }

    private class ListItem{
        String home;
        String away;
        String date;
        String score;
        int home_crest;
        int away_crest;

        public ListItem(String h, String a, String d, String s, int hc, int ac){
            this.home = h;
            this.away = a;
            this.date = d;
            this.score = s;
            this.home_crest = hc;
            this.away_crest = ac;
        }

    }
}
