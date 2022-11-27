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

    static List<? extends ForeignKey<?, ? extends Referable<?>, ?>> getSlaveForeignKeys(Type sType) {
        return SLAVE_FOREIGN_KEYS.getOrDefault(sType, Collections.emptyList());
    }

    static List<? extends ForeignKey<?, ? extends Referable<?>, ?>> getMasterForeignKeys(Type mType) {
        return MASTER_FOREIGN_KEYS.getOrDefault(mType, Collections.emptyList());
    }

    static boolean foreignKeysIsEmpty() {
        return SLAVE_FOREIGN_KEYS.isEmpty() && MASTER_FOREIGN_KEYS.isEmpty();
    }
}
