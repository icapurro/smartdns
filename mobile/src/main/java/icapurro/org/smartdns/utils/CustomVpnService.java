package icapurro.org.smartdns.utils;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.VpnService;
import android.os.Binder;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.util.Log;

//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;

import icapurro.org.smartdns.data.local.PreferencesHelper;
import icapurro.org.smartdns.data.model.APIResponse;
import icapurro.org.smartdns.data.model.Service;
import icapurro.org.smartdns.data.model.SmartDns;
import icapurro.org.smartdns.data.remote.SmartDnsService;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class CustomVpnService extends VpnService {

    private final IBinder mainBinder = new MyLocalBinder();
    private final String TAG = "CustomVpnService";
    private Thread vpnThread;
    private PendingIntent pendingIntent;
    private ParcelFileDescriptor vpnInterface;
    private PreferencesHelper preferencesHelper;
    Builder builder = new Builder();
    public static boolean isRunning;

    private String dns1 = null;
    private String dns2 = null;

    @Override
    public void onCreate() {
        preferencesHelper = new PreferencesHelper(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        SmartDns service = preferencesHelper.loadSmartDns();
        //replace in config with your own dns servers
        dns1 = service.getDns1().getIp();
        dns2 = service.getDns2().getIp();
        service.serviceUpdate()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SmartDnsService.Response>() {
                    @Override
                    public final void onCompleted() {
                        Log.i("SmartDnsService", "onCompleted");
                    }

                    @Override
                    public final void onError(Throwable e) {
                        Log.e("SmartDnsService", e.getMessage());
                    }

                    @Override
                    public final void onNext(SmartDnsService.Response response) {
                        Log.i("SmartDnsService", response.getMessage());
                    }
                });

        // Start a new session by creating a new thread.
        vpnThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //a. Configure the TUN and get the interface.
                    builder.setSession("DnsChangerLocalVpn")
                            .setMtu(1500)
                            .addAddress("10.0.2.15", 24)
                            .addAddress("10.0.2.16", 24)
                            .addAddress("10.0.2.17", 24)
                            .addAddress("10.0.2.18", 24)
                            .addDnsServer(dns1)
                            .addDnsServer(dns2);

                    vpnInterface = builder.setConfigureIntent(pendingIntent).establish();

                } catch (Exception e) {
                    Timber.e(TAG, e);
                }
            }

        }, "DnsChangerVpnRunnable");

        //start the service in a separate thread
        vpnThread.start();
        isRunning = true;

        Intent in = new Intent();
        in.setAction("vpn.start");
        sendBroadcast(in);

        return START_STICKY;
    }


    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "unbind");
        return super.onUnbind(intent);
    }

    public void kill() {
        try {
            if (vpnInterface != null) {
                vpnInterface.close();
                vpnInterface = null;
            }
            isRunning = false;
        } catch (Exception e) {
            Timber.e(TAG, e);
        }
        stopSelf();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "destroyed");
        if (vpnThread != null) {
            Log.i(TAG, "interrupted");
            vpnThread.interrupt();
        }
        isRunning = false;
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mainBinder;
    }

    public class MyLocalBinder extends Binder {
        public CustomVpnService getService(){
            return CustomVpnService.this;
        }
    }
}