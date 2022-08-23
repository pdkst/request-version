package io.github.pdkst.request.spring;

import io.github.pdkst.request.versions.SinceVersion;
import io.github.pdkst.request.versions.VersionRequestCondition;
import io.github.pdkst.request.versions.VersionRequestConditionProvider;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

/**
 * @author pdkst
 */
public class VersionRequestMappingHandlerMapping extends RequestMappingHandlerMapping {
    private final VersionRequestConditionProvider provider;

    public VersionRequestMappingHandlerMapping(VersionRequestConditionProvider provider) {
        this.provider = provider;
    }

    @Override
    protected RequestCondition<?> getCustomMethodCondition(@NonNull Method method) {
        final SinceVersion annotation = AnnotatedElementUtils.findMergedAnnotation(method, SinceVersion.class);
        if (annotation == null) {
            return null;
        }
        final String value = annotation.value();
        return new VersionRequestCondition(value, annotation.fallback(), provider);
    }

    @Override
    protected RequestCondition<?> getCustomTypeCondition(@NonNull Class<?> handlerType) {
        final SinceVersion annotation = AnnotatedElementUtils.findMergedAnnotation(handlerType, SinceVersion.class);
        if (annotation == null) {
            return null;
        }
        final String value = annotation.value();
        return new VersionRequestCondition(value, annotation.fallback(), provider);
    }
}
