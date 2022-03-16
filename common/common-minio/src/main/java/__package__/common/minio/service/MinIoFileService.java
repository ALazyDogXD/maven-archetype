package __package__.common.minio.service;

import __package__.common.minio.exception.FileDeleteFailException;
import __package__.common.minio.exception.FileUploadFailException;

import java.io.InputStream;

/**
 * @author Mr_W
 * @date 2021/3/13 12:39
 * @description MinIo 文件服务
 */
public interface MinIoFileService {

    /**
     * 上传文件
     *
     * @param bucketName  桶名称
     * @param contentType 媒体类型
     * @param path        文件路径
     * @param fileName    文件名称
     * @param fileByte    文件字节数组
     * @return 图片路径
     * @throws FileUploadFailException 文件上传失败
     */
    String upload(String bucketName, String contentType, String path, String fileName, byte[] fileByte) throws FileUploadFailException;

    /**
     * 上传文件
     *
     * @param bucketName  桶名称
     * @param contentType 媒体类型
     * @param path        文件路径
     * @param fileName    文件名称
     * @param in          文件流
     * @return 图片路径
     * @throws FileUploadFailException 文件上传失败
     */
    String upload(String bucketName, String contentType, String path, String fileName, InputStream in) throws FileUploadFailException;

    /**
     * 上传文件
     *
     * @param bucketName 桶名称
     * @param path       文件路径
     * @param fileName   文件名称
     * @param fileByte   文件字节数组
     * @return 图片路径
     * @throws FileUploadFailException 文件上传失败
     */
    String upload(String bucketName, String path, String fileName, byte[] fileByte) throws FileUploadFailException;

    /**
     * 删除文件
     *
     * @param bucketName 桶名称
     * @param path       文件路径(包含文件名)
     * @throws FileDeleteFailException 文件删除失败
     */
    void remove(String bucketName, String path) throws FileDeleteFailException;

}
