//package __package__.common.mybatisplus.reference;
//
//import __package__.common.base.util.ReflectUtil;
//import __package__.common.mybatisplus.exception.ReferenceException;
//import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
//import com.baomidou.mybatisplus.extension.service.IService;
//import org.apache.ibatis.executor.Executor;
//import org.apache.ibatis.mapping.BoundSql;
//import org.apache.ibatis.mapping.MappedStatement;
//import org.apache.ibatis.mapping.SqlCommandType;
//import org.apache.ibatis.plugin.Interceptor;
//import org.apache.ibatis.plugin.Intercepts;
//import org.apache.ibatis.plugin.Invocation;
//import org.apache.ibatis.plugin.Signature;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.core.annotation.Order;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//
//import javax.annotation.Resource;
//import java.lang.reflect.Constructor;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Type;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
///**
// * @author ALazyDogXD
// * @date 2022/4/10 23:29
// * @description 关联处理器
// */
//@Intercepts({
//        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
//})
//@Order(-1)
//public class ReferenceHandler implements Interceptor, CommandLineRunner {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(ReferenceHandler.class);
//
//    private static final Map<Type, List<? extends ForeignKey<?, ? extends Referable<?>, ?>>> SLAVE_FOREIGN_KEYS = new ConcurrentHashMap<>(32);
//
//    private static final Map<Type, List<ForeignKey<?, ? extends Referable<?>, ?>>> MASTER_FOREIGN_KEYS = new ConcurrentHashMap<>(32);
//
//    @Resource(name = "commonThreadPool")
//    private ThreadPoolTaskExecutor pool;
//
//    @Lazy
//    @Resource
//    private List<IService<?>> iServices;
//
//    @Override
//    public void run(String... args) {
//        if (iServices != null) {
//            for (IService<?> iService : iServices) {
//                // 找到继承 IService 的接口
//                Class<?> iServiceClass = null;
//                for (Class<?> i : iService.getClass().getSuperclass().getInterfaces()) {
//                    if (IService.class.isAssignableFrom(i)) {
//                        iServiceClass = i;
//                    }
//                }
//                if (iServiceClass == null) {
//                    return;
//                }
//
//                // 收集外键
//                Class<?> entityClass = (Class<?>) ReflectUtil.getInterfaceGenericsType(iServiceClass, IService.class);
//                Constructor<?> constructor;
//                try {
//                    if (Referable.class.isAssignableFrom(entityClass)) {
//                        constructor = entityClass.getConstructor();
//                        constructor.setAccessible(true);
//                        Referable<?> r = (Referable<?>) constructor.newInstance();
//                        List<? extends ForeignKey<?, ? extends Referable<?>, ?>> foreignKeys = r.refer();
//                        // 收集外键, key 为从表类型
//                        SLAVE_FOREIGN_KEYS.put(entityClass, foreignKeys);
//                        // 收集外键, key 为主表类型
//                        for (ForeignKey<?, ? extends Referable<?>, ?> foreignKey : foreignKeys) {
//                            foreignKey.initMainTable();
//                            Class<?> mainClass = foreignKey.getMainClass();
//                            List<ForeignKey<?, ? extends Referable<?>, ?>> masterForeignKeys;
//                            if ((masterForeignKeys = MASTER_FOREIGN_KEYS.get(mainClass)) == null) {
//                                masterForeignKeys = new ArrayList<>();
//                                MASTER_FOREIGN_KEYS.put(mainClass, masterForeignKeys);
//                            }
//                            masterForeignKeys.addAll(foreignKeys);
//                        }
//                    }
//                } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
//                    throw new ReferenceException("索引初始化异常", e);
//                }
//            }
//        }
//    }
//
//    @Override
//    public Object intercept(Invocation invocation) throws Throwable {
//        Object result = null;
//        if (!SLAVE_FOREIGN_KEYS.isEmpty() && invocation.getArgs().length >= 2 &&
//                invocation.getArgs()[0] instanceof MappedStatement &&
//                invocation.getArgs()[1] instanceof Referable) {
//            MappedStatement statement = (MappedStatement) invocation.getArgs()[0];
//            Referable<?> reference = (Referable<?>) invocation.getArgs()[1];
//            BoundSql boundSql = statement.getBoundSql(reference);
//            Type entityType = ReflectUtil.getInterfaceGenericsType(reference.getClass().getSuperclass(), Referable.class);
//            String sql = boundSql.getSql();
//            switch (statement.getSqlCommandType()) {
//                case INSERT:
//                    sql = spliceSqlForInsert(sql, entityType, reference);
//                    break;
//                case UPDATE:
//                    sql = spliceSqlForUpdate(sql, entityType, reference);
//                    break;
//                case DELETE:
//                    sql = spliceSqlForDelete(sql, entityType, reference);
//                    break;
//                default:
//                    break;
//            }
//            PluginUtils.MPBoundSql mpBoundSql = PluginUtils.mpBoundSql(boundSql);
//            mpBoundSql.sql(sql);
//            result = invocation.proceed();
//            if (result instanceof Integer && (int) result <= 0) {
//                record(statement.getSqlCommandType(), entityType, reference);
//                throw new ReferenceException("数据无法关联");
//            }
//        }
//        if (result == null) {
//            return invocation.proceed();
//        } else {
//            return result;
//        }
//    }
//
//    private String spliceSqlForInsert(String sql, Type rt, Referable<?> reference) {
//        // 检查主表是否可关联
//        StringBuilder s = new StringBuilder(modifySqlForInsert(sql));
//        s.append(" WHERE");
//        for (ForeignKey<?, ? extends Referable<?>, ?> foreignKey : SLAVE_FOREIGN_KEYS.get(rt)) {
//            if (!s.toString().endsWith("WHERE")) {
//                s.append(" AND ");
//            }
//            String mainSelectSql = foreignKey.getMainSelectSql(reference);
//            s.append(" EXISTS(").append(mainSelectSql).append(") ");
//        }
//        return s.toString();
//    }
//
//    private String modifySqlForInsert(String sql) {
//        StringBuilder s = new StringBuilder(sql.substring(0, sql.indexOf("VALUES")));
//        s.append("SELECT * FROM (SELECT ");
//        String[] insertFields = sql.substring(sql.indexOf("(") + 1, sql.indexOf(")")).split(",");
//        for (String insertField : insertFields) {
//            s.append("?").append(insertField).append(",");
//        }
//        s.deleteCharAt(s.length() - 1);
//        s.append(") valid_reference ");
//        return s.toString();
//    }
//
//    private String spliceSqlForUpdate(String sql, Type rt, Referable<?> reference) {
//        // 检查主表是否可关联
//        return null;
//    }
//
//    private String spliceSqlForDelete(String sql, Type rt, Referable<?> reference) {
//        // 检查主表是否可删除
//        return "";
//    }
//
//    private void record(SqlCommandType type, Type rt, Referable<?> reference) {
//        // 记录日志
//    }
//}
