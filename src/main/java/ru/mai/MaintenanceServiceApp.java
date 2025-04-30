package ru.mai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.mai.config.ExecutorConfiguration;
import ru.mai.config.SuccessRateConfigurationProperties;
import ru.mai.config.TimeToFixConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(
        {TimeToFixConfigurationProperties.class, SuccessRateConfigurationProperties.class, ExecutorConfiguration.class}
)
public class MaintenanceServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(MaintenanceServiceApp.class);
    }

}