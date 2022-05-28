package __package__.common.mybatisplus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author alazydogxd
 * @date 2022/5/24 12:31 PM
 * @description 外键 MAPPER
 */
public interface ReferenceMapper<E> extends BaseMapper<E> {

    /**
     * 插入数据同时检查外键约束
     *
     * @param entity 实体对象
     * @return 影响行数
     */
    int saveWithCheckReference(E entity);

}
