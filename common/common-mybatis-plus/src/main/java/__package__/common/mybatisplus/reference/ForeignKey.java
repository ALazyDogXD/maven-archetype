package __package__.common.mybatisplus.reference;

import __package__.common.mybatisplus.enums.ForeignKeyModel;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.toolkit.GlobalConfigUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.core.toolkit.support.SerializedLambda;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import org.apache.ibatis.session.Configuration;

import static __package__.common.mybatisplus.enums.ForeignKeyModel.RESTRICT;

/**
 * @author ALazyDogXD
 * @date 2022/4/10 23:18
 * @description 外键
 */

public class ForeignKey<M, S extends Referable<S>, T> {

    private Class<S> sClass;

    private Class<M> mClass;

    private volatile String mFieldName;

    private SFunction<M, T> m;

    private SFunction<S, T> s;

    private ForeignKeyModel deleteModel = RESTRICT;

    private ForeignKeyModel updateModel = RESTRICT;

    private volatile String sTableName;

    private volatile String mTableName;

    static <M, S extends Referable<S>, T> ForeignKey<M, S, T> create(Class<S> sClass, SFunction<S, T> s, Class<M> mClass, SFunction<M, T> m) {
        ForeignKey<M, S, T> foreignKey = new ForeignKey<>();
        foreignKey.s = s;
        foreignKey.m = m;
        foreignKey.sClass = sClass;
        foreignKey.mClass = mClass;
        return foreignKey;
    }

    public void initMainTable(GlobalConfig.DbConfig dbConfig) {
        if (mTableName == null) {
            synchronized (this) {
                if (mTableName == null) {
                    mTableName = initTableName(dbConfig, mClass);
                }
            }
        }
    }

    public void initSlaveTable() {
        if (sTableName == null) {
            synchronized (this) {
                if (sTableName == null) {
                    sTableName = SqlHelper.table(sClass).getTableName();
                }
            }
        }
    }

    public ForeignKey<M, S, T> modify(ForeignKeyModel deleteModel, ForeignKeyModel updateModel) {
        if (deleteModel != null) {
            this.deleteModel = deleteModel;
        }
        if (deleteModel != null) {
            this.updateModel = updateModel;
        }
        return this;
    }

    public Class<M> getMainClass() {
        return mClass;
    }

    public boolean isUpdateModel(ForeignKeyModel model) {
        return updateModel.equals(model);
    }

    public boolean isDeleteModel(ForeignKeyModel model) {
        return deleteModel.equals(model);
    }

    public String getSlaveCheckSelectSql(Configuration configuration) {
        initSlaveTable();
        initMainTableInfo(configuration);
        String sSqlSelect = new LambdaQueryWrapper<S>().select(s).getSqlSelect();
        return "SELECT '" + sTableName + "' table_name FROM " + sTableName + " WHERE " + sSqlSelect + " = #{" + mFieldName + "} HAVING COUNT(" + sSqlSelect + ") = 0";
    }

    public String getMainCheckSelectSql(Configuration configuration) {
        initMainTableInfo(configuration);
        String sSqlSelect = new LambdaQueryWrapper<S>().select(s).getSqlSelect();
        return "SELECT '" + mTableName + "' table_name FROM " + mTableName + " WHERE " + mFieldName + " = #{" + sSqlSelect + "} HAVING COUNT(" + mFieldName + ") = 0";
    }

    public String getSlaveSelectSql(Configuration configuration) {
        initSlaveTable();
        initMainTableInfo(configuration);
        String sSqlSelect = new LambdaQueryWrapper<S>().select(s).getSqlSelect();
        return "SELECT " + sSqlSelect + " FROM " + sTableName + " WHERE " + sSqlSelect + " = #{" + mFieldName + "}";
    }

    public String getMainSelectSql(Configuration configuration) {
        initMainTableInfo(configuration);
        String sSqlSelect = new LambdaQueryWrapper<S>().select(s).getSqlSelect();
        return "SELECT " + mFieldName + " FROM " + mTableName + " WHERE " + mFieldName + " = #{" + sSqlSelect + "}";
    }

    private void initMainTableInfo(Configuration configuration) {
        GlobalConfig.DbConfig dbConfig = GlobalConfigUtils.getGlobalConfig(configuration).getDbConfig();
        initMainTable(dbConfig);
        initMainFieldName(configuration);
    }

    private String initTableName(GlobalConfig.DbConfig dbConfig, Class<?> clazz) {
        TableName table = clazz.getAnnotation(TableName.class);

        String tableName = clazz.getSimpleName();
        String tablePrefix = dbConfig.getTablePrefix();
        String schema = dbConfig.getSchema();
        boolean tablePrefixEffect = true;

        if (table != null) {
            if (StringUtils.isNotBlank(table.value())) {
                tableName = table.value();
                if (StringUtils.isNotBlank(tablePrefix) && !table.keepGlobalPrefix()) {
                    tablePrefixEffect = false;
                }
            } else {
                tableName = initTableNameWithDbConfig(tableName, dbConfig);
            }
            if (StringUtils.isNotBlank(table.schema())) {
                schema = table.schema();
            }
        } else {
            tableName = initTableNameWithDbConfig(tableName, dbConfig);
        }

        String targetTableName = tableName;
        if (StringUtils.isNotBlank(tablePrefix) && tablePrefixEffect) {
            targetTableName = tablePrefix + targetTableName;
        }
        if (StringUtils.isNotBlank(schema)) {
            targetTableName = schema + StringPool.DOT + targetTableName;
        }

        return targetTableName;
    }

    private String initTableNameWithDbConfig(String className, GlobalConfig.DbConfig dbConfig) {
        String tableName = className;
        // 开启表名下划线申明
        if (dbConfig.isTableUnderline()) {
            tableName = StringUtils.camelToUnderline(tableName);
        }
        // 大写命名判断
        if (dbConfig.isCapitalMode()) {
            tableName = tableName.toUpperCase();
        } else {
            // 首字母小写
            tableName = StringUtils.firstToLowerCase(tableName);
        }
        return tableName;
    }

    private void initMainFieldName(Configuration configuration) {
        if (mFieldName == null) {
            synchronized (this) {
                if (mFieldName == null) {
                    mFieldName = SerializedLambda.resolve(m).getImplMethodName().replace("get", "");
                    mFieldName = StringUtils.firstToLowerCase(mFieldName);
                    if (configuration.isMapUnderscoreToCamelCase()) {
                        mFieldName = StringUtils.camelToUnderline(mFieldName);
                    }
                }
            }
        }
    }

}
