package com.github.peacetrue.classify;

import com.github.peacetrue.core.Operators;
import com.github.peacetrue.core.Range;
import com.github.peacetrue.result.ResultType;
import com.github.peacetrue.result.exception.DataResultException;
import com.github.peacetrue.spring.data.relational.core.query.CriteriaUtils;
import com.github.peacetrue.spring.data.relational.core.query.UpdateUtils;
import com.github.peacetrue.spring.util.BeanUtils;
import com.github.peacetrue.util.DateUtils;
import com.github.peacetrue.util.StreamUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.data.domain.*;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.relational.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

/**
 * 分类服务实现
 *
 * @author xiayx
 */
@Slf4j
@Service
public class ClassifyServiceImpl implements ClassifyService {

    @Autowired
    private R2dbcEntityOperations entityOperations;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public static Criteria buildCriteria(ClassifyQuery params) {
        return CriteriaUtils.and(
                CriteriaUtils.nullableCriteria(CriteriaUtils.smartIn("id"), params::getId),
                CriteriaUtils.nullableCriteria(Criteria.where("code")::like, value -> value + "%", params::getCode),
                CriteriaUtils.nullableCriteria(Criteria.where("name")::like, value -> "%" + value + "%", params::getName),
                CriteriaUtils.nullableCriteria(Criteria.where("typeId")::is, params::getTypeId),
                CriteriaUtils.nullableCriteria(Criteria.where("typeCode")::is, params::getTypeCode),
                CriteriaUtils.nullableCriteria(Criteria.where("parentId")::is, params::getParentId),
                CriteriaUtils.nullableCriteria(Criteria.where("level")::is, params::getLevel),
                CriteriaUtils.nullableCriteria(Criteria.where("parentId")::not, params::getLevel),
                CriteriaUtils.nullableCriteria(Criteria.where("creatorId")::is, params::getCreatorId),
                CriteriaUtils.nullableCriteria(Criteria.where("createdTime")::greaterThanOrEquals, params.getCreatedTime()::getLowerBound),
                CriteriaUtils.nullableCriteria(Criteria.where("createdTime")::lessThan, DateUtils.DATE_CELL_EXCLUDE, params.getCreatedTime()::getUpperBound),
                CriteriaUtils.nullableCriteria(Criteria.where("modifierId")::is, params::getModifierId),
                CriteriaUtils.nullableCriteria(Criteria.where("modifiedTime")::greaterThanOrEquals, params.getModifiedTime()::getLowerBound),
                CriteriaUtils.nullableCriteria(Criteria.where("modifiedTime")::lessThan, DateUtils.DATE_CELL_EXCLUDE, params.getModifiedTime()::getUpperBound)
        );
    }

