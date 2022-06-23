package __package__.common.redisson.mapper;

import org.redisson.api.RedissonClient;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ALazyDogXD
 * @date 2022/6/4 23:37
 * @description LuaMapper 缓存
 */

public class LuaMapperCache {

    private final Map<Class<?>, Object> cache = new HashMap<>(16);

    private Map<Class<?>, Map<String, String>> scripts = new HashMap<>(16);

    private RedissonClient redissonClient;

    public void setScripts(Map<Class<?>, Map<String, String>> scripts) {
        this.scripts = scripts;
    }

    public void setRedissonClient(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public <T> T get(Class<T> type) {
        if (cache.containsKey(type)) {
            //noinspection unchecked
            return (T) cache.get(type);
        } else {
            T proxy = LuaMapperProxyFactory.create(type, scripts.get(type), redissonClient);
            cache.put(type, proxy);
            return proxy;
        }
    }

}
