package __package__.common.mybatisplus.reference.sql;

import __package__.common.mybatisplus.reference.ForeignKey;
import __package__.common.mybatisplus.reference.Referable;
import org.springframework.util.SerializationUtils;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ALazyDogXD
 * @date 2022/4/13 1:14
 * @description 关联上下文
 */
public class ReferenceContext {

    static final Map<Type, List<? extends ForeignKey<?, ? extends Referable<?>, ?>>> SLAVE_FOREIGN_KEYS = new ConcurrentHashMap<>(32);

    static final Map<Type, List<ForeignKey<?, ? extends Referable<?>, ?>>> MASTER_FOREIGN_KEYS = new ConcurrentHashMap<>(32);

//    @SuppressWarnings("unchecked")
    static List<? extends ForeignKey<?, ? extends Referable<?>, ?>> getSlaveForeignKeys(Type sType) {
//        return (List<? extends ForeignKey<?, ? extends Referable<?>, ?>>) SerializationUtils.deserialize(SerializationUtils.serialize(SLAVE_FOREIGN_KEYS.getOrDefault(sType, Collections.emptyList())));
        return SLAVE_FOREIGN_KEYS.getOrDefault(sType, Collections.emptyList());
    }

//    @SuppressWarnings("unchecked")
    static List<? extends ForeignKey<?, ? extends Referable<?>, ?>> getMasterForeignKeys(Type mType) {
//        return (List<? extends ForeignKey<?, ? extends Referable<?>, ?>>) SerializationUtils.deserialize(SerializationUtils.serialize(MASTER_FOREIGN_KEYS.getOrDefault(mType, Collections.emptyList())));
        return MASTER_FOREIGN_KEYS.getOrDefault(mType, Collections.emptyList());
    }

    static boolean foreignKeysIsEmpty() {
        return SLAVE_FOREIGN_KEYS.isEmpty() && MASTER_FOREIGN_KEYS.isEmpty();
    }

//    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
//        if (!(bean instanceof ReferenceMapper)) return bean;
//        Class<?> mapperClass = ReflectUtil.getSonInterface(bean.getClass(), ReferenceMapper.class);
//
//        // 收集外键
//        Class<?> entityClass = (Class<?>) ReflectUtil.getInterfaceGenericsType(mapperClass, ReferenceMapper.class);
//        if (entityClass == null) {
//            return bean;
//        }
//        Constructor<?> constructor;
//        try {
//            if (Referable.class.isAssignableFrom(entityClass)) {
//                constructor = entityClass.getConstructor();
//                constructor.setAccessible(true);
//                Referable<?> r = (Referable<?>) constructor.newInstance();
//                List<? extends ForeignKey<?, ? extends Referable<?>, ?>> foreignKeys = r.refer();
//                // 收集外键, key 为从表类型
//                SLAVE_FOREIGN_KEYS.put(entityClass, foreignKeys);
//                // 收集外键, key 为主表类型
//                for (ForeignKey<?, ? extends Referable<?>, ?> foreignKey : foreignKeys) {
//                    foreignKey.initMainTable();
//                    Class<?> mainClass = foreignKey.getMainClass();
//                    List<ForeignKey<?, ? extends Referable<?>, ?>> masterForeignKeys;
//                    if ((masterForeignKeys = MASTER_FOREIGN_KEYS.get(mainClass)) == null) {
//                        masterForeignKeys = new ArrayList<>();
//                        MASTER_FOREIGN_KEYS.put(mainClass, masterForeignKeys);
//                    }
//                    masterForeignKeys.addAll(foreignKeys);
//                }
//            }
//        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
//            throw new ReferenceException("索引初始化异常", e);
//        }
//        return bean;
//    }
//
//    @SuppressWarnings("rawtypes")
//    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        Collection<ReferenceMapper> mappers = applicationContext.getBeansOfType(ReferenceMapper.class).values();
//        if (!mappers.isEmpty()) {
//            for (ReferenceMapper<?> mapper : mappers) {
//                // 找到继承 IService 的接口
//                Class<?> mapperClass = ReflectUtil.getSonInterface(mapper.getClass(), ReferenceMapper.class);
////                for (Class<?> i : iService.getClass().getSuperclass().getInterfaces()) {
//////                    if (IService.class.isAssignableFrom(i)) {
//////                        iServiceClass = i;
//////                    }
////
////                    if (i.isAssignableFrom(IService.class)) {
////                        iServiceClass = i;
////                    }
////                }
////                if (iServiceClass == null) {
////                    continue;
////                }
//
//                // 收集外键
//                Class<?> entityClass = (Class<?>) ReflectUtil.getInterfaceGenericsType(mapperClass, ReferenceMapper.class);
//                if (entityClass == null) {
//                    continue;
//                }
//                Constructor<?> constructor;
//                try {
//                    if (Referable.class.isAssignableFrom(entityClass)) {
//                        constructor = entityClass.getConstructor();
//                        constructor.setAccessible(true);
//                        Referable<?> r = (Referable<?>) constructor.newInstance();
//                        List<? extends ForeignKey<?, ? extends Referable<?>, ?>> foreignKeys = r.refer();
//                        // 收集外键, key 为从表类型
//                        SLAVE_FOREIGN_KEYS.put(entityClass, foreignKeys);
//                        // 收集外键, key 为主表类型
//                        for (ForeignKey<?, ? extends Referable<?>, ?> foreignKey : foreignKeys) {
//                            foreignKey.initMainTable();
//                            Class<?> mainClass = foreignKey.getMainClass();
//                            List<ForeignKey<?, ? extends Referable<?>, ?>> masterForeignKeys;
//                            if ((masterForeignKeys = MASTER_FOREIGN_KEYS.get(mainClass)) == null) {
//                                masterForeignKeys = new ArrayList<>();
//                                MASTER_FOREIGN_KEYS.put(mainClass, masterForeignKeys);
//                            }
//                            masterForeignKeys.addAll(foreignKeys);
//                        }
//                    }
//                } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
//                    throw new ReferenceException("索引初始化异常", e);
//                }
//            }
//        }
//    }
}
