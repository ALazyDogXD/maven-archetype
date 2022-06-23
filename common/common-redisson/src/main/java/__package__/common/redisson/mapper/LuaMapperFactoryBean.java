package __package__.common.redisson.mapper;

import org.springframework.beans.factory.FactoryBean;

/**
 * @author ALazyDogXD
 * @date 2022/6/4 22:09
 * @description LuaMapper 代理生产工厂
 */

public class LuaMapperFactoryBean<T> implements FactoryBean<T> {

    private final Class<T> type;

    private LuaMapperCache luaMapperCache;

    public LuaMapperFactoryBean(Class<T> type) {
        this.type = type;
    }

    @Override
    public T getObject() {
        return luaMapperCache.get(type);
    }

    @Override
    public Class<T> getObjectType() {
        return type;
    }
}
