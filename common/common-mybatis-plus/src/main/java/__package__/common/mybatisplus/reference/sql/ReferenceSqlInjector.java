package __package__.common.mybatisplus.reference.sql;

import __package__.common.base.util.ReflectUtil;
import __package__.common.mybatisplus.exception.ReferenceException;
import __package__.common.mybatisplus.mapper.ReferenceMapper;
import __package__.common.mybatisplus.reference.ForeignKey;
import __package__.common.mybatisplus.reference.Referable;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.lang.NonNull;
import org.springframework.lang.NonNullApi;
import org.springframework.lang.NonNullFields;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static __package__.common.mybatisplus.reference.sql.ReferenceContext.MASTER_FOREIGN_KEYS;
import static __package__.common.mybatisplus.reference.sql.ReferenceContext.SLAVE_FOREIGN_KEYS;

/**
 * @author alazydogxd
 * @date 2022/5/24 12:35 PM
 * @description 外键检查 SQL 注入器
 */
public class ReferenceSqlInjector extends DefaultSqlInjector implements ResourceLoaderAware {
    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass);
        methodList.add(new SaveWithCheckReference());
        return methodList;
    }

    public void setResourceLoader(@NonNull ResourceLoader resourceLoader) {
//        ClassPathScanningCandidateComponentProvider classPathScanningCandidateComponentProvider = new ClassPathScanningCandidateComponentProvider(false);
//        classPathScanningCandidateComponentProvider.setResourceLoader(resourceLoader);
//        classPathScanningCandidateComponentProvider.addIncludeFilter(new AssignableTypeFilter(ReferenceMapper.class));
//        Set<BeanDefinition> candidateComponents = classPathScanningCandidateComponentProvider.findCandidateComponents("classpath:/");
//        System.out.println(candidateComponents);
        Set<Class<?>> mapperClasses = new HashSet<>();
        PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
        ResourcePatternResolver resolver = ResourcePatternUtils.getResourcePatternResolver(pathMatchingResourcePatternResolver);
        CachingMetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(pathMatchingResourcePatternResolver);
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
//                .concat(ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders("")))
                .concat("/**/*.class");
        try {
            for (Resource resource : resolver.getResources(packageSearchPath)) {
                if (resource.isReadable()) {
                    try {
                        MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                        String className = metadataReader.getClassMetadata().getClassName();
                        if (!className.contains("$") && !className.contains("-")) {
                            Class<?> aClass = Class.forName(className);
                            if (ReferenceMapper.class.isAssignableFrom(aClass) && aClass != ReferenceMapper.class)
                                mapperClasses.add(aClass);
                        }
                    } catch (UnsatisfiedLinkError | ExceptionInInitializerError | IOException | ClassNotFoundException | NoClassDefFoundError e) {
//                        throw new RuntimeException(e);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (!mapperClasses.isEmpty()) {
            for (Class<?> mapper : mapperClasses) {
                // 找到继承 IService 的接口
//                Class<?> mapperClass = ReflectUtil.getSonInterface(mapper, ReferenceMapper.class);
//                for (Class<?> i : iService.getClass().getSuperclass().getInterfaces()) {
////                    if (IService.class.isAssignableFrom(i)) {
////                        iServiceClass = i;
////                    }
//
//                    if (i.isAssignableFrom(IService.class)) {
//                        iServiceClass = i;
//                    }
//                }
//                if (iServiceClass == null) {
//                    continue;
//                }

                // 收集外键
                Class<?> entityClass = (Class<?>) ReflectUtil.getInterfaceGenericsType(mapper, ReferenceMapper.class);
                if (entityClass == null) {
                    continue;
                }
                Constructor<?> constructor;
                try {
                    if (Referable.class.isAssignableFrom(entityClass)) {
                        constructor = entityClass.getConstructor();
                        constructor.setAccessible(true);
                        Referable<?> r = (Referable<?>) constructor.newInstance();
                        List<? extends ForeignKey<?, ? extends Referable<?>, ?>> foreignKeys = r.refer();
                        // 收集外键, key 为从表类型
                        SLAVE_FOREIGN_KEYS.put(entityClass, foreignKeys);
                        // 收集外键, key 为主表类型
                        for (ForeignKey<?, ? extends Referable<?>, ?> foreignKey : foreignKeys) {
                            foreignKey.initMainTable();
                            Class<?> mainClass = foreignKey.getMainClass();
                            List<ForeignKey<?, ? extends Referable<?>, ?>> masterForeignKeys;
                            if ((masterForeignKeys = MASTER_FOREIGN_KEYS.get(mainClass)) == null) {
                                masterForeignKeys = new ArrayList<>();
                                MASTER_FOREIGN_KEYS.put(mainClass, masterForeignKeys);
                            }
                            masterForeignKeys.addAll(foreignKeys);
                        }
                    }
                } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                    throw new ReferenceException("索引初始化异常", e);
                }
            }
        }
    }
}
