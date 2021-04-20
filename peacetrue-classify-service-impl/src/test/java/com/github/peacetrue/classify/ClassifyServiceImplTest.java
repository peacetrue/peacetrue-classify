package com.github.peacetrue.classify;

import com.github.peacetrue.spring.util.BeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.Arrays;


/**
 * @author : xiayx
 * @since : 2020-05-22 16:43
 **/
@Slf4j
@SpringBootTest(classes = TestServiceClassifyAutoConfiguration.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ClassifyServiceImplTest {

    public static final EasyRandom EASY_RANDOM = new EasyRandom();
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
        ClassifyAdd classifyAdd = from(1L, "page", "页面区域",
                from("pc", "电脑端页面区域",
                        from("index", "首页",
                                from("banner", "banner")
                        ),
                        from("about", "关于我们"),
                        from("notice", "通知公告"),
                        from("childrenArt", "少儿美术"),
                        from("study", "出国留学"),
                        from("test", "统招艺考"),
                        from("ceramicArt", "陶瓷艺术"),
                        from("onlineApply", "在线报名"),
                        from("contact", "联系我们")
                ),
                from("mobile", "手机端页面区域",
                        from("index", "首页",
                                from("banner", "banner")
                        ),
                        from("about", "关于我们"),
                        from("notice", "通知公告"),
                        from("childrenArt", "少儿美术"),
                        from("study", "出国留学"),
                        from("test", "统招艺考"),
                        from("ceramicArt", "陶瓷艺术"),
                        from("onlineApply", "在线报名"),
                        from("contact", "联系我们")
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
