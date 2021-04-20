package com.github.peacetrue.classify;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Nullable;

/**
 * 分类服务接口
 *
 * @author xiayx
 */
public interface ClassifyService {

    /** 新增 */
    Mono<ClassifyVO> add(ClassifyAdd params);

    /** 分页查询 */
    Mono<Page<ClassifyVO>> query(@Nullable ClassifyQuery params, @Nullable Pageable pageable, String... projection);

    /** 全量查询 */
    Flux<ClassifyVO> query(ClassifyQuery params, @Nullable Sort sort, String... projection);

    /** 全量查询 */
    default Flux<ClassifyVO> query(ClassifyQuery params, String... projection) {
        return this.query(params, (Sort) null, projection);
    }

    /** 获取 */
    Mono<ClassifyVO> get(ClassifyGet params, String... projection);

    /** 修改 */
    Mono<Integer> modify(ClassifyModify params);

    /** 删除 */
    Mono<Integer> delete(ClassifyDelete params);

    /** 重新排序 */
    Mono<Void> rearrange(ClassifyRearrange params);



}
