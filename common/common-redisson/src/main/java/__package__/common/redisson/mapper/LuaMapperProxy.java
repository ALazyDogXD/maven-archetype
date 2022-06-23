package __package__.common.redisson.mapper;

import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ALazyDogXD
 * @date 2022/6/4 23:50
 * @description LuaMapper 代理
 */

public class LuaMapperProxy implements InvocationHandler {

    private final Map<String, String> luaScripts;

    private final Map<String, String> shaScripts = new ConcurrentHashMap<>();

    private RedissonClient client;

    public LuaMapperProxy(Map<String, String> scripts, RedissonClient client) {
        luaScripts = scripts;
        this.client = client;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        String sha = shaScripts.get(method.getName());
        if (sha == null) {
            sha = client.getScript().scriptLoad(luaScripts.get(method.getName()));
            shaScripts.put(method.getName(), sha);
        }
        return client.getScript().evalSha(RScript.Mode.READ_ONLY, sha, RScript.ReturnType.STATUS);
    }
}
