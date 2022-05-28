package __package__.common.mybatisplus.service;

import __package__.common.mybatisplus.mapper.ReferenceMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;

/**
 * @author alazydogxd
 * @date 2022/5/23 11:19 PM
 * @description 外键服务
 */
public interface ReferenceService<E> extends IService<E> {

    /**
     * 插入一条记录
     *
     * @param e 实体对象
     * @return 是否插入成功
     */
    default boolean saveWithCheckReference(E e) {
        return SqlHelper.retBool(getReferenceMapper().saveWithCheckReference(e));
    }

    /**
     * 获取对应 entity 的 BaseMapper
     *
     * @return BaseMapper
     */
    ReferenceMapper<E> getReferenceMapper();
}
