package __package__.common.minio.config;

import __package__.common.minio.util.MinIoUtil;
import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import org.slf4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author ALazyDogXD
 * @date 2022/3/17 7:27
 * @description MinIO 配置类
 */

@SuppressWarnings("SpringFacetCodeInspection")
@Configuration
@ComponentScan("__package__.common.minio")
@ConfigurationProperties(prefix = "minio")
public class MinioAutoConfiguration {

    private static final Logger LOGGER = getLogger(MinioAutoConfiguration.class);

    private String endpoint;

    private int port;

    private String accessKey;

    private String secretKey;

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    @Bean
    public MinioClient minioClient() throws InvalidPortException, InvalidEndpointException {
        try {
            return new MinioClient(endpoint, port, accessKey, secretKey);
        } catch (InvalidEndpointException | InvalidPortException e) {
            LOGGER.error("MinIoClient 初始化失败");
            throw e;
        }
    }

}
