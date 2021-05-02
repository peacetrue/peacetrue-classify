package com.github.peacetrue.classify;

import com.github.peacetrue.spring.core.io.support.YamlPropertySourceFactory;
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
@PropertySource(value = "classpath:/application-classify-controller.yml", factory = YamlPropertySourceFactory.class)
public class ControllerClassifyAutoConfiguration {

}
