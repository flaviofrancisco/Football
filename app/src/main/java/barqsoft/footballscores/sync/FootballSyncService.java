package barqsoft.footballscores.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Flavio on 11/3/2015.
 */
public class FootballSyncService extends Service {

    private static final Object sSyncAdapterLock = new Object();
    private static FootballSyncAdapter mFootballSyncAdapter = null;

    @Override
    public void onCreate() {

        synchronized (sSyncAdapterLock) {
            if (mFootballSyncAdapter == null) {
                mFootballSyncAdapter = new FootballSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mFootballSyncAdapter.getSyncAdapterBinder();
    }
}