    @Override
    //TODO 新增子节点时异常，疑似父节点尚未提交导致
    @Transactional
    public Mono<ClassifyVO> add(ClassifyAdd params) {
        log.info("新增分类信息[{}]", params);
        if (params.getRemark() == null) params.setRemark("");
        //查询父节点
        return this.get(Operators.setOperator(params, new ClassifyGet(params.getParentId())))
                .switchIfEmpty(Mono.error(new DataResultException(
                        ResultType.failure.name(), String.format("父节点'%s'不存在", params.getParentId()), params.getParentId()
                )))
                .zipWhen(parent -> {
                    //新增子节点。设置类型、层级
                    Classify entity = BeanUtils.map(params, Classify.class);
                    entity.setTypeId(parent.getTypeId());
                    entity.setTypeCode(parent.getTypeCode());
                    entity.setCode(parent.getCode() + "-" + entity.getCode());//类型下唯一节点
                    entity.setLevel(parent.getLevel() + 1);
                    entity.setLeaf(true);
                    entity.setSerialNumber(0);//设置临时序号，待后续更新
                    entity.setCreatorId(params.getOperatorId());
                    entity.setCreatedTime(LocalDateTime.now());
                    entity.setModifierId(entity.getCreatorId());
                    entity.setModifiedTime(entity.getCreatedTime());
                    return entityOperations.insert(entity);
                })
                .map(tuple2 -> {
                    ClassifyVO classifyVO = BeanUtils.map(tuple2.getT2(), ClassifyVO.class);
                    classifyVO.setParent(tuple2.getT1());
                    return classifyVO;
                })
                .flatMap(node -> {
                    //更新父节点是否叶子节点
                    if (node.getParent().getLeaf()) {
                        return this.setLeaf(node.getParentId(), false)
                                .thenReturn(node);
                    }
                    return Mono.just(node);
                })
                .flatMap(node -> {
                    //递归新增子节点
                    if (params.getChildren() == null) return Mono.just(node);
                    return Flux.fromIterable(params.getChildren())
                            .flatMap(child -> {
                                child.setParentId(node.getId());
                                return this.add(Operators.setOperator(params, child));
                            })
                            .doOnNext(child -> {
                                if (node.getChildren() == null) node.setChildren(new ArrayList<>(1));
                                node.getChildren().add(child);
                            })
                            .then(Mono.just(node));
                })
                .doOnNext(node -> eventPublisher.publishEvent(new PayloadApplicationEvent<>(node, params)));
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Page<ClassifyVO>> query(@Nullable ClassifyQuery params, @Nullable Pageable pageable, String... projection) {
        log.info("分页查询分类信息[{}]", params);
        if (params == null) params = ClassifyQuery.DEFAULT;
        if (params.getCreatedTime() == null) params.setCreatedTime(Range.LocalDateTime.DEFAULT);
        if (params.getModifiedTime() == null) params.setModifiedTime(Range.LocalDateTime.DEFAULT);
        Pageable finalPageable = pageable == null ? PageRequest.of(0, 10) : pageable;
        Criteria where = buildCriteria(params);
        return entityOperations.count(Query.query(where), Classify.class)
                .flatMap(total -> total == 0L ? Mono.empty() : Mono.just(total))
                .<Page<ClassifyVO>>flatMap(total -> {
                    Query query = Query.query(where).with(finalPageable).sort(finalPageable.getSortOr(Sort.by("createdTime").descending()));
                    return entityOperations.select(query, Classify.class)
                            .map(item -> BeanUtils.map(item, ClassifyVO.class))
                            .flatMap(fetchChildren(projection))
                            .collectList()
                            .map(item -> new PageImpl<>(item, finalPageable, total));
                })
                .switchIfEmpty(Mono.just(new PageImpl<>(Collections.emptyList(), finalPageable, 0L)));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<ClassifyVO> query(@Nullable ClassifyQuery params, @Nullable Sort sort, String... projection) {
        log.info("全量查询分类信息[{}]", params);
        if (params == null) params = ClassifyQuery.DEFAULT;
        if (params.getCreatedTime() == null) params.setCreatedTime(Range.LocalDateTime.DEFAULT);
        if (params.getModifiedTime() == null) params.setModifiedTime(Range.LocalDateTime.DEFAULT);
        if (sort == null) sort = Sort.by("serialNumber");
        Criteria where = buildCriteria(params);
        Query query = Query.query(where).sort(sort).limit(100);
        return entityOperations.select(query, Classify.class)
                .filter(node -> !node.getId().equals(node.getParentId()))
                .map(item -> BeanUtils.map(item, ClassifyVO.class))
                .flatMap(fetchChildren(projection));
    }

    private static boolean hasChildren(String... projection) {
        return ArrayUtils.contains(projection, "children");
    }

    private Function<ClassifyVO, Mono<ClassifyVO>> fetchChildren(String[] projection) {
        return item -> {
            if (hasChildren(projection) && Boolean.FALSE.equals(item.getLeaf())) {
                ClassifyQuery classifyQuery = new ClassifyQuery();
                classifyQuery.setParentId(item.getId());
                return this.query(classifyQuery, Sort.by("serialNumber"), projection)
                        .reduce(new ArrayList<>(), StreamUtils.reduceToCollection())
                        .map(children -> {
                            item.setChildren(children);
                            return item;
                        });
            }
            return Mono.just(item);
        };
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<ClassifyVO> get(ClassifyGet params, String... projection) {
        log.info("获取分类信息[{}]", params);
        Criteria where = CriteriaUtils.and(
                CriteriaUtils.nullableId(params::getId),
                CriteriaUtils.nullableCriteria(Criteria.where("code")::is, params::getCode),
                CriteriaUtils.nullableCriteria(Criteria.where("parentId")::is, params::getParentId),
                CriteriaUtils.nullableCriteria(Criteria.where("leaf")::is, params::getLeaf)
        );
        return entityOperations.selectOne(Query.query(where), Classify.class)
                .map(item -> BeanUtils.map(item, ClassifyVO.class))
                .flatMap(fetchChildren(projection));
    }

    @Override
    @Transactional
    public Mono<Integer> modify(ClassifyModify params) {
        log.info("修改分类信息[{}]", params);
        Criteria where = Criteria.where("id").is(params.getId());
        Query idQuery = Query.query(where);
        return entityOperations.selectOne(idQuery, Classify.class)
                .map(item -> BeanUtils.map(item, ClassifyVO.class))
                .zipWhen(entity -> {
                    Classify modify = BeanUtils.map(params, Classify.class);
                    modify.setModifierId(params.getOperatorId());
                    modify.setModifiedTime(LocalDateTime.now());
                    Update update = UpdateUtils.selectiveUpdateFromExample(modify);
                    return entityOperations.update(idQuery, update, Classify.class);
                })
                .doOnNext(tuple2 -> eventPublisher.publishEvent(new PayloadApplicationEvent<>(tuple2.getT1(), params)))
                .map(Tuple2::getT2)
                .switchIfEmpty(Mono.just(0));
    }

    @Override
    @Transactional
    public Mono<Integer> delete(ClassifyDelete params) {
        log.info("删除分类信息[{}]", params);
        Criteria where = Criteria.where("id").is(params.getId());
        Query idQuery = Query.query(where);
        return entityOperations.selectOne(idQuery, Classify.class)
                .map(item -> BeanUtils.map(item, ClassifyVO.class))
                .zipWhen(classify -> entityOperations.delete(idQuery, Classify.class))
                .doOnNext(tuple2 -> eventPublisher.publishEvent(new PayloadApplicationEvent<>(tuple2.getT1(), params)))
                .map(Tuple2::getT2)
                .switchIfEmpty(Mono.just(0));
    }

    public Mono<Integer> setSerialNumber(ClassifyVO vo) {
        Query parentId = Query.query(Criteria.where("parentId").is(vo.getParentId()));
        return entityOperations.count(parentId, Classify.class)
                .flatMap(count -> this.setSerialNumber(vo.getId(), count.intValue() + 1));
    }

    public Mono<Integer> setSerialNumber(Long id, Integer serialNumber) {
        log.info("设置节点[{}]序号为[{}]", id, serialNumber);
        return entityOperations.update(Classify.class)
                .matching(Query.query(Criteria.where("id").is(id)))
                .apply(Update.update("serialNumber", serialNumber));
    }

    public Mono<Integer> setLeaf(Long id, Boolean leaf) {
        log.info("设置节点[{}]是否叶子节点为[{}]", id, leaf);
        return entityOperations.update(Classify.class)
                .matching(Query.query(Criteria.where("id").is(id)))
                .apply(Update.update("leaf", leaf));
    }

    @Override
    @Transactional
    public Mono<Void> rearrange(ClassifyRearrange params) {
        log.info("重新排序[{}]", params);
        return this.get(new ClassifyGet(params.getId()), "children")
                .flatMap(node -> this.setSerialNumberRecursively(node, (int) SERIAL_NUMBER.getAndIncrement()).thenReturn(node))
                .doOnSuccess(node -> eventPublisher.publishEvent(new PayloadApplicationEvent<>(node, params)))
                .then();
    }

    public static final AtomicLong SERIAL_NUMBER = new AtomicLong(1);

    private Mono<Void> setSerialNumberRecursively(ClassifyVO node, Integer serialNumber) {
        return
//                (node.getSerialNumber().equals(serialNumber)
//                ? Mono.just(1)
//                : this.setSerialNumber(node.getId(), serialNumber))
                this.setSerialNumber(node.getId(), serialNumber)
                        .flatMap(ignored -> {
                            if (node.getChildren() == null) return Mono.empty();
                            return Flux.fromIterable(node.getChildren()).index()
                                    .flatMap(tuple2 -> this.setSerialNumberRecursively(tuple2.getT2(), (int) SERIAL_NUMBER.getAndIncrement()))
                                    .then();
                        });
    }
}
