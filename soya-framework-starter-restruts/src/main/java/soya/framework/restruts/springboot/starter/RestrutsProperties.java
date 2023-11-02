package soya.framework.restruts.springboot.starter;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "soya.framework.restruts")
public class RestrutsProperties {
    public static final String DEFAULT_PATH = "/api/*";
    public static final String DEFAULT_SPEC = "OPENAPI300";

    private String path = DEFAULT_PATH;

    private String apiPath = "swagger.json";

    private boolean autoScan = true;

    private String scanPackages;

    private String specification = DEFAULT_SPEC;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isAutoScan() {
        return autoScan;
    }

    public void setAutoScan(boolean autoScan) {
        this.autoScan = autoScan;
    }

    public String getScanPackages() {
        return scanPackages;
    }

    public void setScanPackages(String scanPackages) {
        this.scanPackages = scanPackages;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getApiPath() {
        return apiPath;
    }

    public void setApiPath(String apiPath) {
        this.apiPath = apiPath;
    }
}
