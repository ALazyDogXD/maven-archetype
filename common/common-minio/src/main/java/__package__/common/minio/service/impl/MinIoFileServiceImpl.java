package __package__.common.minio.service.impl;

import __package__.common.minio.exception.FileDeleteFailException;
import __package__.common.minio.exception.FileUploadFailException;
import __package__.common.minio.service.MinIoFileService;
import __package__.common.minio.util.MinIoUtil;
import io.minio.errors.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author Mr_W
 * @date 2021/3/13 14:07
 * @description MinIo 文件服务
 */
@Service
public class MinIoFileServiceImpl implements MinIoFileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MinIoFileServiceImpl.class);

    private static final String URL_SEPARATOR = "/";

    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("${minio.port}")
    private int port;

    @Override
    public String upload(String bucketName, String contentType, String path, String fileName, byte[] fileByte) throws FileUploadFailException {
        if (!path.endsWith(URL_SEPARATOR)) {
            path = path + URL_SEPARATOR;
        }
        try (InputStream in = new ByteArrayInputStream(fileByte)) {
            // 上传文件
            MinIoUtil.upload(bucketName, path + fileName, in, contentType);
        } catch (IOException | InvalidKeyException | NoSuchAlgorithmException | InsufficientDataException | InternalException | NoResponseException | InvalidBucketNameException | XmlPullParserException | ErrorResponseException | RegionConflictException | InvalidArgumentException | InvalidPortException | InvalidEndpointException e) {
            throw new FileUploadFailException("文件上传失败", e);
        }
        return endpoint + ":" + port + URL_SEPARATOR + bucketName + URL_SEPARATOR + path + fileName;
    }

    @Override
    public String upload(String bucketName, String contentType, String path, String fileName, InputStream in) throws FileUploadFailException {
        if (!path.endsWith(URL_SEPARATOR)) {
            path = path + URL_SEPARATOR;
        }
        try {
            // 上传文件
            MinIoUtil.upload(bucketName, path + fileName, in, contentType);
        } catch (IOException | InvalidKeyException | NoSuchAlgorithmException | InsufficientDataException | InternalException | NoResponseException | InvalidBucketNameException | XmlPullParserException | ErrorResponseException | RegionConflictException | InvalidArgumentException | InvalidPortException | InvalidEndpointException e) {
            throw new FileUploadFailException("文件上传失败", e);
        }
        return endpoint + ":" + port + URL_SEPARATOR + bucketName + URL_SEPARATOR + path + fileName;
    }

    @Override
    public String upload(String bucketName, String path, String fileName, byte[] fileByte) throws FileUploadFailException {
        return upload(bucketName, "application/octet-stream", path, fileName, fileByte);
    }

    @Override
    public void remove(String bucketName, String path) throws FileDeleteFailException {
        try {
            MinIoUtil.removeFile(bucketName, path);
        } catch (InvalidPortException | InvalidEndpointException | IOException | InvalidKeyException | NoSuchAlgorithmException | InsufficientDataException | InternalException | NoResponseException | InvalidBucketNameException | XmlPullParserException | ErrorResponseException e) {
            throw new FileDeleteFailException("文件上传失败", e);
        }
    }
}
