package com.github.peacetrue.region;

import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Nullable;

/**
 * 地区客户端
 *
 * @author xiayx
 */
@ReactiveFeignClient(name = "peacetrue-region", url = "${peacetrue.Classify.url:${peacetrue.server.url:}}")
public interface ClassifyServiceClient {

    @PostMapping(value = "/regions")
    Mono<ClassifyVO> add(ClassifyAdd params);

    @GetMapping(value = "/regions", params = "page")
    Mono<Page<ClassifyVO>> query(@Nullable @SpringQueryMap ClassifyQuery params, @Nullable Pageable pageable, @SpringQueryMap String... projection);

    @GetMapping(value = "/regions", params = "sort")
    Flux<ClassifyVO> query(@SpringQueryMap ClassifyQuery params, Sort sort, @SpringQueryMap String... projection);

    @GetMapping(value = "/regions")
    Flux<ClassifyVO> query(@SpringQueryMap ClassifyQuery params, @SpringQueryMap String... projection);

    @GetMapping(value = "/regions/get")
    Mono<ClassifyVO> get(@SpringQueryMap ClassifyGet params, @SpringQueryMap String... projection);

    @PutMapping(value = "/regions")
    Mono<Integer> modify(ClassifyModify params);

    @DeleteMapping(value = "/regions/delete")
    Mono<Integer> delete(@SpringQueryMap ClassifyDelete params);

}
