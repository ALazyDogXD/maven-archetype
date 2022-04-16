package __package__.common.mybatisplus.handler.reference;

import __package__.common.base.util.ReflectUtil;
import __package__.common.mybatisplus.exception.ReferenceException;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

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
 * @date 2022/4/13 1:14
 * @description 关联上下文
 */

@Primary
@Component
public class ReferenceContext implements ApplicationRunner {

    private static final Map<Type, List<? extends ForeignKey<?, ? extends Referable<?>, ?>>> SLAVE_FOREIGN_KEYS = new ConcurrentHashMap<>(32);

    private static final Map<Type, List<ForeignKey<?, ? extends Referable<?>, ?>>> MASTER_FOREIGN_KEYS = new ConcurrentHashMap<>(32);

    @Lazy
    @Resource
    private List<IService<?>> iServices;

    @Override
    public void run(ApplicationArguments args) {
        if (iServices != null) {
            for (IService<?> iService : iServices) {
                // 找到继承 IService 的接口
                Class<?> iServiceClass = null;
                for (Class<?> i : iService.getClass().getSuperclass().getInterfaces()) {
                    if (IService.class.isAssignableFrom(i)) {
                        iServiceClass = i;
                    }
                }
                if (iServiceClass == null) {
                    return;
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

    static List<? extends ForeignKey<?, ? extends Referable<?>, ?>> getSlaveForeignKeys(Type sType) {
        return SLAVE_FOREIGN_KEYS.get(sType);
    }

    static List<? extends ForeignKey<?, ? extends Referable<?>, ?>> getMasterForeignKeys(Type mType) {
        return MASTER_FOREIGN_KEYS.get(mType);
    }

    static boolean foreignKeysIsEmpty() {
        return SLAVE_FOREIGN_KEYS.isEmpty() && MASTER_FOREIGN_KEYS.isEmpty();
    }

}
