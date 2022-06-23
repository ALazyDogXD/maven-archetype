package __package__.common.redisson.config;

import __package__.common.redisson.handler.LuaScriptLoader;
import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

// 利用 mybatis 的 mapper, 复用 mybatis 的 mapper

/**
 * @author ALazyDogXD
 * @date 2022/4/10 14:48
 * @description Redis 配置
 */
@Configuration
public class RedisConfiguration {

    public RedisConfiguration(RedissonClient client, LuaScriptLoader scriptLoader, LuaScriptProperties properties) {
        Map<String, String> scripts = scriptLoader.getScripts();
        scripts.forEach((fileName, script) -> {
            String sha = client.getScript().scriptLoad(script);
            properties.putScript(fileName, sha);
            Object o = client.getScript().evalSha(RScript.Mode.READ_ONLY, sha, RScript.ReturnType.STATUS);
            System.out.println(o);
        });
    }

}
