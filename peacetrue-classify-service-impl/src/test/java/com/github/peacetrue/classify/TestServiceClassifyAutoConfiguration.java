package com.github.peacetrue.classify;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.data.r2dbc.R2dbcDataAutoConfiguration;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ImportAutoConfiguration({
        R2dbcAutoConfiguration.class,
        R2dbcDataAutoConfiguration.class,
        ServiceClassifyAutoConfiguration.class,
})
@EnableAutoConfiguration
public class TestServiceClassifyAutoConfiguration {

}
