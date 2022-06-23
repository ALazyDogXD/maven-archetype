package __package__.common.redisson.anno;

import java.lang.annotation.*;

/**
 * @author ALazyDogXD
 * @date 2022/6/5 2:54
 * @description LuaMapper 标识
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface LuaMapper {
    // Interface Mapper
}
