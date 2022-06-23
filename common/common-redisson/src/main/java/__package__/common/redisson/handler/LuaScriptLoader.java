package __package__.common.redisson.handler;

import __package__.common.redisson.config.LuaScriptProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * @author ALazyDogXD
 * @date 2022/4/10 18:22
 * @description Lua 脚本加载器
 */

@Component
public class LuaScriptLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(LuaScriptLoader.class);

    private static final ResourcePatternResolver RESOURCE_RESOLVER = new PathMatchingResourcePatternResolver();

    private final LuaScriptProperties luaScriptProperties;

    private Map<String, String> scripts;

    public LuaScriptLoader(LuaScriptProperties luaScriptProperties) {
        this.luaScriptProperties = luaScriptProperties;
    }

    @PostConstruct
    public void init() throws IOException {
        if (scripts == null) {
            scripts = new ConcurrentHashMap<>(32);
            Resource[] resources = Optional.ofNullable(luaScriptProperties.getScriptLocations()).orElse(Collections.emptyList()).stream()
                    .flatMap(location -> Stream.of(getResources(location))).toArray(Resource[]::new);

            for (Resource resource : resources) {
                StringBuilder script = new StringBuilder();
                byte[] bytes = new byte[1024];
                InputStream inputStream = resource.getInputStream();
                int len;
                while ((len = inputStream.read(bytes)) != -1) {
                    script.append(new String(bytes, 0, len, StandardCharsets.UTF_8));
                }
                LOGGER.debug(script.toString());
                scripts.put(resource.getFilename(), script.toString());
            }

        }
    }

    private Resource[] getResources(String location) {
        try {
            return RESOURCE_RESOLVER.getResources(location);
        } catch (IOException e) {
            return new Resource[0];
        }
    }

    public Map<String, String> getScripts() {
        return scripts;
    }

}
