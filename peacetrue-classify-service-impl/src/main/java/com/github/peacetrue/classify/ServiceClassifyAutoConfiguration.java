package com.github.peacetrue.classify;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Objects;

/**
 * @author xiayx
 */
@Configuration
@EnableConfigurationProperties(ServiceClassifyProperties.class)
@ComponentScan(basePackageClasses = ServiceClassifyAutoConfiguration.class)
@PropertySource("classpath:/application-classify-service.yml")
public class ServiceClassifyAutoConfiguration {

    private ServiceClassifyProperties properties;

    public ServiceClassifyAutoConfiguration(ServiceClassifyProperties properties) {
        this.properties = Objects.requireNonNull(properties);
    }

}
