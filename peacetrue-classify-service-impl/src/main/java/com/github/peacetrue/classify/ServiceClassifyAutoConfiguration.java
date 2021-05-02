package com.github.peacetrue.classify;

import com.github.peacetrue.spring.core.io.support.YamlPropertySourceFactory;
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
@PropertySource(value = "classpath:/application-classify-service.yml", factory = YamlPropertySourceFactory.class)
public class ServiceClassifyAutoConfiguration {

    private ServiceClassifyProperties properties;

    public ServiceClassifyAutoConfiguration(ServiceClassifyProperties properties) {
        this.properties = Objects.requireNonNull(properties);
    }

}
