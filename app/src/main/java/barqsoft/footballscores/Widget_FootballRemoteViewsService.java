package barqsoft.footballscores;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

/**
 * Created by Flavio on 10/25/2015.
 */
public class Widget_FootballRemoteViewsService extends RemoteViewsService {

    private static final String[] SCORE_COLUMNS = {
            DatabaseContract.SCORES_TABLE + "." + DatabaseContract.scores_table._ID,
            DatabaseContract.scores_table.LEAGUE_COL,
            DatabaseContract.scores_table.DATE_COL,
            DatabaseContract.scores_table.TIME_COL,
            DatabaseContract.scores_table.HOME_COL,
            DatabaseContract.scores_table.AWAY_COL,
            DatabaseContract.scores_table.HOME_GOALS_COL,
            DatabaseContract.scores_table.AWAY_GOALS_COL,
            DatabaseContract.scores_table.MATCH_ID,
            DatabaseContract.scores_table.MATCH_DAY
    };

    static final int INDEX_SCORE_ID = 0;
    static final int INDEX_SCORE_LEAGUE_COL = 1;
    static final int INDEX_SCORE_DATE_COL = 2;
    static final int INDEX_SCORE_TIME_COL = 3;
    static final int INDEX_SCORE_HOME_COL = 4;
    static final int INDEX_SCORE_AWAY_COL= 5;
    static final int INDEX_SCORE_HOME_GOALS_COL = 6;
    static final int INDEX_SCORE_AWAY_GOALS_COL = 7;
    static final int INDEX_SCORE_MATCH_ID = 8;
    static final int INDEX_SCORE_MATCH_DAY = 9;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {

            private Cursor data = null;

            @Override
            public void onCreate() {
                // Nothing to do
            }

            @Override
            public void onDataSetChanged() {
                if (data != null) {
                    data.close();
                }

                final long identityToken = Binder.clearCallingIdentity();
                Uri uri = DatabaseContract.scores_table.buildScoreAll();
                data = getContentResolver().query(uri, SCORE_COLUMNS, null, null, null);
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {

                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }

                RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.scores_list_item);

                String home_name = data.getString(INDEX_SCORE_HOME_COL);
                String away_name = data.getString(INDEX_SCORE_AWAY_COL);
                String matchtime = data.getString(INDEX_SCORE_TIME_COL);
                String scores = Utilies.getScores(data.getInt(INDEX_SCORE_HOME_GOALS_COL), data.getInt(INDEX_SCORE_AWAY_COL));

                views.setTextViewText(R.id.home_name, home_name);
                views.setTextViewText(R.id.away_name, away_name);
                views.setTextViewText(R.id.data_textview, matchtime);
                views.setTextViewText(R.id.score_textview, scores);
                views.setImageViewResource(R.id.home_crest, Utilies.getTeamCrestByTeamName(data.getString(INDEX_SCORE_HOME_COL)));
                views.setImageViewResource(R.id.away_crest, Utilies.getTeamCrestByTeamName(data.getString(INDEX_SCORE_AWAY_COL)));

                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.scores_list_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                if (data.moveToPosition(position))
                    return data.getLong(INDEX_SCORE_ID);
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }

        };
    }
}