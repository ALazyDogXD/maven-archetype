package __package__.common.base.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ALazyDogXD
 * @date 2022/4/11 0:02
 * @description 泛型工具类
 */

public class ReflectUtil {

    private ReflectUtil() {
    }

    public static Class<?> getInterface(Class<?> c, Class<?> i) {
        for (Class<?> anInterface : c.getInterfaces()) {
            if (anInterface == i) {
                return anInterface;
            }
            Class<?> result = getInterface(anInterface, i);
            if (result != null) {
                return result;
            }
        }
        if (c.getSuperclass() != Object.class) {
            return getInterface(c.getSuperclass(), i);
        }
        return null;
    }

    public static Class<?> getSonInterface(Class<?> c, Class<?> i) {
        for (Class<?> anInterface : c.getInterfaces()) {
            boolean flag = false;
            for (Class<?> anInterfaceInterface : anInterface.getInterfaces()) {
                if ((anInterfaceInterface == i)) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                return anInterface;
            }
        }
        if (c.getSuperclass() != Object.class) {
            return getInterface(c.getSuperclass(), i);
        }
        return null;
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

    public static Type getGenericsType(Class<?> c) {
        return getGenericsType(c, 0);
    }

    public static Type getGenericsType(Class<?> c, int i) {
        ParameterizedType p;
        Type mType = null;
        Type[] interfaces = c.getGenericInterfaces(), ts;
        for (Type t : interfaces) {
            if (t instanceof ParameterizedType) {
                p = (ParameterizedType) t;
                if ((ts = p.getActualTypeArguments()) != null) {
                    mType = ts[i];
                }
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
