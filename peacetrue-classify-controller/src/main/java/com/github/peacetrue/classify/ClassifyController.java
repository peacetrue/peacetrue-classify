package com.github.peacetrue.classify;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 分类控制器
 *
 * @author xiayx
 */
@Slf4j
@RestController
@RequestMapping(value = "/classifys")
public class ClassifyController {

    @Autowired
    private ClassifyService classifyService;

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Mono<ClassifyVO> addByForm(ClassifyAdd params) {
        log.info("新增分类信息(请求方法+表单参数)[{}]", params);
        return classifyService.add(params);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ClassifyVO> addByJson(@RequestBody ClassifyAdd params) {
        log.info("新增分类信息(请求方法+JSON参数)[{}]", params);
        return classifyService.add(params);
    }

    @GetMapping(params = "page")
    public Mono<Page<ClassifyVO>> query(ClassifyQuery params, Pageable pageable, String... projection) {
        log.info("分页查询分类信息(请求方法+参数变量)[{}]", params);
        return classifyService.query(params, pageable, projection);
    }

    @GetMapping
    public Flux<ClassifyVO> query(ClassifyQuery params, Sort sort, String... projection) {
        log.info("全量查询分类信息(请求方法+参数变量)[{}]", params);
        return classifyService.query(params, sort, projection);
    }

    @GetMapping("/{id}")
    public Mono<ClassifyVO> getByUrlPathVariable(@PathVariable Long id, String... projection) {
        log.info("获取分类信息(请求方法+路径变量)详情[{}]", id);
        return classifyService.get(new ClassifyGet(id), projection);
    }

    @RequestMapping("/get")
    public Mono<ClassifyVO> getByPath(ClassifyGet params, String... projection) {
        log.info("获取分类信息(请求路径+参数变量)详情[{}]", params);
        return classifyService.get(params, projection);
    }

    @PutMapping(value = {"", "/*"}, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Mono<Integer> modifyByForm(ClassifyModify params) {
        log.info("修改分类信息(请求方法+表单参数)[{}]", params);
        return classifyService.modify(params);
    }

    @PutMapping(value = {"", "/*"}, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Integer> modifyByJson(@RequestBody ClassifyModify params) {
        log.info("修改分类信息(请求方法+JSON参数)[{}]", params);
        return classifyService.modify(params);
    }

    @DeleteMapping("/{id}")
    public Mono<Integer> deleteByUrlPathVariable(@PathVariable Long id) {
        log.info("删除分类信息(请求方法+URL路径变量)[{}]", id);
        return classifyService.delete(new ClassifyDelete(id));
    }

    @DeleteMapping(params = "id")
    public Mono<Integer> deleteByUrlParamVariable(ClassifyDelete params) {
        log.info("删除分类信息(请求方法+URL参数变量)[{}]", params);
        return classifyService.delete(params);
    }

    @RequestMapping(path = "/delete")
    public Mono<Integer> deleteByPath(ClassifyDelete params) {
        log.info("删除分类信息(请求路径+URL参数变量)[{}]", params);
        return classifyService.delete(params);
    }

    @PutMapping("/rearrange")
    public Mono<Void> rearrange(ClassifyRearrange params) {
        log.info("重新排序[{}]", params);
        return classifyService.rearrange(params);
    }


}
