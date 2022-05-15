package com.test.test;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author ALazyDogXD
 * @date 2022/4/17 15:56
 * @description 测试
 */

public class TestImportSelector implements ImportSelector, EnvironmentAware, BeanFactoryAware, BeanClassLoaderAware, ResourceLoaderAware {

    public TestImportSelector(Environment environment,
                              BeanFactory beanFactory,
                              ClassLoader classLoader,
                              ResourceLoader resourceLoader) {
        System.out.println("创建 ImportSelector" + environment + beanFactory + classLoader + resourceLoader);
    }

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        System.out.println("执行 selectImports" + importingClassMetadata);
        return new String[0];
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        System.out.println("执行 setBeanClassLoader" + classLoader);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("执行 setBeanFactory" + beanFactory);
    }

    @Override
    public void setEnvironment(Environment environment) {
        System.out.println("执行 setEnvironment" + environment);
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        System.out.println("执行 setResourceLoader" + resourceLoader);
    }
}
