package __package__.common.mybatisplus.handler.reference;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import org.springframework.util.StringUtils;

import java.util.function.Function;

import static __package__.common.mybatisplus.handler.reference.ForeignKeyModel.RESTRICT;

/**
 * @author ALazyDogXD
 * @date 2022/4/10 23:18
 * @description 外键
 */

public class ForeignKey<M, S extends Referable<S>, T> {

    private Class<S> sClass;

    private Class<M> mClass;

    private SFunction<M, T> m;

    private SFunction<S, T> s;

    private ForeignKeyModel deleteModel = RESTRICT;

    private ForeignKeyModel updateModel = RESTRICT;

    private String sTableName;

    private String mTableName;

    static <M, S extends Referable<S>, T> ForeignKey<M, S, T> create(Class<S> sClass, SFunction<S, T> s, Class<M> mClass, SFunction<M, T> m) {
        ForeignKey<M, S, T> foreignKey = new ForeignKey<>();
        foreignKey.s = s;
        foreignKey.m = m;
        foreignKey.sClass = sClass;
        foreignKey.sTableName = SqlHelper.table(sClass).getTableName();
        foreignKey.mClass = mClass;
        return foreignKey;
    }

    public void initMainTable() {
        mTableName = SqlHelper.table(mClass).getTableName();
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

    public T getSlaveValue(Object s) {
        if (sClass.isAssignableFrom(s.getClass())) {
            //noinspection unchecked
            return this.s.apply((S) s);
        } else {
            throw new ClassCastException("传入类型" + s.getClass() + "与" + sClass + "不符");
        }
    }

    public T getMainValue(Object m) {
        if (mClass.isAssignableFrom(m.getClass())) {
            //noinspection unchecked
            return this.m.apply((M) m);
        } else {
            throw new ClassCastException("传入类型" + m.getClass() + "与" + mClass + "不符");
        }
    }

    public Function<M, T> getMainFields() {
        return m;
    }

    public Class<M> getMainClass() {
        return mClass;
    }

    public ForeignKeyModel getDeleteModel() {
        return deleteModel;
    }

    public ForeignKeyModel getUpdateModel() {
        return updateModel;
    }

    public String getSlaveSelectSql(Object m) {
        LambdaQueryWrapper<S> wrapper = new LambdaQueryWrapper<S>().select(s).eq(s, getMainValue(m));
        String segment = wrapper.getSqlSegment();
        String replace = StringUtils.replace(segment, segment.substring(segment.indexOf("#{"), segment.indexOf("}") + 1), getSlaveValue(m).toString());
        return "SELECT " + wrapper.getSqlSelect() + " FROM " + sTableName + " WHERE " + replace;
    }

    public String getMainSelectSql(Object s) {
        LambdaQueryWrapper<M> wrapper = new LambdaQueryWrapper<M>().select(m).eq(m, getSlaveValue(s));
        String segment = wrapper.getSqlSegment();
        String replace = StringUtils.replace(segment, segment.substring(segment.indexOf("#{"), segment.indexOf("}") + 1), getSlaveValue(s).toString());
        return "SELECT " + wrapper.getSqlSelect() + " FROM " + mTableName + " WHERE " + replace;
    }

}
