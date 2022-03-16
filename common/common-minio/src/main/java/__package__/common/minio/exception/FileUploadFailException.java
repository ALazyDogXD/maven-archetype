package __package__.common.minio.exception;

/**
 * @author ALazyDogXD
 * @date 2022/3/17 7:16
 * @description 文件上传失败
 */

public class FileUploadFailException extends Exception {
    public FileUploadFailException(String message, Throwable cause) {
        super(message, cause);
    }
}
