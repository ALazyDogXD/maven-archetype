package __package__.common.mybatisplus.reference;

import __package__.common.mybatisplus.enums.ForeignKeyModel;
import __package__.common.mybatisplus.exception.ReferenceException;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ALazyDogXD
 * @date 2022/4/12 10:46
 * @description 外键创建者
 */

public class ForeignKeyBuilder<S extends Referable<S>> {

    private final List<ForeignKey<?, S, ?>> foreignKeys = new ArrayList<>();

    private Class<S> sClass;

    ForeignKeyBuilder() {
    }

    public ForeignKeyBuilder<S> setEntityClass(Class<S> sClass) {
        this.sClass = sClass;
        return this;
    }

    public <M, T> ForeignKeyBuilder<S> add(SFunction<S, T> s, Class<M> mClass, SFunction<M, T> m) {
        foreignKeys.add(createForeignKey(s, mClass, m));
        return this;
    }

    public <M, T> ForeignKeyBuilder<S> add(SFunction<S, T> s, Class<M> mClass, SFunction<M, T> m, ForeignKeyModel deleteModel, ForeignKeyModel updateModel) {
        foreignKeys.add(createForeignKey(s, mClass, m).modify(deleteModel, updateModel));
        return this;
    }

    private <M, T> ForeignKey<M, S, T> createForeignKey(SFunction<S, T> s, Class<M> mClass, SFunction<M, T> m) {
        if (sClass == null) {
            throw new ReferenceException("请先设置 entityClass");
        }
        return ForeignKey.create(sClass, s, mClass, m);
    }

    public List<ForeignKey<?, S, ?>> build() {
        return foreignKeys;
    }

}
