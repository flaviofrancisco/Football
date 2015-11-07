package barqsoft.footballscores.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Flavio on 11/2/2015.
 */
public class FootballAuthenticatorService extends Service {

    private FootballAuthenticator mAuth;

    @Override
    public void onCreate(){
        mAuth = new FootballAuthenticator(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mAuth.getIBinder();
    }
}
