package __package__.common.mybatisplus.exception;

/**
 * @author ALazyDogXD
 * @date 2022/4/12 14:39
 * @description 索引异常
 */

public class ReferenceException extends RuntimeException {
    public ReferenceException(String message) {
        super(message);
    }

    public ReferenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
