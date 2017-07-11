package icapurro.org.smartdns.data.model;


import java.util.ArrayList;
import java.util.List;

import icapurro.org.smartdns.data.remote.SmartDnsService;
import rx.Observable;

public class SmartDns extends Service {

    private String serviceId;
    private LocationIp dns1, dns2;
    private Boolean updateIp;

    public SmartDns(String serviceId) {
        super("SmartDns");
        ips = createLocationIps();
        this.dns1 = ips.get(21);
        this.dns2 = ips.get(23);
        this.serviceId = serviceId;
        this.updateIp = true;
    }

    private List<LocationIp> createLocationIps() {
        return new ArrayList<LocationIp>() {{
            add(new LocationIp("Australia, Melbourne", "168.1.79.238"));
            add(new LocationIp("Australia, Sydney", "54.66.128.66"));
            add(new LocationIp("Brazil, Sao Paulo", "54.94.226.225"));
            add(new LocationIp("Canada, Montreal", "169.54.78.85"));
            add(new LocationIp("Canada, Toronto", "169.53.182.120"));
            add(new LocationIp("Germany, Frankfurt", "54.93.173.153"));
            add(new LocationIp("India, Pune", "169.38.73.5"));
            add(new LocationIp("Ireland, Dublin", "54.229.171.243"));
            add(new LocationIp("Israel", "195.28.181.161"));
            add(new LocationIp("Italy, Milan", "95.141.39.236"));
            add(new LocationIp("Japan, Tokyo", "54.64.107.105"));
            add(new LocationIp("Mexico", "169.57.10.21"));
            add(new LocationIp("Netherlands, Amsterdam", "46.166.189.68"));
            add(new LocationIp("New Zealand", "223.165.64.97"));
            add(new LocationIp("Singapore", "54.255.130.140"));
            add(new LocationIp("South Africa 1", "154.127.57.224"));
            add(new LocationIp("South Africa 2", "129.232.164.26"));
            add(new LocationIp("Spain, Valencia", "192.162.27.100"));
            add(new LocationIp("Sweden, Stockholm", "46.246.29.69"));
            add(new LocationIp("Switzerland", "81.17.17.170"));
            add(new LocationIp("Turkey, Istanbul", "188.132.234.170"));
            add(new LocationIp("US East - N. Virginia", "23.21.43.50"));
            add(new LocationIp("US Center - Dallas", "169.53.235.135"));
            add(new LocationIp("US West - Los Angeles", "54.183.15.10"));
        }};
    }

    private LocationIp searchDns(String name) {
        for (LocationIp locationIp : ips) {
            if (locationIp.toString().equals(name)) {
                return locationIp;
            }
        }
        return null;
    }

    public Boolean shouldUpdateIp() {
        return updateIp;
    }

    public void setUpdateIp(Boolean updateIp) {
        this.updateIp = updateIp;
    }

    public void setDns1(int i) {
        this.dns1 = ips.get(i);
    }

    public void setDns1(String dns1) {
        LocationIp res = searchDns(dns1);
        if (res != null) {
            this.dns1 = res;
        }
    }

    public void setDns2(int i) {
        this.dns2 = ips.get(i);
    }

    public void setDns2(String dns2) {
        LocationIp res = searchDns(dns2);
        if (res != null) {
            this.dns2 = res;
        }
    }

    public LocationIp getDns1() {
        return dns1;
    }

    public LocationIp getDns2() {
        return dns2;
    }

    public String getServiceId() {
        return serviceId;
    }

    public Boolean hasServiceId() {
        return serviceId != null && !serviceId.equals("");
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public List<String> getLocationStrings() {
        List<String> results = new ArrayList<String>();
        for (LocationIp locationIp : ips) {
            results.add(locationIp.toString());
        }
        return results;
    }

    public Observable<SmartDnsService.Response> serviceUpdate() {
        return SmartDnsService.Creator.newSmartDnsService().getIpUpdate(this.serviceId);
    }
}
