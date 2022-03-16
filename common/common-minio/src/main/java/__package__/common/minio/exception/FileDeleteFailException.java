package __package__.common.minio.exception;

/**
 * @author ALazyDogXD
 * @date 2022/3/17 7:20
 * @description 文件删除失败
 */

public class FileDeleteFailException extends Exception {
    public FileDeleteFailException(String message, Throwable cause) {
        super(message, cause);
    }
}
