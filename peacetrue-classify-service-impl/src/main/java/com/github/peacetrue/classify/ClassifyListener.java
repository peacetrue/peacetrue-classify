package com.github.peacetrue.classify;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.relational.core.query.Update;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * @author : xiayx
 * @since : 2020-12-06 08:10
 **/
@Slf4j
@Component
public class ClassifyListener {

    @Autowired
    private ClassifyService classifyService;

    //TODO org.springframework.beans.factory.BeanNotOfRequiredTypeException: Bean named 'classifyServiceImpl' is expected to be of type 'com.github.peacetrue.classify.ClassifyServiceImpl' but was actually of type 'com.sun.proxy.$Proxy111'
//    @Autowired
//    private ClassifyServiceImpl classifyServiceImpl;
    @Autowired
    private R2dbcEntityOperations entityOperations;

    public Mono<Integer> setSerialNumber(ClassifyVO vo) {
//        Query parentId = Query.query(Criteria.where("parentId").is(vo.getParentId()));
        return entityOperations.getDatabaseClient()
                .execute("select ifnull(max(serial_number),0) from classify where parent_id=?")
                .bind(0, vo.getParentId())
                .as(Long.class)
                .fetch()
                .first()
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


//    @Order(1)
//    @EventListener
//    public void setParentLeafAfterClassifyAdd(PayloadApplicationEvent<ClassifyAdd> event) {
//        ClassifyVO source = (ClassifyVO) event.getSource();
//        log.info("新增分类节点[{}]后，设置父节点为非叶子节点", source.getId());
//        //如果父节点已经不是叶子节点，不需要再设置
//        if (source.getParent().getLeaf()) {
//            setLeaf(source.getParentId(), false)
//                    .publishOn(Schedulers.elastic())
//                    .subscribe();
//        }
//    }

    @Order(2)
    @EventListener
    public void setSerialNumberAfterClassifyAdd(PayloadApplicationEvent<ClassifyAdd> event) {
        ClassifyVO source = (ClassifyVO) event.getSource();
        log.info("新增分类节点[{}]后，设置节点序号", source.getId());
        setSerialNumber(source)
                .publishOn(Schedulers.boundedElastic())
                .subscribe();
    }

}
