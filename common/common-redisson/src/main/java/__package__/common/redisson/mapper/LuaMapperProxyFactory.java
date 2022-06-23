package __package__.common.redisson.mapper;

import org.redisson.api.RedissonClient;

import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * @author ALazyDogXD
 * @date 2022/6/4 23:47
 * @description LuaMapper 代理生产工厂
 */

public class LuaMapperProxyFactory {

    private LuaMapperProxyFactory() {
    }

    public static <T> T create(Class<T> mapperInterface, Map<String, String> scripts, RedissonClient redisClient) {
        //noinspection unchecked
        return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[]{mapperInterface}, new LuaMapperProxy(scripts, redisClient));
    }

}