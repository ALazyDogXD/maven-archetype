package __package__.common.mybatisplus.reference;

/**
 * @author ALazyDogXD
 * @date 2022/4/11 18:33
 * @description 引用模式
 */

public enum ForeignKeyModel {
    // 置空
    SET_NULL,
    // 级联
    CASCADE,
    // 严格
    RESTRICT
}
