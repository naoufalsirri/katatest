package com.kata.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "delivery.api")
public class DeliveryApiProperties {

    private String mode = "classic"; // Valeur par défaut

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public boolean isStreamingEnabled() {
        return "streaming".equalsIgnoreCase(mode);
    }
}