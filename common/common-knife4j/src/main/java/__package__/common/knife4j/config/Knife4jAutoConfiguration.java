package __package__.common.knife4j.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * @author ALazyDogXD
 * @date 2022/3/17 5:44
 * @description Kinfe4j 配置类
 */

@SuppressWarnings({"SpringFacetCodeInspection", "SpellCheckingInspection"})
@Configuration
@ComponentScan("__package__.common.knife4j")
@EnableSwagger2WebMvc
public class Knife4jAutoConfiguration {
}
