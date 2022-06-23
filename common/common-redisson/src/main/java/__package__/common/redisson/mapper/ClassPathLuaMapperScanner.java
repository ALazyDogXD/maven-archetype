package __package__.common.redisson.mapper;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.lang.NonNull;

import java.util.Map;
import java.util.Set;

/**
 * @author ALazyDogXD
 * @date 2022/6/4 22:28
 * @description Lua 脚本扫描
 */

public class ClassPathLuaMapperScanner extends ClassPathBeanDefinitionScanner {

    private LuaMapperCache cache;

    /** 文件名, lua 脚本 */
    private Map<String, String> scripts;

    public ClassPathLuaMapperScanner(BeanDefinitionRegistry registry) {
        super(registry, false);
    }

    public void setCache(LuaMapperCache cache) {
        this.cache = cache;
    }

    public void setScripts(Map<String, String> scripts) {
        this.scripts = scripts;
    }

    @Override
    @NonNull
    protected Set<BeanDefinitionHolder> doScan(@NonNull String... basePackages) {
        // 将脚本资源设置到 LuaMapperCache 中
        // 配置 LuaMapperFactoryBean 的 LuaMapperCache 属性
        // 代理 luaMapper 接口
        return super.doScan(basePackages);
    }
}
