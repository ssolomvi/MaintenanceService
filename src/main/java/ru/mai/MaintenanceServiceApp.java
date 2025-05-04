package ru.mai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.mai.config.property.ExecutorConfiguration;
import ru.mai.config.property.SuccessRateConfigurationProperties;
import ru.mai.config.property.TimeToCreateConfigurationProperties;
import ru.mai.config.property.TimeToFixConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(
        {TimeToFixConfigurationProperties.class,
                SuccessRateConfigurationProperties.class,
                TimeToCreateConfigurationProperties.class,
                ExecutorConfiguration.class}
)
public class MaintenanceServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(MaintenanceServiceApp.class);
    }

}