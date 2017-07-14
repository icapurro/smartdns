package icapurro.org.smartdns.ui.connection;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.util.Linkify;
import android.widget.CompoundButton;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.crashlytics.android.Crashlytics;
import com.kobakei.ratethisapp.RateThisApp;

import icapurro.org.smartdns.R;
import icapurro.org.smartdns.data.local.PreferencesHelper;
import icapurro.org.smartdns.ui.base.BaseActivity;
import io.fabric.sdk.android.Fabric;


public class ConnectionActivity extends BaseActivity {

    PreferencesHelper preferencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_connection);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, new VpnFragment());
        ft.commit();

        preferencesHelper = new PreferencesHelper(getApplicationContext());

        if (!preferencesHelper.isDontAskAgain()) {
            showInstructions();
        }

        // Monitor launch times and interval from installation
        RateThisApp.onCreate(this);
        // If the condition is satisfied, "Rate this app" dialog will be shown
        RateThisApp.showRateDialogIfNeeded(this);
    }

    private void showInstructions() {
        final SpannableString body = new SpannableString(getResources()
                .getString(R.string.instructions_body));
        Linkify.addLinks(body, Linkify.ALL);
        new MaterialDialog.Builder(this)
                .title(R.string.instructions_title)
                .content(body)
                .positiveText(R.string.ok)
                .checkBoxPromptRes(R.string.dont_ask_again, false, new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        preferencesHelper.setDontAskAgain(true);
                    }
                })
                .show();
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
