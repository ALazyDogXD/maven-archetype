package __package__.common.mybatisplus.service;

import __package__.common.mybatisplus.exception.ReferenceException;
import __package__.common.mybatisplus.mapper.ReferenceMapper;
import __package__.common.mybatisplus.reference.Referable;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import org.springframework.transaction.annotation.Transactional;

import java.lang.ref.Reference;
import java.util.Arrays;
import java.util.List;

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
        return saveWithCheckReference(e, true);
    }

    /**
     * 插入一条记录
     *
     * @param e 实体对象
     * @param throwEx 插入失败是否抛出异常
     * @return 是否插入成功
     */
    @Transactional(rollbackFor = Exception.class)
    default boolean saveWithCheckReference(E e, boolean throwEx) {
        boolean flag;
        if (!(flag = SqlHelper.retBool(getReferenceMapper().saveWithCheckReference(e))) && throwEx) {
            List<String> tableNames = getReferenceMapper().checkReference(e);
            throw new ReferenceException("must insert master table " + tableNames + " at first");
        }
        return flag;
    }

    /**
     * 获取对应 entity 的 BaseMapper
     *
     * @return BaseMapper
     */
    ReferenceMapper<E> getReferenceMapper();
}
