package __package__.common.mybatisplus.reference.sql;

import __package__.common.mybatisplus.enums.ReferenceMethod;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import static __package__.common.mybatisplus.enums.ReferenceMethod.DELETE_WITH_CHECK_REFERENCE_RESTRICT;

/**
 * @author ALazyDogXD
 * @date 2022/7/10 14:17
 * @description 删除 + 外键检测
 */

public class DeleteWithCheckReference extends AbstractMethod {
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        // 判断引用模式 (仅处理级联与严格模式)
        // 判断是否逻辑删除 (不处理所有逻辑删除)
        // 组装 SQL 语句

        String sql = null;
        ReferenceMethod referenceMethod = DELETE_WITH_CHECK_REFERENCE_RESTRICT;

        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return addDeleteMappedStatement(mapperClass, DELETE_WITH_CHECK_REFERENCE_RESTRICT.getMethod(), sqlSource);
    }
}
