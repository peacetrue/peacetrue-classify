package com.github.peacetrue.region;

import com.github.peacetrue.spring.util.BeanUtils;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

/**
 * @author : xiayx
 * @since : 2020-05-23 18:26
 **/
@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class ClassifyServiceClientTest {

    private static ClassifyVO vo;
    private final EasyRandom easyRandom = new EasyRandom();
    @Autowired
    private ClassifyServiceClient regionServiceClient;

    @Test
    void test010add() {
        ClassifyAdd params = easyRandom.nextObject(ClassifyAdd.class);
        params.setOperatorId(easyRandom.nextObject(String.class));
        vo = regionServiceClient.add(params);
        Assertions.assertNotNull(vo);
    }

    @Test
    void test020query() {
        ClassifyQuery params = BeanUtils.map(vo, ClassifyQuery.class);
        Page<ClassifyVO> page = regionServiceClient.query(params, PageRequest.of(0, 10));
        Assertions.assertEquals(1, page.getTotalElements());
    }

    @Test
    void test030query() {
        ClassifyQuery params = BeanUtils.map(vo, ClassifyQuery.class);
        List<ClassifyVO> vos = regionServiceClient.query(params, Sort.by("code"));
        Assertions.assertEquals(1, vos.size());
    }

    @Test
    void test040get() {
        ClassifyGet params = BeanUtils.map(vo, ClassifyGet.class);
        ClassifyVO item = regionServiceClient.get(params);
        Assertions.assertEquals(vo.getCode(), item.getCode());
    }

    @Test
    void test050modify() {
        ClassifyModify params = easyRandom.nextObject(ClassifyModify.class);
        params.setId(vo.getId());
        params.setOperatorId(easyRandom.nextObject(String.class));
        Integer count = regionServiceClient.modify(params);
        Assertions.assertEquals(1, count);
    }

    @Test
    void test060delete() {
        ClassifyDelete params = easyRandom.nextObject(ClassifyDelete.class);
        params.setId(vo.getId());
        Integer count = regionServiceClient.delete(params);
        Assertions.assertEquals(1, count);
    }
}
