package __package__.common.mybatisplus.reference.sql;

import __package__.common.mybatisplus.reference.ForeignKey;
import __package__.common.mybatisplus.reference.Referable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.util.List;

import static __package__.common.mybatisplus.constant.Constant.SAVE_WITH_CHECK_REFERENCE;

/**
 * @author alazydogxd
 * @date 2022/5/24 12:40 PM
 * @description 插入 + 外键检测
 */
public class SaveWithCheckReference extends AbstractMethod {
    // INSERT INTO test (id, name)
    // SELECT * FROM (SELECT 4 id,  2 name) test
    // WHERE
    // EXISTS(SELECT count(*) FROM test WHERE id = 3) AND
    // EXISTS(SELECT count(*) FROM test WHERE id = 3)
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        //                                 t f         fw               fv  t  r
        String sql = "<script>INSERT INTO %s %s SELECT %s FROM (SELECT %s) %s %s</script>";
        String fieldSql = prepareFieldSql(tableInfo);
        String fieldWithoutBracketSql = prepareFieldWithoutBracketSql(tableInfo);
        String fieldWithValueSql = prepareFieldWithValueSql(tableInfo);
        String referenceCheckSql = prepareReferenceCheckSql(tableInfo);
        String sqlResult = String.format(
                sql,
                tableInfo.getTableName(),
                fieldSql,
                fieldWithoutBracketSql,
                fieldWithValueSql,
                tableInfo.getTableName(),
                referenceCheckSql);
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sqlResult, modelClass);
        KeyGenerator keyGenerator = new NoKeyGenerator();
        String keyProperty = null;
        String keyColumn = null;
        // 表包含主键处理逻辑,如果不包含主键当普通字段处理
        if (StringUtils.isNotBlank(tableInfo.getKeyProperty())) {
            if (tableInfo.getIdType() == IdType.AUTO) {
                // 自增主键
                keyGenerator = new Jdbc3KeyGenerator();
                keyProperty = tableInfo.getKeyProperty();
                keyColumn = tableInfo.getKeyColumn();
            } else {
                if (null != tableInfo.getKeySequence()) {
                    keyGenerator = TableInfoHelper.genKeyGenerator(SAVE_WITH_CHECK_REFERENCE, tableInfo, builderAssistant);
                    keyProperty = tableInfo.getKeyProperty();
                    keyColumn = tableInfo.getKeyColumn();
                }
            }
        }
        return addInsertMappedStatement(mapperClass, modelClass, SAVE_WITH_CHECK_REFERENCE, sqlSource, keyGenerator, keyProperty, keyColumn);
    }

    private String prepareFieldSql(TableInfo tableInfo) {
        return SqlScriptUtils.convertTrim(tableInfo.getAllInsertSqlColumnMaybeIf(null),
                LEFT_BRACKET, RIGHT_BRACKET, null, COMMA);
    }

    private String prepareFieldWithoutBracketSql(TableInfo tableInfo) {
        return SqlScriptUtils.convertTrim(tableInfo.getAllInsertSqlColumnMaybeIf(null),
                null, null, null, COMMA);
    }

    private String prepareFieldWithValueSql(TableInfo tableInfo) {
        String trim = SqlScriptUtils.convertTrim(tableInfo.getAllInsertSqlPropertyMaybeIf(null),
                null, null, null, COMMA);
        StringBuilder fieldWithValueSql = new StringBuilder(trim);
        int fromIndex;
        fieldWithValueSql.insert(fromIndex = fieldWithValueSql.indexOf("}") + 1, " " + tableInfo.getKeyColumn());
        for (TableFieldInfo tableFieldInfo : tableInfo.getFieldList()) {
            fieldWithValueSql.insert(fromIndex = fieldWithValueSql.indexOf("}", fromIndex) + 1, " " + tableFieldInfo.getColumn());
        }
        return fieldWithValueSql.toString();
    }

    private String prepareReferenceCheckSql(TableInfo tableInfo) {
        StringBuilder referenceCheckSql = new StringBuilder();
        List<? extends ForeignKey<?, ? extends Referable<?>, ?>> slaveForeignKeys = ReferenceContext.getSlaveForeignKeys(tableInfo.getEntityType());
        // 无引用, 不做外键判断
        if (slaveForeignKeys.isEmpty()) return "";
        for (ForeignKey<?, ? extends Referable<?>, ?> foreignKey : slaveForeignKeys) {
            referenceCheckSql
                    .append("EXISTS(")
                    .append(foreignKey.getMainSelectSql())
                    .append(") AND ");
        }
        return SqlScriptUtils.convertTrim(referenceCheckSql.toString(),
                "WHERE",
                null, null, "AND");
    }
}
