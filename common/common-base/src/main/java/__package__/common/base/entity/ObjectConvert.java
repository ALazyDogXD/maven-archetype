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

    /**
     * 对象转换前置处理
     */
    default void beforeConvert() {
    }

    /**
     * 对象转换前置处理
     *
     * @param t 入参对象 t 转 this
     */
    default void beforeConvert(T t) {
    }

    /**
     * 对象转换后置处理
     *
     * @param t 入参对像
     */
    default void afterConvert(T t) {
    }

    /**
     * 实体转换 this -> t
     *
     * @return t
     */
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

    /**
     * 实体转换 t -> this
     *
     * @param t 入参对象
     * @return this
     */
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
