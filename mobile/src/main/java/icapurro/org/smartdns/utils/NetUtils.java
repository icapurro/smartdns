package icapurro.org.smartdns.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtils {

    /**
     * Get the network info
     * @param context
     * @return
     */
    public static NetworkInfo getNetworkInfo(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    /**
     * Check if there is any connectivity to a Wifi or Ethernet network
     * @param context
     * @return
     */
    public static boolean isConnectedWifiOrWired(Context context){
        NetworkInfo info = NetUtils.getNetworkInfo(context);
        return (info != null && info.isConnected() &&
                (info.getType() == ConnectivityManager.TYPE_WIFI ||
                 info.getType() == ConnectivityManager.TYPE_ETHERNET));
    }

}
