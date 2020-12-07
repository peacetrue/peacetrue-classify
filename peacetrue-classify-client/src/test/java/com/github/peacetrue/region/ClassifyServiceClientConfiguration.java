package com.github.peacetrue.region;

import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

/**
 * @author : xiayx
 * @since : 2020-05-23 19:27
 **/
//@Configuration
public class ClassifyServiceClientConfiguration {

    @Value("${spring.security.user.name}")
    private String name;
    @Value("${spring.security.user.password}")
    private String password;

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor(name, password);
    }


}
