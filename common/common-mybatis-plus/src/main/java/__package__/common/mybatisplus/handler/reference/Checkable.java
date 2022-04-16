package __package__.common.mybatisplus.handler.reference;

/**
 * @author ALazyDogXD
 * @date 2022/4/13 15:19
 * @description 索引检测
 */

public interface Checkable<S> {

    void saveWithCheckReference(S s);

}
