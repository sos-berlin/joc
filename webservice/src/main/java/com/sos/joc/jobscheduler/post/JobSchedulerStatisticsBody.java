package com.sos.joc.jobscheduler.post;

public class JobSchedulerStatisticsBody {
    String protocol;
    String host;
    Long port;
    String accessToken;
    
    public String getHost() {
        return host;
    }
    public void setHost(String host) {
        this.host = host;
    }
    public Long getPort() {
        return port;
    }
    public void setPort(Long port) {
        this.port = port;
    }
    public String getAccessToken() {
        return accessToken;
    }
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
    public String getProtocol() {
        return protocol;
    }
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
 
    
    

}
