//package com.test.config;
//
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.*;
//import org.springframework.beans.factory.config.BeanPostProcessor;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.context.*;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.ImportAware;
//import org.springframework.context.annotation.ImportSelector;
//import org.springframework.core.env.Environment;
//import org.springframework.core.type.AnnotationMetadata;
//
//import javax.annotation.PostConstruct;
//
///**
// * @author alazydogxd
// * @date 2022/5/26 11:49 AM
// * @description 测试
// */
//@Configuration
//public class TestConfig implements ImportSelector,
//        ApplicationContextAware,
//        ApplicationEventPublisherAware,
//        BeanClassLoaderAware,
//        BeanFactoryAware,
//        BeanNameAware,
//        EnvironmentAware,
//        ImportAware,
//        ApplicationRunner, BeanPostProcessor, InitializingBean {
//
//    {
//        System.out.println(0 + "config 初始化");
//    }
//
//    private int i;
//
//    @PostConstruct
//    public void init() {
//        System.out.println(++i + "init");
//    }
//
//    @Override
//    public String[] selectImports(AnnotationMetadata annotationMetadata) {
//        System.out.println(++i + "selector 调用");
//        return new String[0];
//    }
//
//    @Override
//    public void setBeanClassLoader(ClassLoader classLoader) {
//        System.out.println(++i + "beanClassLoaderAware: " + classLoader);
//    }
//
//    @Override
//    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
//        System.out.println(++i + "beanFactoryAware: " + beanFactory);
//    }
//
//    @Override
//    public void setBeanName(String s) {
//        System.out.println(++i + "beanNameAware: " + s);
//    }
//
//    @Override
//    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        System.out.println(++i + "applicationContextAware: " + applicationContext);
//    }
//
//    @Override
//    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
//        System.out.println(++i + "applicationEventPublisherAware: " + applicationEventPublisher);
//    }
//
//    @Override
//    public void setEnvironment(Environment environment) {
//        System.out.println(++i + "environment: " + environment);
//    }
//
//    @Override
//    public void setImportMetadata(AnnotationMetadata importMetadata) {
//        System.out.println(++i + "importAware: " + importMetadata);
//    }
//
//    private boolean before;
//
//    @Override
//    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
//        if (!before) {
//            before = true;
//            System.out.println(++i + "beanPostBefore: " + beanName);
//        }
//        return bean;
//    }
//
//    private boolean after;
//
//    @Override
//    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//        if (!after) {
//            after = true;
//            System.out.println(++i + "beanPostAfter: " + beanName);
//        }
//        return bean;
//    }
//
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        System.out.println(++i + "runner");
//    }
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        System.out.println(++i + "initBean: afterSet");
//    }
//}
