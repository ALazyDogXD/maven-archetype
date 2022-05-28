package com.test.entity;

import __package__.common.mybatisplus.reference.ForeignKey;
import __package__.common.mybatisplus.reference.Referable;

import java.util.List;

/**
 * @author ALazyDogXD
 * @date 2022/4/11 13:44
 * @description a
 */

public class A implements Referable<A> {

    private Integer id;

    private String test;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    @Override
    public List<ForeignKey<?, A, ?>> refer() {
        return createBuilderOfReference().setEntityClass(A.class)
                .add(A::getId, B.class, B::getId)
                .build();
    }
}
