package com.transporeon.routing.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Configuration
@ConfigurationProperties(prefix = "routing")
@Data
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class RoutingConfig {
    @NotNull
    private Integer maxStops;
    @NotNull
    private Double groundTransferThreshold;
}
