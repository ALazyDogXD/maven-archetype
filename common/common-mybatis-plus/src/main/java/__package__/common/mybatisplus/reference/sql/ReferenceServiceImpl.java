package __package__.common.mybatisplus.reference.sql;

import __package__.common.mybatisplus.enums.ForeignKeyModel;
import __package__.common.mybatisplus.mapper.ReferenceMapper;
import __package__.common.mybatisplus.reference.ForeignKey;
import __package__.common.mybatisplus.reference.Referable;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static __package__.common.mybatisplus.reference.sql.ReferenceContext.getMasterForeignKeys;

/**
 * @author alazydogxd
 * @date 2022/5/26 12:33 AM
 * @description 外键约束服务
 */
public class ReferenceServiceImpl<M extends ReferenceMapper<E>, E> extends ServiceImpl<M, E> implements ReferenceService<E> {

    @Autowired
    protected M referenceMapper;

    private volatile Map<ForeignKeyModel, ? extends List<? extends ForeignKey<?, ? extends Referable<?>, ?>>> masterForeignKeyMap;

    private volatile QueryWrapper<E> slaveSelectSql;

    @Override
    public ReferenceMapper<E> getReferenceMapper() {
        return referenceMapper;
    }

    @Override
    public Map<ForeignKeyModel, ? extends List<? extends ForeignKey<?, ? extends Referable<?>, ?>>> getMasterForeignKeyMap() {
        if (masterForeignKeyMap == null) {
            synchronized (this) {
                if (masterForeignKeyMap == null) {
                    List<? extends ForeignKey<?, ? extends Referable<?>, ?>> masterForeignKeys = getMasterForeignKeys(entityClass);
                    masterForeignKeyMap = masterForeignKeys.stream().collect(Collectors.groupingBy(ForeignKey::getDeleteModel));
                }
            }
        }
        return masterForeignKeyMap;
    }

    @Override
    public QueryWrapper<E> getSlaveSelectSql(List<Serializable> ids) {
        if (slaveSelectSql == null) {
            synchronized (this) {
                if (slaveSelectSql == null) {
                    String keyProperty = TableInfoHelper.getTableInfo(entityClass).getKeyProperty();
                    String[] selectSql = getMasterForeignKeys(entityClass).stream().map(ForeignKey::getSlaveKeyName).toArray(String[]::new);
                    slaveSelectSql = new QueryWrapper<E>().select(selectSql).in(keyProperty, ids);
                }
            }
        }
        return slaveSelectSql;
    }
}
