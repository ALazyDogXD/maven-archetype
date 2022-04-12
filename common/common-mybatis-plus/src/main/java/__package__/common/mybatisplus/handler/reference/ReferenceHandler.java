package __package__.common.mybatisplus.handler.reference;

import __package__.common.base.util.ReflectUtil;
import __package__.common.mybatisplus.exception.ReferenceException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ALazyDogXD
 * @date 2022/4/10 23:29
 * @description 关联处理器
 */
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
public class ReferenceHandler implements Interceptor, BeanPostProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReferenceHandler.class);

    private static final Map<Type, List<? extends ForeignKey<?, ? extends Referable<?>, ?>>> SLAVE_FOREIGN_KEYS = new ConcurrentHashMap<>(32);

    private static final Map<Type, List<ForeignKey<?, ? extends Referable<?>, ?>>> MASTER_FOREIGN_KEYS = new ConcurrentHashMap<>(32);

    private static final Map<Type, IService<?>> SERVICES = new ConcurrentHashMap<>(32);

    @Resource
    private ThreadPoolTaskExecutor pool;

    @Override
    public Object postProcessAfterInitialization(@Nullable Object bean, @Nullable String beanName) throws BeansException {
        if (bean == null || beanName == null) {
            return bean;
        }

        if (bean instanceof IService) {
            // 收集 IService
            SERVICES.put(bean.getClass(), (IService<?>) bean);

            // 找到继承 IService 的接口
            Class<?> iServiceClass = null;
            for (Class<?> i : bean.getClass().getSuperclass().getInterfaces()) {
                if (IService.class.isAssignableFrom(i)) {
                    iServiceClass = i;
                }
            }
            if (iServiceClass == null) {
                return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
            }

            // 收集外键
            Class<?> entityClass = (Class<?>) ReflectUtil.getInterfaceGenericsType(iServiceClass, IService.class);
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
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object result = null;
        try {
            if (!SERVICES.isEmpty() && invocation.getArgs().length >= 2 &&
                    invocation.getArgs()[0] instanceof MappedStatement &&
                    invocation.getArgs()[1] instanceof Referable) {
                MappedStatement statement = (MappedStatement) invocation.getArgs()[0];
                Referable<?> reference = (Referable<?>) invocation.getArgs()[1];
                Type entityType = ReflectUtil.getInterfaceGenericsType(reference.getClass(), Referable.class);
                switch (statement.getSqlCommandType()) {
                    case INSERT:
                        checkInsert(entityType, reference);
                        break;
                    case UPDATE:
                        checkUpdate(entityType, reference);
                        break;
                    case DELETE:
                        checkDelete(entityType, reference);
                        break;
                    default:
                        break;
                }
                result = invocation.proceed();
                if (result instanceof Boolean && !(Boolean) result) {
                    record(statement.getSqlCommandType(), entityType, reference);
                    throw new ReferenceException("数据无法关联");
                }
            }
        } catch (Exception e) {
            LOGGER.error("", e);
        }
        if (result == null) {
            return invocation.proceed();
        } else {
            return result;
        }
    }

    private void checkInsert(Type rt, Referable<?> reference) {
        // 检查主表是否可关联
        for (ForeignKey<?, ? extends Referable<?>, ?> foreignKey : SLAVE_FOREIGN_KEYS.get(rt)) {
            Object slaveValue = foreignKey.getSlaveValue(reference);
        }
    }

    private void checkUpdate(Type rt, Referable<?> reference) {

    }

    private void checkDelete(Type rt, Referable<?> reference) {

    }

    private void record(SqlCommandType type, Type rt, Referable<?> reference) {
        pool.execute(() -> {
            // 记录错误日志
        });
    }

}
