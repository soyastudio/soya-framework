package soya.framework.action.springboot;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "soya.framework.action")
public class ActionProperties {
    private String scanPackages = "soya.framework.action";

    public String getScanPackages() {
        return scanPackages;
    }

    public void setScanPackages(String scanPackages) {
        this.scanPackages = scanPackages;
    }
}
