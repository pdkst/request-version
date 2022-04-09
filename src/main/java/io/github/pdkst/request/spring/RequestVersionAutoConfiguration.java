package io.github.pdkst.request.spring;

import io.github.pdkst.request.versions.providers.HeaderVersionRequestConditionProvider;
import io.github.pdkst.request.versions.VersionRequestConditionProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author pdkst
 * @since 2022/4/9
 */
@Configuration
public class RequestVersionAutoConfiguration {

    @Bean
    @ConditionalOnBean(VersionRequestConditionProvider.class)
    public CustomWebMvcRegistrations customWebMvcRegistrations(VersionRequestConditionProvider provider) {
        return new CustomWebMvcRegistrations(provider);
    }

    @Bean
    @ConditionalOnMissingBean
    public VersionRequestConditionProvider versionRequestConditionProvider() {
        return new HeaderVersionRequestConditionProvider("x-version");
    }
}
