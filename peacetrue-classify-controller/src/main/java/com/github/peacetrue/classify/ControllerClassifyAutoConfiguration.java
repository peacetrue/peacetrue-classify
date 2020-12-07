package com.github.peacetrue.classify;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


/**
 * @author xiayx
 */
@Configuration
@EnableConfigurationProperties(ControllerClassifyProperties.class)
@ComponentScan(basePackageClasses = ControllerClassifyAutoConfiguration.class)
@PropertySource("classpath:/application-classify-controller.yml")
public class ControllerClassifyAutoConfiguration {

}
