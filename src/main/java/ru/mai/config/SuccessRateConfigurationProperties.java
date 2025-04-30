package ru.mai.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties("app.success-rate")
public class SuccessRateConfigurationProperties {

    private Map<String, Integer> byType;

    private Map<String, Integer> byComponent;

    public Map<String, Integer> getByType() {
        return byType;
    }

    public void setByType(Map<String, Integer> byType) {
        this.byType = byType;
    }

    public Map<String, Integer> getByComponent() {
        return byComponent;
    }

    public void setByComponent(Map<String, Integer> byComponent) {
        this.byComponent = byComponent;
    }

}
