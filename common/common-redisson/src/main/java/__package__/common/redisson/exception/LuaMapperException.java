package __package__.common.redisson.exception;

/**
 * @author ALazyDogXD
 * @date 2022/6/5 2:45
 * @description Lua 文件读取异常
 */

public class LuaMapperException extends RuntimeException {
    public LuaMapperException(String message) {
        super(message);
    }

    public LuaMapperException(String message, Throwable cause) {
        super(message, cause);
    }
}
