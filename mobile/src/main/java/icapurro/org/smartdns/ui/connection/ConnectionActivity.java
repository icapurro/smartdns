package icapurro.org.smartdns.ui.connection;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;

import com.crashlytics.android.Crashlytics;
import com.kobakei.ratethisapp.RateThisApp;

import icapurro.org.smartdns.R;
import icapurro.org.smartdns.ui.base.BaseActivity;
import io.fabric.sdk.android.Fabric;


public class ConnectionActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_connection);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, new VpnFragment());
        ft.commit();

        // Monitor launch times and interval from installation
        RateThisApp.onCreate(this);
        // If the condition is satisfied, "Rate this app" dialog will be shown
        RateThisApp.showRateDialogIfNeeded(this);
    }

    public void setBackgroundColor(int color) {
        int colorResource;
        switch (color) {
            case R.color.green_primary:
                getTheme().applyStyle(R.style.AppTheme_Green, true);
                colorResource = ContextCompat.getColor(getApplicationContext(), R.color.green_primary);
                break;
            default:
                getTheme().applyStyle(R.style.AppTheme_Blue, true);
                colorResource = ContextCompat.getColor(getApplicationContext(), R.color.blue_primary);
                break;
        }
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(colorResource));
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(colorResource);
            getWindow().setStatusBarColor(colorResource);
        }
    }

}
