package __package__.common.mybatisplus.reference.sql;

import __package__.common.mybatisplus.enums.ForeignKeyModel;
import __package__.common.mybatisplus.exception.ReferenceException;
import __package__.common.mybatisplus.mapper.ReferenceMapper;
import __package__.common.mybatisplus.reference.ForeignKey;
import __package__.common.mybatisplus.reference.Referable;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author alazydogxd
 * @date 2022/5/23 11:19 PM
 * @description 外键服务
 */
public interface ReferenceService<E> extends IService<E> {

    /**
     * 插入一条记录
     *
     * @param entity 实体对象
     * @return 是否插入成功
     */
    default boolean saveWithCheckReference(E entity) {
        return saveWithCheckReference(entity, true);
    }

    /**
     * 插入一条记录
     *
     * @param entity  实体对象
     * @param throwEx 插入失败是否抛出异常
     * @return 是否插入成功
     */
    @Transactional(rollbackFor = Exception.class)
    default boolean saveWithCheckReference(E entity, boolean throwEx) {
        boolean flag;
        if (!(flag = SqlHelper.retBool(getReferenceMapper().saveWithCheckReference(entity))) && throwEx) {
            List<String> tableNames = getReferenceMapper().checkReference(entity);
            throw new ReferenceException("must insert master table " + tableNames + " at first");
        }
        return flag;
    }

    /**
     * 删除一条记录
     *
     * @param id 实体对象 ID
     * @return 删除是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    default boolean deleteWithCheckReference(Serializable id) {
        return batchDeleteWithCheckReference(Collections.singletonList(id));
    }

    /**
     * 删除多条条记录
     *
     * @param ids 实体对象 ID 集合
     * @return 删除是否成功
     */
    default boolean batchDeleteWithCheckReference(List<Serializable> ids) {
        if (ids.isEmpty()) {
            return false;
        }
        boolean flag = false;
        // 引用分组
        Map<ForeignKeyModel, ? extends List<? extends ForeignKey<?, ? extends Referable<?>, ?>>> masterForeignKeyMap = getMasterForeignKeyMap();

        // 查询所有索引列的值
        List<E> entities = getReferenceMapper().selectList(getSlaveSelectSql(ids));

        // 严格模式: 引用存在直接报错
        // 置空模式: 引用主表列置空
        // 级联模式: 批量多表删除 与 逻辑删除
        return flag;
    }

    /**
     * 获取对应 entity 的 BaseMapper
     *
     * @return BaseMapper
     */
    ReferenceMapper<E> getReferenceMapper();

    /**
     * 获取外键引用, 按外键模式分组
     *
     * @return 外键引用
     */
    Map<ForeignKeyModel, ? extends List<? extends ForeignKey<?, ? extends Referable<?>, ?>>> getMasterForeignKeyMap();

    /**
     * 获取从表查询语句
     *
     * @param ids 主键集合
     * @return 从表查询语句
     */
    QueryWrapper<E> getSlaveSelectSql(List<Serializable> ids);

}
