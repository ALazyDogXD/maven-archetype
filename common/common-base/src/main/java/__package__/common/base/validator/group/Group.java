package __package__.common.base.validator.group;

/**
 * @author ALazyDogXD
 * @date 2022/3/13 16:18
 * @description 参数校验分组
 */

public interface Group {

    /**
     * 参数校验分组-创建
     */
    Class<?> CREATE = Create.class;

    /**
     * 参数校验分组-修改
     */
    Class<?> MODIFY = Modify.class;

}
