package __package__.common.redisson.anno;

import __package__.common.redisson.mapper.LuaMapperRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author ALazyDogXD
 * @date 2022/6/4 22:00
 * @description Lua 脚本扫描
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({LuaMapperRegistrar.class})
public @interface LuaMapperScan {

    String[] value() default {};

}
