package __package__.common.redisson.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ALazyDogXD
 * @date 2022/4/10 18:27
 * @description Lua 脚本配置
 */

@Configuration
@ConfigurationProperties(prefix = "common.redisson.lua")
public class LuaScriptProperties {

    private List<String> scriptLocations = List.of("classpath*:/lua/**/*.lua");

    private Map<String, String> script;

    public List<String> getScriptLocations() {
        return scriptLocations;
    }

    public void setScriptLocations(List<String> scriptLocations) {
        this.scriptLocations = scriptLocations;
    }

    public String getScript(String fileName) {
        return script.get(fileName);
    }

    public void putScript(String fileName, String sha) {
        if (script == null) {
            script = new ConcurrentHashMap<>(32);
        }
        script.put(fileName, sha);
    }
}
