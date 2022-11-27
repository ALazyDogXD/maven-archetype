package __package__.common.mybatisplus.reference.sql;

import __package__.common.base.util.ReflectUtil;
import __package__.common.mybatisplus.exception.ReferenceException;
import __package__.common.mybatisplus.mapper.ReferenceMapper;
import __package__.common.mybatisplus.reference.ForeignKey;
import __package__.common.mybatisplus.reference.Referable;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.lang.NonNull;

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
        methodList.add(new CheckMasterReference());
        methodList.add(new DeleteWithCheckReference());
        return methodList;
    }

    @Override
    public void setResourceLoader(@NonNull ResourceLoader resourceLoader) {
        Set<Class<?>> mapperClasses = new HashSet<>();
        PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
        ResourcePatternResolver resolver = ResourcePatternUtils.getResourcePatternResolver(pathMatchingResourcePatternResolver);
        CachingMetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(pathMatchingResourcePatternResolver);
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX.concat("/**/*.class");
        try {
            // TODO 加载时间待优化, 不要扫描全包
            for (Resource resource : resolver.getResources(packageSearchPath)) {
                if (resource.isReadable()) {
                    try {
                        MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                        String className = metadataReader.getClassMetadata().getClassName();

                        if (Arrays.stream(metadataReader.getClassMetadata().getInterfaceNames()).anyMatch(i -> i.equals(ReferenceMapper.class.getName()))) {
                            Class<?> aClass = Class.forName(className);
                            if (ReferenceMapper.class.isAssignableFrom(aClass) && aClass != ReferenceMapper.class) {
                                mapperClasses.add(aClass);
                            }
                        }
                    } catch (Exception e) {
                        // 忽略异常
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (!mapperClasses.isEmpty()) {
            for (Class<?> mapper : mapperClasses) {
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
