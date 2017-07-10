package icapurro.org.smartdns;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

public class SmartDnsApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            Fabric.with(this, new Crashlytics());
        }
    }

    public static SmartDnsApplication get(Context context) {
        return (SmartDnsApplication) context.getApplicationContext();
    }
}
