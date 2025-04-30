package ru.mai.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties("app.time-to-fix")
public class TimeToFixConfigurationProperties {

    private Map<String, Long> byType;

    private Map<String, Long> byComponent;

    public Map<String, Long> getByType() {
        return byType;
    }

    public void setByType(Map<String, Long> byType) {
        this.byType = byType;
    }

    public Map<String, Long> getByComponent() {
        return byComponent;
    }

    public void setByComponent(Map<String, Long> byComponent) {
        this.byComponent = byComponent;
    }

}
