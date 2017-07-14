package icapurro.org.smartdns.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

import icapurro.org.smartdns.data.model.SmartDns;
import icapurro.org.smartdns.injection.ApplicationContext;

@Singleton
public class PreferencesHelper {

    private static final String PREF_FILE_NAME = "dnschanger_pref_file";
    private static final String SMART_DNS_SERVICE_ID = "smart_dns_service_id";
    private static final String SMART_DNS_DNS_1 = "smart_dns_dns_1";
    private static final String SMART_DNS_DNS_2 = "smart_dns_dns_2";
    private static final String SMART_DNS_UPDATE_IP = "smart_dns_update_ip";
    private static final String START_DATE = "start_date";
    private static final String DONT_ASK_AGAIN = "dont_ask_again";

    private final SharedPreferences mPref;

    @Inject
    public PreferencesHelper(@ApplicationContext Context context) {
        mPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    public SmartDns saveSmartDns(SmartDns smartDns) {
        mPref.edit()
                .putString(SMART_DNS_SERVICE_ID, smartDns.getServiceId())
                .putString(SMART_DNS_DNS_1, smartDns.getDns1().toString())
                .putString(SMART_DNS_DNS_2, smartDns.getDns2().toString())
                .putBoolean(SMART_DNS_UPDATE_IP, smartDns.shouldUpdateIp())
                .apply();
        return smartDns;
    }

    public SmartDns loadSmartDns() {
        String serviceId = mPref.getString(SMART_DNS_SERVICE_ID, null);
        String dns1 = mPref.getString(SMART_DNS_DNS_1, null);
        String dns2 = mPref.getString(SMART_DNS_DNS_2, null);
        Boolean updateIp = mPref.getBoolean(SMART_DNS_UPDATE_IP, false);
        SmartDns smartDns = new SmartDns(serviceId);
        smartDns.setDns1(dns1);
        smartDns.setDns2(dns2);
        smartDns.setUpdateIp(updateIp);
        return smartDns;
    }

    public void saveStartDate(int time) {
        mPref.edit().putInt(START_DATE, time).apply();
    }

    public int getStartDate() {
        return mPref.getInt(START_DATE, 0);
    }

    public void setDontAskAgain(Boolean dontAskAgain) {
        mPref.edit().putBoolean(DONT_ASK_AGAIN, dontAskAgain).apply();
    }

    public Boolean isDontAskAgain() {
        return mPref.getBoolean(DONT_ASK_AGAIN, false);
    }

    public void clear() {
        mPref.edit().clear().apply();
    }

}
