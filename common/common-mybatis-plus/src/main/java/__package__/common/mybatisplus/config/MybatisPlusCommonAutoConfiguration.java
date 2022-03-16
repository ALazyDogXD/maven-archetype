package __package__.common.mybatisplus.config;

import __package__.common.mybatisplus.handler.MetaHandler;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Mr_W
 * @date 2021/2/27 19:44
 * @description 基础服务配置
 */

@SuppressWarnings("SpellCheckingInspection")
@Configuration
@ComponentScan("__package__.common.mybatisplus")
public class MybatisPlusCommonAutoConfiguration {

    /**
     * 分页插件
     */
    @Bean
    public PaginationInnerInterceptor paginationInterceptor() {
        return new PaginationInnerInterceptor();
    }


    /**
     * 乐观锁插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }

    @Bean
    public GlobalConfig globalConfig(MetaHandler metaHandler) {
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setMetaObjectHandler(metaHandler);
        return globalConfig;
    }

    @Bean
    public MetaHandler metaHandler() {
        return new MetaHandler();
    }

}
