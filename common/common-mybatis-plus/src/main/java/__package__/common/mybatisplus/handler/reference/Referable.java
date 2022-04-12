package __package__.common.mybatisplus.handler.reference;

import java.util.List;

/**
 * @author ALazyDogXD
 * @date 2022/4/10 22:09
 * @description 外键关联
 */
public interface Referable<S extends Referable<S>> {

    /**
     * 关联主表和从表
     *
     * @return 外键
     */
    List<ForeignKey<?, S, ?>> refer();

    /**
     * 关联主表和从表
     *
     * @return 外键创建者
     */
    default ForeignKeyBuilder<S> createBuilderOfReference() {
        return new ForeignKeyBuilder<>();
    }

}
