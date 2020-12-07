package com.github.peacetrue.classify;

import com.github.peacetrue.spring.data.relational.core.query.QueryUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.relational.core.query.Update;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Schedulers;

/**
 * @author : xiayx
 * @since : 2020-12-06 08:10
 **/
@Slf4j
@Component
public class ClassifyListener {

    @Autowired
    private R2dbcEntityOperations entityOperations;

    @EventListener
    public void setParentLeafAfterClassifyAdd(PayloadApplicationEvent<ClassifyAdd> event) {
        ClassifyVO source = (ClassifyVO) event.getSource();
        log.info("新增分类节点[{}]后，设置父节点为非叶子节点", source.getId());
        //如果父节点已经不是叶子节点，不需要再设置
        entityOperations.update(Classify.class)
                .matching(QueryUtils.id(source::getParentId))
                .apply(Update.update("leaf", false))
                .publishOn(Schedulers.elastic())
                .subscribe();
    }
}
