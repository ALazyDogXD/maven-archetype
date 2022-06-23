package __package__.common.redisson.mapper;

import __package__.common.redisson.anno.LuaMapperScan;
import __package__.common.redisson.exception.LuaMapperException;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;
import org.springframework.util.ClassUtils;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author ALazyDogXD
 * @date 2022/6/4 21:57
 * @description 获取所有的 lua 脚本; 将 RedissonClient 对象注入到 LuaMapperCache 对象中;
 */

public class LuaMapperRegistrar implements ImportBeanDefinitionRegistrar, BeanDefinitionRegistryPostProcessor, EnvironmentPostProcessor, BeanPostProcessor {

    private static final ResourcePatternResolver RESOURCE_RESOLVER = new PathMatchingResourcePatternResolver();

    private static final String LUA_BASE_PACKAGE_PROPERTY = "common.redisson.lua.base-packages";

    private LuaMapperCache cache;

    private List<String> mapperBasePackages;

    private List<String> luaBasePackages;

    @Override
    public void registerBeanDefinitions(@NonNull AnnotationMetadata importingClassMetadata, @NonNull BeanDefinitionRegistry registry) {
        AnnotationAttributes mapperScanAttrs = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(LuaMapperScan.class.getName()));
        mapperBasePackages = new ArrayList<>();
        mapperBasePackages.addAll(
                Arrays.stream(Objects.requireNonNull(mapperScanAttrs).getStringArray("value")).filter(StringUtils::hasText).collect(Collectors.toList()));
        if (mapperBasePackages.isEmpty()) {
            mapperBasePackages.add(getDefaultBasePackage(importingClassMetadata));
        }
    }

    private String getDefaultBasePackage(AnnotationMetadata importingClassMetadata) {
        return ClassUtils.getPackageName(importingClassMetadata.getClassName());
    }

    private Resource[] getResources(String location) {
        try {
            return RESOURCE_RESOLVER.getResources(location);
        } catch (IOException e) {
            return new Resource[0];
        }
    }

    @Override
    public void postProcessBeanDefinitionRegistry(@NonNull BeanDefinitionRegistry registry) throws BeansException {
        ClassPathLuaMapperScanner scanner = new ClassPathLuaMapperScanner(registry);
        Resource[] resources = luaBasePackages.stream()
                .flatMap(location -> Stream.of(getResources(location))).toArray(Resource[]::new);
        Map<String, String> scripts = new HashMap<>(16);
        for (Resource resource : resources) {
            if (scripts.containsKey(resource.getFilename())) {
                throw new LuaMapperException("脚本名称重复: " + resource.getFilename());
            }
            try {
                String script = StreamUtils.copyToString(resource.getInputStream(), Charset.defaultCharset());
                scripts.put(resource.getFilename(), script);
            } catch (IOException e) {
                throw new LuaMapperException("读取 lua[" + resource.getFilename() + "] 脚本失败", e);
            }
        }
        scanner.setScripts(scripts);
        scanner.setCache(cache = new LuaMapperCache());
        scanner.scan(mapperBasePackages.toArray(new String[0]));
    }

    @Override
    public void postProcessBeanFactory(@NonNull ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        luaBasePackages = new ArrayList<>();
        String[] packages = environment.getProperty(LUA_BASE_PACKAGE_PROPERTY, String[].class);
        if (packages != null) {
            luaBasePackages.addAll(
                    Arrays.stream(packages).filter(StringUtils::hasText).collect(Collectors.toList()));
        }
        if (luaBasePackages.isEmpty()) {
            luaBasePackages.add("classpath*:/lua/**/*.lua");
        }
    }

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if (bean instanceof RedissonClient) {
            cache.setRedissonClient(((RedissonClient) bean));
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
