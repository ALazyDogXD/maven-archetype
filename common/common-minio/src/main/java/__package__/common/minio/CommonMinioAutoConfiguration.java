package __package__.common.minio;

import __package__.common.minio.config.MinioProperties;
import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author ALazyDogXD
 * @date 2022/3/17 7:27
 * @description MinIO 配置类
 */

@ComponentScan
@EnableAutoConfiguration
@EnableConfigurationProperties
public class CommonMinioAutoConfiguration {

    private static final Logger LOGGER = getLogger(CommonMinioAutoConfiguration.class);

    @Bean
    public MinioClient minioClient(MinioProperties minioProperties) throws InvalidPortException, InvalidEndpointException {
        try {
            return new MinioClient(minioProperties.getEndpoint(),
                    minioProperties.getPort(),
                    minioProperties.getAccessKey(),
                    minioProperties.getSecretKey());
        } catch (InvalidEndpointException | InvalidPortException e) {
            LOGGER.error("MinIoClient 初始化失败");
            throw e;
        }
    }

}
