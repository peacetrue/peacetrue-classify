package com.github.peacetrue.classify;

import com.github.peacetrue.spring.util.BeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.io.Serializable;
import java.util.Arrays;


/**
 * @author : xiayx
 * @since : 2020-05-22 16:43
 **/
@Slf4j
@SpringBootTest(classes = TestServiceClassifyAutoConfiguration.class)
@ActiveProfiles("classify-service-test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ClassifyServiceImplTest {

    public static final EasyRandom EASY_RANDOM = new EasyRandom(new EasyRandomParameters().randomize(Serializable.class, () -> 1L));
    public static final ClassifyAdd ADD = EASY_RANDOM.nextObject(ClassifyAdd.class);
    public static final ClassifyModify MODIFY = EASY_RANDOM.nextObject(ClassifyModify.class);
    public static ClassifyVO vo;

    static {
        ADD.setParentId(1L);
        ADD.setOperatorId(EASY_RANDOM.nextObject(Long.class));
        MODIFY.setOperatorId(EASY_RANDOM.nextObject(Long.class));
    }

    @Autowired
    private ClassifyService service;

    public static ClassifyAdd from(String code, String name, ClassifyAdd... children) {
        return from(null, code, name, children);
    }

    public static ClassifyAdd from(Long parentId, String code, String name, ClassifyAdd... children) {
        ClassifyAdd classifyAdd = new ClassifyAdd();
        classifyAdd.setParentId(parentId);
        classifyAdd.setCode(code);
        classifyAdd.setName(name);
        classifyAdd.setChildren(Arrays.asList(children));
        return classifyAdd;
    }

    @Test
    @Order(0)
    void addRecursively() throws Exception {
        ClassifyAdd classifyAdd = from(1L, "page", "????????????",
                from("pc", "?????????????????????",
                        from("index", "??????",
                                from("banner", "banner")
                        ),
                        from("about", "????????????"),
                        from("notice", "????????????"),
                        from("childrenArt", "????????????"),
                        from("study", "????????????"),
                        from("test", "????????????"),
                        from("ceramicArt", "????????????"),
                        from("onlineApply", "????????????"),
                        from("contact", "????????????")
                ),
                from("mobile", "?????????????????????",
                        from("index", "??????",
                                from("banner", "banner")
                        ),
                        from("about", "????????????"),
                        from("notice", "????????????"),
                        from("childrenArt", "????????????"),
                        from("study", "????????????"),
                        from("test", "????????????"),
                        from("ceramicArt", "????????????"),
                        from("onlineApply", "????????????"),
                        from("contact", "????????????")
                )
        );
        classifyAdd.setOperatorId(1L);
        service.add(classifyAdd)
                .flatMap(vo -> {
                    log.info("vo:\n" + vo);
                    return service.rearrange(new ClassifyRearrange(1L));
                })
//                .flatMap(vo -> service.get(new ClassifyGet(1L), "children"))
//                .doOnSuccess(vo -> {
//                    log.info("vo:\n" + vo);
//                })
                .publishOn(Schedulers.boundedElastic())
                .subscribe();

        Thread.sleep(5 * 1000L);
    }

    @Test
    @Order(10)
    void add() {
        service.add(ADD)
                .as(StepVerifier::create)
                .assertNext(data -> {
                    Assertions.assertEquals(data.getCreatorId(), ADD.getOperatorId());
                    vo = data;
                })
                .verifyComplete();
    }

    @Test
    @Order(20)
    void queryForPage() {
        ClassifyQuery params = BeanUtils.map(vo, ClassifyQuery.class);
        service.query(params, PageRequest.of(0, 10))
                .as(StepVerifier::create)
                .assertNext(page -> Assertions.assertEquals(1, page.getTotalElements()))
                .verifyComplete();
    }

    @Test
    @Order(30)
    void queryForList() {
        ClassifyQuery params = BeanUtils.map(vo, ClassifyQuery.class);
        service.query(params)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    @Order(40)
    void get() {
        ClassifyGet params = BeanUtils.map(vo, ClassifyGet.class);
        service.get(params)
                .as(StepVerifier::create)
                .assertNext(item -> Assertions.assertEquals(vo.getId(), item.getId()))
                .verifyComplete();
    }

    @Test
    @Order(50)
    void modify() {
        ClassifyModify params = MODIFY;
        params.setId(vo.getId());
        service.modify(params)
                .as(StepVerifier::create)
                .expectNext(1)
                .verifyComplete();
    }

    @Test
    @Order(60)
    void delete() {
        ClassifyDelete params = new ClassifyDelete(vo.getId());
        service.delete(params)
                .as(StepVerifier::create)
                .expectNext(1)
                .verifyComplete();
    }
}
