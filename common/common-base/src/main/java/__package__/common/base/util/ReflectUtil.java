package __package__.common.base.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author ALazyDogXD
 * @date 2022/4/11 0:02
 * @description 泛型工具类
 */

public class ReflectUtil {

    private ReflectUtil() {
    }

    public static Type getInterfaceGenericsType(Class<?> c, Class<?> ic) {
        return getInterfaceGenericsType(c, ic, 1, 0);
    }

    public static Type getInterfaceGenericsType(Class<?> c, Class<?> ic, int genericsCount, int i) {
        ParameterizedType p;
        Type mType = null;
        Type[] interfaces = c.getGenericInterfaces(), ts;
        for (Type t : interfaces) {
            if (t instanceof ParameterizedType &&
                    (p = (ParameterizedType) t).getRawType() == ic &&
                    (ts = p.getActualTypeArguments()) != null &&
                    ts.length == genericsCount) {
                mType = ts[i];
            }
        }
        return mType;
    }

    public static Type[] getInterfaceGenericsTypes(Class<?> c, Class<?> ic) {
        ParameterizedType p;
        Type[] interfaces = c.getGenericInterfaces();
        for (Type t : interfaces) {
            if (t instanceof ParameterizedType &&
                    (p = (ParameterizedType) t).getRawType() == ic) {
                return p.getActualTypeArguments();
            }
        }
        return null;
    }

}
