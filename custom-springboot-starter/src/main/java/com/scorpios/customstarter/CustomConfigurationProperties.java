package com.scorpios.customstarter;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("custom.node")
public class CustomConfigurationProperties {

    private String customHost = "127.0.0.1";

    private String customPort = "8080";


    public String getCustomHost() {
        return customHost;
    }

    public void setCustomHost(String customHost) {
        this.customHost = customHost;
    }

    public String getCustomPort() {
        return customPort;
    }

    public void setCustomPort(String customPort) {
        this.customPort = customPort;
    }
}
