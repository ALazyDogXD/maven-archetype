package __package__.common.mybatisplus.reference.sql;

import __package__.common.mybatisplus.reference.ForeignKey;
import __package__.common.mybatisplus.reference.Referable;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import static __package__.common.mybatisplus.constant.RelationSqlKeyWord.UNION_ALL;
import static __package__.common.mybatisplus.enums.ReferenceMethod.CHECK_REFERENCE;

/**
 * @author ALazyDogXD
 * @date 2022/5/29 18:22
 * @description 检查主表是否存在约束值
 */

public class CheckMasterReference extends AbstractMethod {
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, String.format(CHECK_REFERENCE.getSqlTemplate(), prepareCheckSql(tableInfo)), modelClass);
        return this.addSelectMappedStatementForOther(mapperClass, CHECK_REFERENCE.getMethod(), sqlSource, String.class);
    }

    private String prepareCheckSql(TableInfo tableInfo) {
        StringBuilder checkSql = new StringBuilder();

        for (ForeignKey<?, ? extends Referable<?>, ?> slaveForeignKey : ReferenceContext.getSlaveForeignKeys(tableInfo.getEntityType())) {
            checkSql.append(slaveForeignKey.getMainCheckSelectSql(configuration)).append(SPACE).append(UNION_ALL).append(SPACE);
        }

        return SqlScriptUtils.convertTrim(checkSql.toString(),
                null, null, null, UNION_ALL);
    }

}
