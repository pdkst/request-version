package io.github.pdkst.request.spring;

import io.github.pdkst.request.versions.VersionRequestConditionProvider;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * @author pdkst
 * @since 2022/3/24 15:04
 */
@Configuration
public class CustomWebMvcRegistrations implements WebMvcRegistrations {
    private final VersionRequestConditionProvider provider;

    public CustomWebMvcRegistrations(VersionRequestConditionProvider provider) {
        this.provider = provider;
    }

    @Override
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        return new VersionRequestMappingHandlerMapping(provider);
    }
}


