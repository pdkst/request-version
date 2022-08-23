package io.github.pdkst;

import io.github.pdkst.request.spring.CustomWebMvcRegistrations;
import io.github.pdkst.request.versions.VersionRequestConditionProvider;
import io.github.pdkst.request.versions.providers.HeaderVersionRequestConditionProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 请求版本自动配置
 *
 * @author pdkst
 * @since 2022/4/9
 */
@Configuration
public class RequestVersionAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public VersionRequestConditionProvider versionRequestConditionProvider() {
        return new HeaderVersionRequestConditionProvider("x-version");
    }

    @Bean
    @ConditionalOnBean(VersionRequestConditionProvider.class)
    public CustomWebMvcRegistrations customWebMvcRegistrations(VersionRequestConditionProvider provider) {
        return new CustomWebMvcRegistrations(provider);
    }
}
