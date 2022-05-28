package __package__.common.mybatisplus.handler.reference;

import __package__.common.base.util.ReflectUtil;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.parser.JsqlParserSupport;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;

import java.lang.reflect.Type;
import java.sql.SQLException;

/**
 * @author ALazyDogXD
 * @date 2022/4/13 0:33
 * @description 关联拦截器
 */

public class ReferenceInterceptor extends JsqlParserSupport implements InnerInterceptor {

    @Override
    public void beforeUpdate(Executor executor, MappedStatement ms, Object parameter) {
        if (!ReferenceContext.foreignKeysIsEmpty() &&
                parameter instanceof Referable) {
            Referable<?> reference = (Referable<?>) parameter;
            BoundSql boundSql = ms.getBoundSql(reference);
            Type entityType = ReflectUtil.getInterfaceGenericsType(reference.getClass().getSuperclass(), Referable.class);
            String sql = boundSql.getSql();
            switch (ms.getSqlCommandType()) {
                case INSERT:
                    sql = spliceSqlForInsert(sql, entityType, reference);
                    break;
                case UPDATE:
                    sql = spliceSqlForUpdate(sql, entityType, reference);
                    break;
                case DELETE:
                    sql = spliceSqlForDelete(sql, entityType, reference);
                    break;
                default:
                    break;
            }
            PluginUtils.MPBoundSql mpBoundSql = PluginUtils.mpBoundSql(boundSql);
            mpBoundSql.sql(sql);
            record(ms.getSqlCommandType(), entityType, reference);
        }
    }

    private String spliceSqlForInsert(String sql, Type rt, Referable<?> reference) {
        // 检查主表是否可关联
        StringBuilder s = new StringBuilder(modifySqlForInsert(sql));
        s.append(" WHERE");
        for (ForeignKey<?, ? extends Referable<?>, ?> foreignKey : ReferenceContext.getSlaveForeignKeys(rt)) {
            if (!s.toString().endsWith("WHERE")) {
                s.append(" AND ");
            }
            String mainSelectSql = foreignKey.getMainSelectSql(reference);
            s.append(" EXISTS(").append(mainSelectSql).append(") ");
        }
        return s.toString();
    }

    private String modifySqlForInsert(String sql) {
        StringBuilder s = new StringBuilder(sql.substring(0, sql.indexOf("VALUES")));
        s.append("SELECT * FROM (SELECT ");
        String[] insertFields = sql.substring(sql.indexOf("(") + 1, sql.indexOf(")")).split(",");
        for (String insertField : insertFields) {
            s.append("?").append(insertField).append(",");
        }
        s.deleteCharAt(s.length() - 1);
        s.append(") valid_reference ");
        return s.toString();
    }

    private String spliceSqlForUpdate(String sql, Type rt, Referable<?> reference) {
        // 检查主表是否可关联
        return null;
    }

    private String spliceSqlForDelete(String sql, Type rt, Referable<?> reference) {
        // 检查主表是否可删除
        return "";
    }

    private void record(SqlCommandType type, Type rt, Referable<?> reference) {
        // 记录日志
    }

}
