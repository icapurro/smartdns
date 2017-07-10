package icapurro.org.smartdns.data.model;

import java.util.List;

public abstract class Service {

    protected String name;
    protected List<LocationIp> ips;
    protected LocationIp dns1;
    protected LocationIp dns2;

    protected Service(String name) {
        this.name = name;
    }

    public LocationIp getDns1() {
        return dns1;
    }

    public LocationIp getDns2() {
        return dns2;
    }

}
