package soya.framework.springboot.starter;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "soya.framework")
public class SoyaFrameworkProperties {
    
    private String scanPackages;
    private boolean enableProxy;
    private String proxyPackages;

    public String getScanPackages() {
        return scanPackages;
    }

    public void setScanPackages(String scanPackages) {
        this.scanPackages = scanPackages;
    }

    public boolean isEnableProxy() {
        return enableProxy;
    }

    public void setEnableProxy(boolean enableProxy) {
        this.enableProxy = enableProxy;
    }

    public String getProxyPackages() {
        return proxyPackages;
    }

    public void setProxyPackages(String proxyPackages) {
        this.proxyPackages = proxyPackages;
    }
}
