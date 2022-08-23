package io.github.pdkst.request.versions;

import org.springframework.lang.Nullable;

import javax.servlet.http.HttpServletRequest;

/**
 * @author pdkst
 * @since 2022/4/9
 */
public interface VersionRequestConditionProvider {
    /**
     * 创建一个版本条件
     *
     * @param request 请求
     * @return condition，如果请求条件没有版本，则返回null
     */
    @Nullable
    VersionRequestCondition create(HttpServletRequest request);
}
