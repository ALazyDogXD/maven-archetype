package __package__.common.knife4j;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * @author ALazyDogXD
 * @date 2022/3/17 5:44
 * @description Kinfe4j 配置类
 */

@SuppressWarnings("SpellCheckingInspection")
@ComponentScan
@EnableSwagger2WebMvc
@EnableAutoConfiguration
public class CommonKnife4jAutoConfiguration {
}
