package __package__.common.mybatisplus.handler.reference;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.lang.reflect.Type;
import java.util.function.Function;

import static __package__.common.base.util.ReflectUtil.getInterfaceGenericsType;
import static __package__.common.mybatisplus.handler.reference.ForeignKeyModel.RESTRICT;

/**
 * @author ALazyDogXD
 * @date 2022/4/10 23:18
 * @description 外键
 */

public class ForeignKey<M, S extends Referable<S>, T> {

    private final Type generics = getInterfaceGenericsType(this.getClass(), Referable.class);

    private Class<M> mClass;

    private SFunction<M, T> m;

    private String mainFieldName;

    private SFunction<S, T> s;

    private String slaveFieldName;

    private ForeignKeyModel deleteModel = RESTRICT;

    private ForeignKeyModel updateModel = RESTRICT;

    static <M, S extends Referable<S>, T> ForeignKey<M, S, T> create(SFunction<S, T> s, Class<M> mClass, SFunction<M, T> m) {
        ForeignKey<M, S, T> foreignKey = new ForeignKey<>();
        foreignKey.s = s;
        foreignKey.m = m;
        foreignKey.mClass = mClass;
        return foreignKey;
    }

    public ForeignKey<M, S, T> modify(ForeignKeyModel deleteModel, ForeignKeyModel updateModel) {
        if (deleteModel != null) {
            this.deleteModel = deleteModel;
        }
        if (deleteModel != null) {
            this.updateModel = updateModel;
        }
        return this;
    }

    public T getSlaveValue(Object s) {
        if (generics.equals(s.getClass())) {
            //noinspection unchecked
            return this.s.apply((S) s);
        } else {
            throw new ClassCastException("传入类型" + s.getClass() + "与" + generics + "不符");
        }
    }

    public T getMainValue(Object m) {
        if (mClass.equals(m.getClass())) {
            //noinspection unchecked
            return this.m.apply((M) m);
        } else {
            throw new ClassCastException("传入类型" + m.getClass() + "与" + mClass + "不符");
        }
    }

    public Function<M, T> getMainFields() {
        return m;
    }

    public Class<M> getMainClass() {
        return mClass;
    }

    public ForeignKeyModel getDeleteModel() {
        return deleteModel;
    }

    public ForeignKeyModel getUpdateModel() {
        return updateModel;
    }

    public String getMainSelectSql(Object s) {
        return new LambdaQueryWrapper<M>().select(m).eq(m, getSlaveValue(s)).getSqlSelect();
    }

}
