package __package__.common.minio.util;

import io.minio.MinioClient;
import io.minio.Result;
import io.minio.errors.*;
import io.minio.messages.Item;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.xmlpull.v1.XmlPullParserException;

import javax.annotation.Resource;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import static __package__.common.minio.constant.MimeConstant.*;

/**
 * @author Mr_W
 * @date 2021/2/17 15:05
 * @description MinIo 工具类
 */
@Component
public class MinIoUtil {

    private static MinioClient MINIO_CLIENT;

    private static String endpoint;

    private static int port;

    private static String accessKey;

    private static String secretKey;

    @Value("${minio.endpoint}")
    public void setEndpoint(String endpoint) {
        MinIoUtil.endpoint = endpoint;
    }

    @Value("${minio.port}")
    public void setPort(int port) {
        MinIoUtil.port = port;
    }

    @Value("${minio.accessKey}")
    public void setAccessKey(String accessKey) {
        MinIoUtil.accessKey = accessKey;
    }

    @Value("${minio.secretKey}")
    public void setSecretKey(String secretKey) {
        MinIoUtil.secretKey = secretKey;
    }

    private MinIoUtil() {
    }

    @Resource
    public void setMinioClient(MinioClient minioClient) {
        MinIoUtil.MINIO_CLIENT = minioClient;
    }

    /**
     * 获取 MinIo 客户端
     *
     * @return MinIo 客户端
     */
    private static MinioClient getMinIoClient() throws InvalidPortException, InvalidEndpointException {
        if (Objects.isNull(MINIO_CLIENT)) {
            synchronized (MinIoUtil.class) {
                if (Objects.isNull(MINIO_CLIENT)) {
                    MINIO_CLIENT = new MinioClient(endpoint, port, accessKey, secretKey);
                }
            }
        }
        return MINIO_CLIENT;
    }

    /**
     * 上传
     *
     * @param bucketName  桶名称
     * @param fileName    文件名称(含路径)
     * @param in          文件流
     * @param contentType 媒体格式
     */
    public static void upload(String bucketName, String fileName, InputStream in, String contentType) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException, RegionConflictException, InvalidArgumentException, InvalidPortException, InvalidEndpointException {
        MinioClient minioClient = getMinIoClient();
        if (!minioClient.bucketExists(bucketName)) {
            minioClient.makeBucket(bucketName);
        }
        minioClient.putObject(bucketName, fileName, in, in.available(), contentType);
    }

    /**
     * 下载
     *
     * @param bucketName 桶名称
     * @param fileName   文件名称(含路径)
     * @return 文件流
     */
    public static InputStream download(String bucketName, String fileName) throws InvalidPortException, InvalidEndpointException, IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InternalException, InvalidArgumentException {
        MinioClient minioClient = getMinIoClient();
        if (fileIsExists(bucketName, fileName)) {
            return minioClient.getObject(bucketName, fileName);
        }
        // 没有找到文件
        throw new FileNotFoundException("未查询到文件: " + fileName);
    }

    /**
     * 文件是否存在
     *
     * @param bucketName 桶名称
     * @param fileName   文件名称(含路径)
     * @return true 存在
     */
    public static boolean fileIsExists(String bucketName, String fileName) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException, InvalidPortException, InvalidEndpointException {
        MinioClient minioClient = getMinIoClient();
        if (minioClient.bucketExists(bucketName)) {
            for (Result<Item> result : minioClient.listObjects(bucketName)) {
                if (result.get().name.equals(fileName)) {
                    return true;
                }
            }

        }
        return false;
    }

    /**
     * 删除文件
     *
     * @param bucketName 桶名称
     * @param filePath   文件路径
     */
    public static void removeFile(String bucketName, String filePath) throws InvalidPortException, InvalidEndpointException, IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {
        MinioClient minioClient = getMinIoClient();
        minioClient.removeObject(bucketName, filePath);
    }

    /**
     * 判断 MinIo 服务文件上传时文件的类型contentType
     *
     * @param filenameExtension 文件后缀
     * @return 媒体类型
     */
    public static String getContentType(String filenameExtension) {
        if (EX_BMP.equalsIgnoreCase(filenameExtension)) {
            return MIME_BMP;
        }
        if (EX_GIF.equalsIgnoreCase(filenameExtension)) {
            return MIME_GIF;
        }
        if (EX_JPEG.equalsIgnoreCase(filenameExtension) ||
                EX_JPG.equalsIgnoreCase(filenameExtension) ||
                EX_PNG.equalsIgnoreCase(filenameExtension)) {
            return MIME_JPEG;
        }
        if (EX_HTML.equalsIgnoreCase(filenameExtension)) {
            return MIME_HTML;
        }
        if (EX_TXT.equalsIgnoreCase(filenameExtension)) {
            return MIME_TXT;
        }
        if (EX_VSD.equalsIgnoreCase(filenameExtension)) {
            return MIME_VSD;
        }
        if (EX_PPTX.equalsIgnoreCase(filenameExtension) ||
                EX_PPT.equalsIgnoreCase(filenameExtension)) {
            return MIME_PPTX;
        }
        if (EX_DOCX.equalsIgnoreCase(filenameExtension) ||
                EX_DOC.equalsIgnoreCase(filenameExtension)) {
            return MIME_DOCX;
        }
        if (EX_XML.equalsIgnoreCase(filenameExtension)) {
            return MIME_XML;
        }
        return MIME_OCTET_STREAM;
    }

}
