package __package__.common.base.entity;

import com.fasterxml.jackson.databind.util.ClassUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.ParameterizedType;

/**
 * @author Mr_W
 * 对象转换
 */
public interface ObjectConvert<T> {

    Logger LOGGER = LoggerFactory.getLogger(ObjectConvert.class);

    default void beforeConvert() {
    }

    default void beforeConvert(T t) {
    }

    default void afterConvert(T t) {
    }

    @SuppressWarnings("unchecked")
    default T convert() {
        T t;
        beforeConvert();
        try {
            Class<T> clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            t = ClassUtil.createInstance(clazz, false);
            BeanUtils.copyProperties(this, t);
            LOGGER.debug("source: {} -> target :{}", this, t);
        } catch (Exception e) {
            throw new RuntimeException("Bean convert failed, Caused by " + e);
        }
        afterConvert(t);
        return t;
    }

    default Object convert(T t) {
        beforeConvert(t);
        try {
            BeanUtils.copyProperties(t, this);
            LOGGER.debug("source: {} -> target :{}", t, this);
        } catch (Exception e) {
            throw new RuntimeException("Bean convert failed, Caused by " + e);
        }
        afterConvert(t);
        return this;
    }

}
