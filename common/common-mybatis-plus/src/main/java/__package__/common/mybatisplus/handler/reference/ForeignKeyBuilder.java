package __package__.common.mybatisplus.handler.reference;

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

    ForeignKeyBuilder() {
    }

    public <M, T> ForeignKeyBuilder<S> add(SFunction<S, T> s, Class<M> mClass, SFunction<M, T> m) {
        foreignKeys.add(ForeignKey.create(s, mClass, m));
        return this;
    }

    public <M, T> ForeignKeyBuilder<S> add(SFunction<S, T> s, Class<M> mClass, SFunction<M, T> m, ForeignKeyModel deleteModel, ForeignKeyModel updateModel) {
        foreignKeys.add(ForeignKey.create(s, mClass, m).modify(deleteModel, updateModel));
        return this;
    }

    public List<ForeignKey<?, S, ?>> build() {
        return foreignKeys;
    }

}
