package icapurro.org.smartdns.data.model;

public class LocationIp {

    protected String name;
    protected String ip;
    protected Long pingTime;

    public LocationIp(String name, String ip) {
        this.name = name;
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public String getIp() {
        return ip;
    }

    public Long getPingTime() {
        return pingTime;
    }

    public void setPingTime(Long pingTime) {
        this.pingTime = pingTime;
    }

    public String toString() {
        return name + " - " + ip;
    }
}
