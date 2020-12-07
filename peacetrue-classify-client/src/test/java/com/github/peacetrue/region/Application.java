package com.github.peacetrue.region;

import com.fasterxml.jackson.databind.Module;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.support.PageJacksonModule;
import org.springframework.context.annotation.Bean;

/**
 * @author : xiayx
 * @since : 2020-05-23 21:55
 **/
@EnableFeignClients
@SpringBootApplication
public class Application {

    @Bean
    public Module pageJacksonModule() {
        return new PageJacksonModule();
    }

//    @Bean
//    public HttpMessageConverters httpMessageConverters(List<HttpMessageConverter<?>> httpMessageConverters) {
//        return new HttpMessageConverters(false, httpMessageConverters);
//    }

}
